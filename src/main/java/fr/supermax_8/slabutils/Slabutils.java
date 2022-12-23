package fr.supermax_8.slabutils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public final class Slabutils extends JavaPlugin implements Listener {

    // OLD CODE BUT WORKS AND PRACTICAL

    private static HashSet<Material> slabs = new HashSet<>();

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("nightvision").setExecutor(new NightNivisionCommand());
        this.getCommand("vanish").setExecutor(new VanishCommand());

        for (Material m : Material.values()) {
            if (m.name().contains("_SLAB")) {
                slabs.add(m);
            }
        }
    }

    @EventHandler
    public void onDied(PlayerDeathEvent e) {
        final Player p = e.getEntity();
        if (p.hasPermission("slabutils.commands")) {
            if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                new BukkitRunnable() {
                    public void run() {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1, false, false, false));
                    }
                }.runTaskLater(this, 10L);
            }

            if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                new BukkitRunnable() {
                    public void run() {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999999, 1, false, false, false));
                    }
                }.runTaskLater(this, 10L);
            }
        }

    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        Block b = e.getBlock();
        if (slabs.contains(b.getType())) {
            Slab blockdata = (Slab) e.getBlock().getBlockData();
            if (this.isTop(e.getPlayer(), e.getBlock())) {
                if (blockdata.getType().equals(Type.DOUBLE)) {
                    blockdata.setType(Type.BOTTOM);
                    b.setBlockData(blockdata, true);
                    e.setCancelled(true);
                }
            } else {
                if (blockdata.getType().equals(Type.DOUBLE)) {
                    blockdata.setType(Type.TOP);
                    b.setBlockData(blockdata, true);
                    e.setCancelled(true);
                }
            }
        }
    }

    private boolean isTop(Player player, Block block) {
        Location start = player.getEyeLocation().clone();

        while (!start.getBlock().equals(block) && start.distance(player.getEyeLocation()) < 6.0D) {
            start.add(player.getLocation().getDirection().multiply(0.05D));
        }

        return start.getY() % 1.0D > 0.5D;
    }


}