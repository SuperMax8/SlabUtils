package fr.supermax_8.slabutils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class NightNivisionCommand implements CommandExecutor {

    // OLD CODE BUT WORKS AND PRACTICAL
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("slabutils.commands") && sender instanceof Player) {
            Player p = (Player)sender;
            if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                p.sendMessage("NightVision off !");
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999999, 1, false, false, false));
                p.sendMessage("NightVision on !");
            }
        }

        return false;
    }


}