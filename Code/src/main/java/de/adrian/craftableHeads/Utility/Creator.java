package de.adrian.craftableHeads.Utility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class Creator {

    public static PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject);
        profile.setTextures(textures);
        return profile;
    }

    public static ItemStack Head(Integer amount, String link){
        ItemStack b = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta SkullMeta = (SkullMeta) b.getItemMeta();
        //PlayerProfile profile = Creator.getProfile("http://textures.minecraft.net/texture/76fdd4b13d54f6c91dd5fa765ec93dd9458b19f8aa34eeb5c80f455b119f278");
        PlayerProfile profile = Creator.getProfile(link);
        SkullMeta.setOwnerProfile(profile);

        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("This is a customised head!");
        lore.add("");
        SkullMeta.setLore(lore);
        SkullMeta.setDisplayName(ChatColor.GOLD + "CUSTOM HEAD");
        SkullMeta.setCustomModelData(23);
        SkullMeta.setMaxStackSize(1);
        b.setItemMeta(SkullMeta);
        return b;
    }

}
