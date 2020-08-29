package com.celciusjj.duel;

import com.celciusjj.Main;
import com.celciusjj.handlers.CountdownHandler;
import com.celciusjj.handlers.EntityParticles;
import com.celciusjj.listeners.EntityEvents;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class StartDuel {

    private Main main = Main.getPlugin(Main.class);
    private static double ALPHA = 0;

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
        int taskID = main.getServer().getScheduler().scheduleSyncRepeatingTask(main, () -> {
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
        CountdownHandler timer = new CountdownHandler(main, 1, () -> {
            FinishDuel cooldown = new FinishDuel(main);
            cooldown.startCountDown(desafiant, target, putFlag(desafiant, target));
            target.closeInventory();
        }, () -> {
            EntityEvents.entities.put(desafiant.getUniqueId(), target.getUniqueId());
            desafiant.sendTitle("§6§lDuelo", "Por la sangre y la gloria!",
                    1, 30, 1);
            target.sendTitle("§6§lDuelo", "Por la sangre y la gloria!",
                    1, 30, 1);
        }, (t) -> {
            desafiant.sendTitle("§6§lDuelo", "El duelo ha comenzado ", 1, 30,
                    1);
            target.sendTitle("§6§lDuelo", "El duelo ha comenzado ", 1, 30,
                    1);
        });

        // Start scheduling, don't use the "run" method unless you want to skip a second
        timer.scheduleTimer();
    }
}
