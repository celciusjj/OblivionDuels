package com.celciusjj.command;

import com.celciusjj.Main;
import com.celciusjj.handlers.FileCreator;
import com.celciusjj.listeners.EntityEvents;
import com.celciusjj.duel.CreateRequestDuel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.HashMap;

public class Commands implements CommandExecutor {

    private Main main;
    private final FileCreator lang, config;
    CreateRequestDuel sendRequest;
    public HashMap<String, Long> cooldowns = new HashMap<String, Long>();

    public Commands(Main plugin) {
        this.main = plugin;
        this.lang = main.getLang();
        this.config = main.getConfig();
        sendRequest = new CreateRequestDuel();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int cooldownTime = 30;
        boolean isInPvp = false;
        if (!(sender instanceof Player)) {
            return false;
        }
        if (cooldowns.containsKey(sender.getName())) {
            long secondsLeft = ((cooldowns.get(sender.getName()) / 1000) + cooldownTime)
                    - (System.currentTimeMillis() / 1000);
            if (secondsLeft > 0) {
                sender.sendMessage("§cNo puede enviar peticiones hasta §f" + String.valueOf(secondsLeft) + " §csegundos");
                return true;
            } else {
                cooldowns.remove(sender.getName());
            }
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("duel")) {
            if (args.length == 0) {
                player.sendMessage("§c" + this.lang.getString("Commands.Null_Player"));
            } else {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("§c" + this.lang.getString("Commands.Not_Find_Player"));
                } else {
                    if (!player.getUniqueId().equals(target.getUniqueId())) {
                        if (EntityEvents.entities.size() != 0) {
                            if (EntityEvents.entities.containsKey(target.getUniqueId())
                                    || EntityEvents.entities.containsValue(target.getUniqueId())
                                    || EntityEvents.entities.containsKey(player.getUniqueId())
                                    || EntityEvents.entities.containsValue(player.getUniqueId())) {
                                isInPvp = true;
                            }
                        }
                        if (!isInPvp) {
                            if (player.getLocation().distanceSquared(target.getLocation()) <= Math.pow(config.getInt("Duel_Range"), 2)) {
                                cooldowns.put(sender.getName(), System.currentTimeMillis());
                                sendRequest.initializeRequest(player, target);
                            } else {
                                player.sendMessage("§c" + this.lang.getString("Commands.Out_Range_Player"));
                            }
                        } else {
                            player.sendMessage("§c" + this.lang.getString("Commands.Player_In_Duel"));
                        }
                    } else {
                        player.sendMessage("§c" + this.lang.getString("Commands.Self_Duel"));
                    }
                }
            }
        }
        return true;
    }
}
