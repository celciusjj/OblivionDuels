package com.celciusjj;

import com.celciusjj.listeners.InventoryEvents;
import com.celciusjj.listeners.RegionCommands;
import com.celciusjj.request.CreateRequest;
import com.celciusjj.command.Commands;
import com.celciusjj.listeners.EntityDamage;
import com.celciusjj.handlers.FileCreator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    PluginDescriptionFile pdffile = getDescription();
    public String version = pdffile.getVersion();

    private FileCreator config, lang;

    public void onEnable() {
        Bukkit.getConsoleSender()
                .sendMessage("§5The plugin has been enabled §b" + version);
        Bukkit.getConsoleSender().sendMessage("§5Developed by §bCelcius");
        setupFiles();
        registerCommands();
        registerEvents();
    }

    public void onDisable() {
        removeAll();
        Bukkit.getConsoleSender()
                .sendMessage("§5The plugin has been disable");
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
        for (int i = 0; i < CooldownTimer.battleList.size(); i++) {
            CooldownTimer.battleList.get(i).getBanner().getEntity().remove();
        }
    }

    public void registerEvents() {
        PluginManager mg = getServer().getPluginManager();
        mg.registerEvents(new CreateRequest(), this);
        mg.registerEvents(new EntityDamage(this), this);
        mg.registerEvents(new RegionCommands(), this);
        mg.registerEvents(new InventoryEvents(), this);
    }

    public FileCreator getConfig() {
        return config;
    }

    public FileCreator getLang() {
        return lang;
    }

}
