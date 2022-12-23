package fr.supermax_8.slabutils;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class VanishCommand implements CommandExecutor {

    // OLD CODE BUT WORKS AND PRACTICAL
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("slabutils.commands")) return false;
            if (args.length == 1) {
                if (args[0].equals("list")) {
                    p.sendMessage("Currently in vanish:");
                    for (Player p2 : Bukkit.getOnlinePlayers()) {
                        if (p2.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                            p.sendMessage(p2.getName());
                        }
                    }
                    return false;
                }

                Player p2 = Bukkit.getPlayer(args[0]);
                if (p2.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    p2.removePotionEffect(PotionEffectType.INVISIBILITY);
                    p.sendMessage("Vanish remove for " + p2.getName());
                } else {
                    p2.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1, false, false, false));
                    p.sendMessage("Vanish add for " + p2.getName());
                }
            } else if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
                p.sendMessage("Vanish remove");
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1, false, false, false));
                p.sendMessage("Vanish add");
            }

        }

        return false;
    }
}
