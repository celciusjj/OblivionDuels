package com.celciusjj.listeners;

import com.celciusjj.Main;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.List;


public class RegionCommands implements Listener {

    private final Main plugin = Main.getPlugin(Main.class);
    List<String> lista = plugin.getConfig().getStringList("Duel_Zones_Exception");

    @EventHandler
    public void toUseTheCommand(PlayerCommandPreprocessEvent event) {
        Player jugador = event.getPlayer();
        String comando = event.getMessage();

        if (comando.toLowerCase().startsWith("/duelo") || comando.toLowerCase().startsWith("/duel")) {
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(jugador.getWorld()));
            ApplicableRegionSet regionSet = regionManager.getApplicableRegions(BlockVector3.at(jugador.getLocation().getX(), jugador.getLocation().getY(), jugador.getLocation().getZ()));
            for (ProtectedRegion region : regionSet) {
                for (String list : lista) {
                    if (region.getId().equals(list)) {
                        event.setCancelled(true);
                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lNo puedes retar a un duelo en este lugar"));
                        return;
                    }
                }
            }
        }
    }

    public boolean getRegionPlayerDamage(Player jugador) {
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(jugador.getWorld()));
        ApplicableRegionSet regionSet = regionManager.getApplicableRegions(BlockVector3.at(jugador.getLocation().getX(), jugador.getLocation().getY(), jugador.getLocation().getZ()));
        for (ProtectedRegion region : regionSet) {
            if (region.getId().equals("arena")) {
                return true;
            }
        }
        return false;
    }
}
