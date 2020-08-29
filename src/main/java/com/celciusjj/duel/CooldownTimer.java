//import EntityParticles;
//import PvpBattle;
package com.celciusjj.duel;

import com.celciusjj.Main;
import com.celciusjj.handlers.EntityParticles;
import com.celciusjj.handlers.PvpBattle;
import com.celciusjj.listeners.EntityEvents;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.UUID;

public class CooldownTimer {

    private Main plugin;
    final int duelTime;

    public CooldownTimer(Main plugin) {
        this.plugin = plugin;
        this.duelTime = plugin.getConfig().getInt("Duel_Time");
    }

    public static ArrayList<PvpBattle> battleList = new ArrayList<>();

    public void endBattle(Player retador, Player target, EntityParticles banner) {
        int taskID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {

            retador.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lDuelo"),
                    "�La batalla ha finalizado en empate!", 1, 20, 1);
            target.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lDuelo"),
                    "�La batalla ha finalizado en empate!", 1, 20, 1);

            EntityEvents.entities.remove(retador.getUniqueId());

            for (int i = 0; i < battleList.size(); i++) {
                if (battleList.get(i).getRetador().equals(retador.getUniqueId())
                        && battleList.get(i).getTarget().equals(target.getUniqueId())) {
                    battleList.get(i).getBanner().getEntity().remove();
                    plugin.getServer().getScheduler()
                            .cancelTask(battleList.get(i).getBanner().getTaskIdParticles());
                    battleList.remove(i);
                }
            }
        }, this.duelTime);

        PvpBattle battle = new PvpBattle(retador.getUniqueId(), target.getUniqueId(), taskID, banner);
        battleList.add(battle);
    }

    public void stopRunneable(UUID target) {
        for (int i = 0; i < battleList.size(); i++) {
            if (battleList.get(i).getTarget().equals(target)
                    || battleList.get(i).getRetador().equals(target)) {
                plugin.getServer().getScheduler().cancelTask(battleList.get(i).getId());
                plugin.getServer().getScheduler().cancelTask(battleList.get(i).getBanner().getTaskIdParticles());
                battleList.get(i).getBanner().getEntity().remove();
                battleList.remove(i);
            }
        }
    }
}
