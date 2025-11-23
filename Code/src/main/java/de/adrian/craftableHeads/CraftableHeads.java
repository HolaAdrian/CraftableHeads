package de.adrian.craftableHeads;

import de.adrian.craftableHeads.Commands.CustomiceHead;
import de.adrian.craftableHeads.Utility.DefaultHeadRecipe;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftableHeads extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        DefaultHeadRecipe headRecipe = new DefaultHeadRecipe(this);
        headRecipe.registerRecipe();
        Permission skinperm = new Permission("customhead.change", "Allows the creator to change the heads skin.");
        getCommand("changehead").setExecutor(new CustomiceHead());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
