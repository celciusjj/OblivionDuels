package com.celciusjj;

import com.celciusjj.duel.ManageDuels;
import com.celciusjj.listeners.InventoryEvents;
import com.celciusjj.listeners.RegionEvents;
import com.celciusjj.command.Commands;
import com.celciusjj.listeners.EntityEvents;
import com.celciusjj.utils.FileCreator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class OblivionDuels extends JavaPlugin {

    public String version = this.getDescription().getVersion();

    private FileCreator config, lang;

    public void onEnable() {
        Bukkit.getConsoleSender()
                .sendMessage("§5 The plugin has been enabled §b" + version);
        Bukkit.getConsoleSender().sendMessage("§5 Developed by §b Celcius");
        setupFiles();
        registerCommands();
        registerEvents();
    }

    public void onDisable() {
        removeAll();
        Bukkit.getConsoleSender()
                .sendMessage("§5 The plugin has been disable");
    }

    void setupFiles() {
        config = new FileCreator(this, "config");
        config.create();

        lang = new FileCreator(this, "lang");
        lang.create();
    }

    public void registerCommands() {
        this.getCommand("duel").setExecutor(new Commands(this));
    }

    void removeAll() {
        for (int i = 0; i < ManageDuels.battleList.size(); i++) {
            ManageDuels.battleList.get(i).getBanner().getEntity().remove();
        }
    }

    public void registerEvents() {
        PluginManager mg = getServer().getPluginManager();
        mg.registerEvents(new EntityEvents(this), this);
        mg.registerEvents(new RegionEvents(), this);
        mg.registerEvents(new InventoryEvents(), this);
    }

    public FileCreator getConfig() {
        return config;
    }

    public FileCreator getLang() {
        return lang;
    }

}
