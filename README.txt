BACKPACK – Spigot 26.3

Funkce:
- /backpack otevře osobní batoh o velikosti 54 slotů (6 řad)
- /bp je zkratka
- každý hráč má vlastní obsah podle UUID
- obsah se ukládá do plugins/Backpack/backpacks.yml při zavření batohu,
  odpojení hráče a vypnutí pluginu
- oprávnění: backpack.use (výchozí: všichni hráči)

POZOR:
Toto je zdrojový projekt, nikoli hotový .jar. Pro sestavení je potřeba JDK 25
a Maven. V kořenové složce projektu spusť:
    mvn package

Výsledný soubor bude:
    target/backpack-1.0.0.jar

Instalace na Aternos:
1. V Aternosu nastav serverový software Spigot 26.3.
2. DŮLEŽITÉ: Aternos nepovoluje nahrávání vlastních pluginových JAR souborů.
   Lze instalovat pouze pluginy z jejich katalogu. Tento vlastní plugin tedy
   na bezplatném Aternosu přímo nenahraješ.
3. Pro použití tohoto vlastního pluginu potřebuješ hosting, který dovoluje
   nahrávat vlastní JAR (např. exaroton), nebo musí být plugin nejprve vydán
   na podporované platformě a schválen/přidán do katalogu Aternosu.
