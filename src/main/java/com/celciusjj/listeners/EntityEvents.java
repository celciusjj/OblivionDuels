package com.celciusjj.listeners;

import com.celciusjj.Main;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.celciusjj.duel.CooldownTimer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class EntityEvents implements Listener {

    Main main;

    public EntityEvents(Main main) {
        this.main = main;
    }

    public static BiMap<UUID, UUID> entities = HashBiMap.create();
    RegionCommands region = new RegionCommands();

    @EventHandler
    public void entity_damage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player pDamaged = (Player) e.getEntity();
            Player pDamager = (Player) e.getDamager();

            if (!region.getRegionPlayerDamage(pDamaged)) {
                if (entities.size() != 0) {
                    if (entities.get(pDamager.getUniqueId()) != null) {
                        UUID pDamagedNew = entities.get(pDamager.getUniqueId());
                        if (pDamagedNew.equals(pDamaged.getUniqueId())) {
                        } else {
                            e.setCancelled(true);
                            return;
                        }
                    } else if (entities.get(pDamaged.getUniqueId()) != null) {
                        BiMap<UUID, UUID> entitiesNew = entities;
                        UUID pDamagerNew = entitiesNew.get(pDamaged.getUniqueId());

                        if (pDamagerNew.equals(pDamager.getUniqueId())) {
                        } else {
                            e.setCancelled(true);
                            return;
                        }
                    } else {
                        e.setCancelled(true);
                        return;
                    }
                } else {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            Player deathPlayer = (Player) e.getEntity();
            CooldownTimer cooldown = new CooldownTimer(main);
            if (entities.containsValue(deathPlayer.getUniqueId())) {
                cooldown.stopRunneable(deathPlayer.getUniqueId());
                entities.inverse().remove(deathPlayer.getUniqueId());
            } else if (entities.containsKey(deathPlayer.getUniqueId())) {
                cooldown.stopRunneable(deathPlayer.getUniqueId());
                entities.remove(deathPlayer.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (entities.containsValue(player.getUniqueId())) {
            CooldownTimer cooldown = new CooldownTimer(main);
            cooldown.stopRunneable(player.getUniqueId());
            entities.inverse().remove(player.getUniqueId());
        } else if (entities.containsKey(player.getUniqueId())) {
            CooldownTimer cooldown = new CooldownTimer(main);
            cooldown.stopRunneable(player.getUniqueId());
            entities.remove(player.getUniqueId());
        }
    }

}
