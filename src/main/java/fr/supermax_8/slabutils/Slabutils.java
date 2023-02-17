package fr.supermax_8.slabutils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public final class Slabutils extends JavaPlugin implements Listener {

    private static HashSet<Material> slabs;

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("nightvision").setExecutor(new NightNivisionCommand());
        getCommand("vanish").setExecutor(new VanishCommand());

        slabs = new HashSet<>();
        for (Material m : Material.values()) {
            if (m.name().contains("_SLAB")) {
                slabs.add(m);
            }
        }

        Metrics metrics = new Metrics(this, 17159);
    }

    @EventHandler
    public void onDied(PlayerDeathEvent e) {
        final Player p = e.getEntity();
        if (!p.hasPermission("slabutils.commands")) return;
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

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void blockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        Block b = e.getBlock();
        if (!slabs.contains(b.getType())) return;
        Slab blockdata = (Slab) e.getBlock().getBlockData();
        checking:
        {
            if (this.isTop(e.getPlayer(), e.getBlock())) {
                if (blockdata.getType().equals(Type.DOUBLE)) {
                    blockdata.setType(Type.BOTTOM);
                    break checking;
                }
            } else if (blockdata.getType().equals(Type.DOUBLE)) {
                blockdata.setType(Type.TOP);
                break checking;
            }
            return;
        }
        e.setCancelled(true);
        b.setBlockData(blockdata, true);
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            b.getLocation().getWorld().dropItemNaturally(b.getLocation(), new ItemStack(b.getType()));
    }

    private boolean isTop(Player player, Block block) {
        Location start = player.getEyeLocation().clone();
        while (!start.getBlock().equals(block) && start.distance(player.getEyeLocation()) < 6.0D)
            start.add(player.getLocation().getDirection().multiply(0.05D));
        return start.getY() % 1.0D > 0.5D;
    }


}