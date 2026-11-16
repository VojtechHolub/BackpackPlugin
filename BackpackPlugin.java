package cz.chatgpt.backpack;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BackpackPlugin extends JavaPlugin implements Listener, CommandExecutor {
    private final Map<UUID, Inventory> openBackpacks = new HashMap<>();
    private File dataFile;
    private YamlConfiguration data;

    @Override
    public void onEnable() {
        dataFile = new File(getDataFolder(), "backpacks.yml");
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().severe("Nepodarilo se vytvorit slozku pluginu.");
        }
        data = YamlConfiguration.loadConfiguration(dataFile);

        getServer().getPluginManager().registerEvents(this, this);
        if (getCommand("backpack") != null) {
            getCommand("backpack").setExecutor(this);
        }
        getLogger().info("Backpack byl zapnut.");
    }

    @Override
    public void onDisable() {
        for (Map.Entry<UUID, Inventory> entry : openBackpacks.entrySet()) {
            saveBackpack(entry.getKey(), entry.getValue());
        }
        saveData();
        openBackpacks.clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Tento prikaz mohou pouzit pouze hraci.");
            return true;
        }
        if (!player.hasPermission("backpack.use")) {
            player.sendMessage(ChatColor.RED + "Na pouziti batohu nemas opravneni.");
            return true;
        }

        UUID uuid = player.getUniqueId();
        Inventory backpack = openBackpacks.computeIfAbsent(uuid, id -> {
            Inventory inventory = getServer().createInventory(null, 54,
                    ChatColor.DARK_GREEN + "Batoh");
            java.util.List<?> stored = data.getList("players." + id);
            if (stored != null) {
                ItemStack[] contents = new ItemStack[54];
                for (int i = 0; i < Math.min(stored.size(), contents.length); i++) {
                    Object item = stored.get(i);
                    if (item instanceof ItemStack stack) contents[i] = stack;
                }
                inventory.setContents(contents);
            }
            return inventory;
        });

        player.openInventory(backpack);
        return true;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        Inventory inventory = event.getInventory();
        if (openBackpacks.get(player.getUniqueId()) == inventory) {
            saveBackpack(player.getUniqueId(), inventory);
            saveData();
            openBackpacks.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Inventory inventory = openBackpacks.remove(uuid);
        if (inventory != null) {
            saveBackpack(uuid, inventory);
            saveData();
        }
    }

    private void saveBackpack(UUID uuid, Inventory inventory) {
        data.set("players." + uuid, inventory.getContents());
    }

    private void saveData() {
        try {
            data.save(dataFile);
        } catch (IOException exception) {
            getLogger().severe("Nepodarilo se ulozit batohy: " + exception.getMessage());
        }
    }
}
