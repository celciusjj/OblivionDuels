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

public class FinishDuel {

    private Main plugin;
    final int duelTime;

    public FinishDuel(Main plugin) {
        this.plugin = plugin;
        this.duelTime = plugin.getConfig().getInt("Duel_Time");
    }

    public static ArrayList<PvpBattle> battleList = new ArrayList<>();

    public void startCountDown(Player desafiant, Player target, EntityParticles banner) {
        int taskID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {

            desafiant.sendTitle("§6§lDuelo",
                    "La batalla ha finalizado en empate!", 1, 40, 1);
            target.sendTitle("§6§lDuelo",
                    "La batalla ha finalizado en empate!", 1, 40, 1);

            EntityEvents.entities.remove(desafiant.getUniqueId());

            for (int i = 0; i < battleList.size(); i++) {
                if (battleList.get(i).getRetador().equals(desafiant.getUniqueId())
                        && battleList.get(i).getTarget().equals(target.getUniqueId())) {
                    battleList.get(i).getBanner().getEntity().remove();
                    plugin.getServer().getScheduler()
                            .cancelTask(battleList.get(i).getBanner().getTaskIdParticles());
                    battleList.remove(i);
                }
            }
        }, this.duelTime);

        PvpBattle battle = new PvpBattle(desafiant.getUniqueId(), target.getUniqueId(), taskID, banner);
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