package de.adrian.craftableHeads.Commands;


import de.adrian.craftableHeads.Utility.Creator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;

public class CustomiceHead implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!commandSender.hasPermission("customhead.change")){
            commandSender.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
            return false;
        }
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.RED + "You have to be a player to perform this command!");
            return false;
        }
        Player p = (Player) commandSender;

        if (strings.length != 1){
            commandSender.sendMessage(ChatColor.RED + "Syntax: /changehead <LINK>");
            return false;
        }

        if(p.getInventory().getItemInMainHand() == null){
            commandSender.sendMessage(ChatColor.RED + "You have to have a player head in your main hand!");
            return false;
        }

        if(p.getInventory().getItemInMainHand().getType() != Material.PLAYER_HEAD){
                commandSender.sendMessage(ChatColor.RED + "You have to have a player head in your main hand!");
                return false;

        }

        String link = strings[0];

        PlayerProfile profile;
        try {
            profile = Creator.getProfile(link);
        } catch (Exception e) {
            p.sendMessage(ChatColor.RED + "This texture link is invalid or unreachable.");
            return false;
        }
        System.out.println(profile);

        ItemStack head = Creator.Head(1, link);
        p.getInventory().setItemInMainHand(head);

        p.sendMessage(ChatColor.GREEN + "You're heads skin was changed.");


        return false;
    }


}
