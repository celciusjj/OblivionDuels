package com.celciusjj.listeners;

import com.celciusjj.duel.DuelData;
import com.celciusjj.duel.ManageDuels;
import com.celciusjj.utils.InventoryCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import com.celciusjj.OblivionDuels;

public class InventoryEvents implements Listener {

    private final OblivionDuels plugin = OblivionDuels.getPlugin(OblivionDuels.class);
    InventoryCreator inventory = new InventoryCreator();

    @EventHandler
    public void closeInventoryScape(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(inventory.getInventoryName())) {
            Player player = (Player) event.getPlayer();
            if (DuelData.duelRequest.containsKey(player)) {
                DuelData.duelRequest.remove(player);
            }
        }
    }

    @EventHandler
    public void onInventoryClickItem(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(inventory.getInventoryName())) {
            if (event.getCurrentItem() == null) {
                return;
            }
            event.setCancelled(true);

            Player p = (Player) event.getWhoClicked();

            if (event.getClickedInventory().equals(p.getOpenInventory().getTopInventory())) {
                int slot = event.getSlot();
                if (slot == 6) {
                    p.closeInventory();
                } else if (slot == 2) {
                    ManageDuels duel = new ManageDuels(plugin);
                    duel.prepareBattle(ManageDuels.duelRequest.get(p), p);
                }
            }
        }
    }
}
