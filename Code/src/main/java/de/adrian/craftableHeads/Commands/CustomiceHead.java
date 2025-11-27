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
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command,
                             @NotNull String s,
                             @NotNull String[] strings) {

        if (!commandSender.hasPermission("customhead.change")) {
            commandSender.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
            return false;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "You have to be a player to perform this command!");
            return false;
        }

        Player p = (Player) commandSender;

        if (strings.length != 1) {
            commandSender.sendMessage(ChatColor.RED + "Syntax: /changehead <LINK>");
            return false;
        }

        ItemStack inHand = p.getInventory().getItemInMainHand();

        if (inHand == null || inHand.getType() != Material.PLAYER_HEAD) {
            commandSender.sendMessage(ChatColor.RED + "You have to have a player head in your main hand!");
            return false;
        }

        int amount = inHand.getAmount();
        if (amount <= 0) {
            commandSender.sendMessage(ChatColor.RED + "You have to have a player head in your main hand!");
            return false;
        }

        // If the player holds more than 1 head, we will need an extra inventory slot
        // because the custom head is non-stackable.
        if (amount > 1 && p.getInventory().firstEmpty() == -1) {
            p.sendMessage(ChatColor.RED + "Your inventory is full. Please clear at least one slot before changing the head.");
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

        // Create the custom non-stackable head
        ItemStack customHead = Creator.Head(1, link);

        if (amount > 1) {
            // Case: stack > 1
            // 1) Decrease the stack in hand by 1 (unchanged heads remain stackable)
            inHand.setAmount(amount - 1);

            // 2) Add the custom head to the inventory (separate slot)
            p.getInventory().addItem(customHead);
        } else {
            // Case: exactly 1 head in hand
            // Just replace it with the custom head, no extra slot needed
            p.getInventory().setItemInMainHand(customHead);
        }

        p.sendMessage(ChatColor.GREEN + "Your head's skin was changed.");
        return true;
    }
}
