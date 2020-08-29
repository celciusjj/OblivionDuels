package com.celciusjj.duel;
import com.celciusjj.handlers.InventoryCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class CreateRequestDuel implements Listener {

    public static HashMap<Player, Player> duelPrepare = new HashMap<Player, Player>();

    public void initializeRequest(Player desafiant, Player target) {
        if (!duelPrepare.containsKey(desafiant) && !duelPrepare.containsValue(desafiant) && !duelPrepare.containsKey(target)
                && !duelPrepare.containsValue(target)) {
            duelPrepare.put(target, desafiant);

            target.sendMessage("§7Ha recibido una solicitud de duelo de §a" + desafiant.getName());

            desafiant.sendMessage("§7La solicitud ha sido enviada con exito a §a" + target.getName());

            InventoryCreator inventory = new InventoryCreator();
            target.openInventory(inventory.createInventory());

        } else {
            desafiant.sendMessage("§cUsted o el jugador retado tiene una solicitud de duelo pendiente.");
        }
    }
}
