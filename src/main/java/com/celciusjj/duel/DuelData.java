package com.celciusjj.duel;

import com.celciusjj.OblivionDuels;
import com.celciusjj.handlers.PvpBattle;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DuelData {

    private final OblivionDuels PLUGIN = OblivionDuels.getPlugin(OblivionDuels.class);
    public static HashMap<Player, Player> duelRequest = new HashMap<>();
    public static ArrayList<PvpBattle> battleList = new ArrayList<>();


    void addPlayersToDuelRequest(Player desafiant, Player target) {
        duelRequest.put(desafiant, target);
    }

    void removePlayersFromDuel(UUID desafiant, UUID target){
        for (int i = 0; i < getBattleListSize(); i++) {
            if (battleList.get(i).getRetador().equals(desafiant)
                    && battleList.get(i).getTarget().equals(target)) {
                battleList.get(i).getBanner().getEntity().remove();
                PLUGIN.getServer().getScheduler()
                        .cancelTask(battleList.get(i).getBanner().getTaskIdParticles());
                battleList.remove(i);
            }
        }
    }

    void removePlayersFromDuelRunneable(UUID target){
        for (int i = 0; i < battleList.size(); i++) {
            if (battleList.get(i).getTarget().equals(target)
                    || battleList.get(i).getRetador().equals(target)) {
                PLUGIN.getServer().getScheduler().cancelTask(battleList.get(i).getId());
                PLUGIN.getServer().getScheduler().cancelTask(battleList.get(i).getBanner().getTaskIdParticles());
                battleList.get(i).getBanner().getEntity().remove();
                battleList.remove(i);
            }
        }
    }

    public int getBattleListSize() {
        return battleList.size();
    }

}
