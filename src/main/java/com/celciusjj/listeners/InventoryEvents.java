package com.celciusjj.listeners;

import com.celciusjj.handlers.InventoryCreator;
import com.celciusjj.request.CreateRequest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import static org.bukkit.Bukkit.getServer;

public class InventoryEvents implements Listener {

    InventoryCreator inventory = new InventoryCreator();

    @EventHandler
    public void closeInventoryScape(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(inventory.getInventoryName())) {
            Player player = (Player) event.getPlayer();
            if (CreateRequest.duelPrepare.containsKey(player)) {
                CreateRequest.duelPrepare.remove(player);
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
                    CreateRequest createRequest = new CreateRequest();
                    createRequest.countDown(CreateRequest.duelPrepare.get(p), p);
                }
            }
        }
    }
}
