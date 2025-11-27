package de.adrian.craftableHeads.Utility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DefaultHeadRecipe {

    private final JavaPlugin plugin;

    public DefaultHeadRecipe(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ---------- PUBLIC API ----------

    public void registerRecipe() {
        FileConfiguration cfg = plugin.getConfig();
        ConfigurationSection sec = cfg.getConfigurationSection("head-recipe");
        if (sec == null) {
            plugin.getLogger().warning("head-recipe section fehlt in config.yml");
            return;
        }

        if (!sec.getBoolean("enabled", true)) {
            return; // deaktiviert
        }

        // 1) ItemStack (Result) bauen
        ItemStack result = createItemMetaFromConfig(sec);

        // 2) Rezept aus config laden
        ShapedRecipe recipe = createShapedRecipeFromConfig(sec, result);

        if (recipe != null) {
            Bukkit.addRecipe(recipe);
            plugin.getLogger().info("Head-Recipe wurde registriert.");
        } else {
            plugin.getLogger().warning("Head-Recipe konnte nicht erstellt werden (prüfe config.yml).");
        }
    }

    // ---------- INTERN ----------

    private ItemStack createItemMetaFromConfig(ConfigurationSection sec) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        // Name
        String displayName = sec.getString("display-name", "&6NOT CUSTOM HEAD");
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        // Lore
        List<String> loreCfg = sec.getStringList("lore");
        List<String> lore = new ArrayList<>();

        if (loreCfg == null || loreCfg.isEmpty()) {
            lore.add("");
            lore.add("This is a not customized head!");
            lore.add("");
        } else {
            for (String line : loreCfg) {
                lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
        meta.setLore(lore);

        // IMPORTANT CHANGE: allow normal stack size (64) instead of forcing 1
        meta.setMaxStackSize(64);

        item.setItemMeta(meta);

        return item;
    }

    private ShapedRecipe createShapedRecipeFromConfig(ConfigurationSection sec, ItemStack result) {
        List<String> shapeList = sec.getStringList("shape");
        if (shapeList == null || shapeList.size() != 3) {
            plugin.getLogger().warning("head-recipe.shape muss genau 3 Zeilen haben.");
            return null;
        }

        String row1 = shapeList.get(0);
        String row2 = shapeList.get(1);
        String row3 = shapeList.get(2);

        if (row1.length() != 3 || row2.length() != 3 || row3.length() != 3) {
            plugin.getLogger().warning("Jede Zeile in head-recipe.shape muss genau 3 Zeichen haben.");
            return null;
        }

        NamespacedKey key = new NamespacedKey(plugin, "default_head");
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(row1, row2, row3);

        ConfigurationSection ingSec = sec.getConfigurationSection("ingredients");
        if (ingSec == null) {
            plugin.getLogger().warning("head-recipe.ingredients fehlt in config.yml");
            return null;
        }

        for (String charKey : ingSec.getKeys(false)) {
            if (charKey.length() != 1) {
                plugin.getLogger().warning("Ingredient-Key '" + charKey + "' ist nicht ein einzelnes Zeichen.");
                continue;
            }

            char symbol = charKey.charAt(0);
            String matName = ingSec.getString(charKey);

            if (matName == null) {
                plugin.getLogger().warning("Kein Material für '" + charKey + "' definiert.");
                continue;
            }

            Material mat = Material.matchMaterial(matName);
            if (mat == null) {
                plugin.getLogger().warning("Ungültiges Material '" + matName + "' für '" + charKey + "'.");
                continue;
            }

            recipe.setIngredient(symbol, mat);
        }

        return recipe;
    }
}
