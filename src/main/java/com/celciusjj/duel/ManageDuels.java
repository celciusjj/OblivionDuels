package com.celciusjj.duel;

import com.celciusjj.OblivionDuels;
import com.celciusjj.handlers.CountdownHandler;
import com.celciusjj.utils.EntityParticles;
import com.celciusjj.handlers.PvpBattle;
import com.celciusjj.listeners.EntityEvents;
import com.celciusjj.utils.InventoryCreator;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import java.util.UUID;

public class ManageDuels extends DuelData{

    private OblivionDuels plugin;
    final int duelTime;
    private static double ALPHA = 0;

    public ManageDuels(OblivionDuels plugin) {
        this.plugin = plugin;
        this.duelTime = plugin.getConfig().getInt("Duel_Time");
    }

    public void initializeRequest(Player desafiant, Player target) {
        if (!duelRequest.containsKey(desafiant) && !duelRequest.containsValue(desafiant) && !duelRequest.containsKey(target)
                && !duelRequest.containsValue(target)) {

            addPlayersToDuelRequest(desafiant, target);

            target.sendMessage("§7Ha recibido una solicitud de duelo de §a" + desafiant.getName());
            desafiant.sendMessage("§7La solicitud ha sido enviada con exito a §a" + target.getName());

            InventoryCreator inventory = new InventoryCreator();
            target.openInventory(inventory.createInventory());
        } else {
            desafiant.sendMessage("§cUsted o el jugador retado tiene una solicitud de duelo pendiente.");
        }
    }


    public EntityParticles putFlag(Player retador, Player target) {
        Location loc = new Location(retador.getWorld(),
                (retador.getLocation().getX() + target.getLocation().getX()) / 2,
                ((retador.getLocation().getY() + retador.getLocation().getY()) / 2) + 4,
                (retador.getLocation().getZ() + target.getLocation().getZ()) / 2);
        Entity entity = loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        ArmorStand armorStand = (ArmorStand) entity;
        armorStand.setGravity(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName( "§f" + retador.getName() + " §cVS §f" + target.getName());
        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        EntityParticles banner = new EntityParticles(entity, startCountdownParticles(loc));
        return banner;
    }


    public int startCountdownParticles(Location loc) {
        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ALPHA += Math.PI / 16;
            Location firstLocation = loc.clone().add(Math.cos(ALPHA), Math.sin(ALPHA) + 2, Math.sin(ALPHA));
            Location secondLocation = loc.clone().add(Math.cos(ALPHA + Math.PI), Math.sin(ALPHA) + 2,
                    Math.sin(ALPHA + Math.PI));
            loc.getWorld().spawnParticle(Particle.FLAME, firstLocation, 0, 0, 0, 0, 0);
            loc.getWorld().spawnParticle(Particle.FLAME, secondLocation, 0, 0, 0, 0, 0);

        }, 0, 1);
        return taskID;
    }

    public void prepareBattle(Player desafiant, Player target) {
        CountdownHandler timer = new CountdownHandler(plugin, 1, () -> {
            startCountDown(desafiant, target, putFlag(desafiant, target));
            target.closeInventory();
        }, () -> {
            EntityEvents.entities.put(desafiant.getUniqueId(), target.getUniqueId());
            desafiant.sendTitle("§6Duelo", "Por la sangre y la gloria!",
                    1, 40, 1);
            target.sendTitle("§6Duelo", "Por la sangre y la gloria!",
                    1, 40, 1);
        }, (t) -> {
            desafiant.sendTitle("§6Duelo", "El duelo ha comenzado ", 1, 30,
                    1);
            target.sendTitle("§6Duelo", "El duelo ha comenzado ", 1, 30,
                    1);
        });
        // Start scheduling, don't use the "run" method unless you want to skip a second
        timer.scheduleTimer();
    }

    public void startCountDown(Player desafiant, Player target, EntityParticles banner) {
        int taskID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {

            desafiant.sendTitle("§6§lDuelo",
                    "La batalla ha finalizado en empate!", 1, 40, 1);
            target.sendTitle("§6§lDuelo",
                    "La batalla ha finalizado en empate!", 1, 40, 1);

            EntityEvents.entities.remove(desafiant.getUniqueId());

            removePlayersFromDuel(target.getUniqueId(), desafiant.getUniqueId());

        }, this.duelTime);

        PvpBattle battle = new PvpBattle(desafiant.getUniqueId(), target.getUniqueId(), taskID, banner);
        battleList.add(battle);
    }

    public void stopRunneable(UUID target) {
        removePlayersFromDuelRunneable(target);
    }
}
