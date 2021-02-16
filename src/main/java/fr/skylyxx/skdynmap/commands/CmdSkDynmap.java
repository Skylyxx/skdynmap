package fr.skylyxx.skdynmap.commands;

import fr.skylyxx.skdynmap.SkDynmap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class CmdSkDynmap implements CommandExecutor {
    private final SkDynmap skDynmap = SkDynmap.getINSTANCE();
    private final String prefix = "§6[SkDynmap] ";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                try {
                    skDynmap.reloadSkDynmapConfig();
                } catch (IOException | InvalidConfigurationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                sender.sendMessage(prefix + "§aConfiguration reloaded !");
            } else if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage("§c§m          §r " + prefix + "§r §c§m          §r");
                sender.sendMessage("§6SkDynmap version: §e" + skDynmap.getDescription().getVersion());
                sender.sendMessage("§6Author: §e" + skDynmap.getDescription().getAuthors().toString());
                sender.sendMessage("§6Documentation: §ehttps://github.com/Skylyxx/skdynmap#documentation");
            } else if (args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("update")) {
                String update = null;
                try {
                    update = skDynmap.checkForUpdates();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (update == null) {
                    sender.sendMessage(prefix + "§cAn error happend while trying to check for SkDynmap update !");
                } else {
                    if (update.equalsIgnoreCase("up-to-date")) {
                        sender.sendMessage(prefix + "§aGood ! You are running the latest version of SkDynmap !");
                    } else {
                        sender.sendMessage(prefix + "§eSkDynmap v" + update + " is available ! Download it at §6" + skDynmap.getDescription().getWebsite() + " §e!");
                    }
                }
            } else {
                sendHelp(sender);
            }
            return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§c§m          §r " + prefix + "§r §c§m          §r");
        sender.sendMessage("§6/skDynmap §ereload§b: Reload SkDynmap's configuration files");
        sender.sendMessage("§6/skDynmap §einfo§b: Get informations about SkDynmap");
        sender.sendMessage("§6/skDynmap §e(check|update)§b: Check for an update of SkDynmap");
        sender.sendMessage("§c§m          §r " + prefix + "§r §c§m          §r");
    }
}
