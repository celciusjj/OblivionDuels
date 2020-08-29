package com.celciusjj.handlers;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class InventoryCreator {
    final String INVENTORY_NAME = "§aSolicitud de duelo";
    Inventory inventario;

    public Inventory createInventory(){
        inventario = Bukkit.createInventory(null, 9, INVENTORY_NAME);
        return fillInventory(inventario);
    }//§

    public Inventory fillInventory(org.bukkit.inventory.Inventory inv) {
        ItemStack item = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Denegar");
        meta.setDisplayName("§cDenegar");
        item.setItemMeta(meta);
        inv.setItem(6, item);

        item = new ItemStack(Material.GREEN_WOOL);
        meta = item.getItemMeta();
        meta.setDisplayName("§aAceptar");
        item.setItemMeta(meta);
        inv.setItem(2, item);

        item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        meta = item.getItemMeta();
        meta.setDisplayName("-");
        item.setItemMeta(meta);

        inv.setItem(0, item);
        inv.setItem(1, item);

        inv.setItem(3, item);
        inv.setItem(4, item);
        inv.setItem(5, item);

        inv.setItem(7, item);
        inv.setItem(8, item);

        return inv;
    }

    public String getInventoryName(){
        return INVENTORY_NAME;
    }
}
