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

    public static ItemStack Head(Integer amount, PlayerProfile profile) {
        ItemStack b = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta skullMeta = (SkullMeta) b.getItemMeta();
        skullMeta.setOwnerProfile(profile);

        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("This is a customised head!");
        lore.add("");
        skullMeta.setLore(lore);
        skullMeta.setDisplayName(ChatColor.GOLD + "CUSTOM HEAD");
        skullMeta.setCustomModelData(23);
        skullMeta.setMaxStackSize(1);

        b.setItemMeta(skullMeta);
        return b;
    }


    public static ItemStack Head(Integer amount, String link){
        PlayerProfile profile = Creator.getProfile(link);
        return Head(amount, profile);
    }
}
