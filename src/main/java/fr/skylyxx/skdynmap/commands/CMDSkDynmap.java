package fr.skylyxx.skdynmap.commands;

import fr.skylyxx.skdynmap.SkDynmap;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class CMDSkDynmap implements CommandExecutor {
    private final SkDynmap skdynmap = SkDynmap.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                skdynmap.reloadAllConfigs();
                sender.sendMessage(Util.getPrefix() + "§aConfiguration reloaded !");
            } else if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage("§c§m          §r " + Util.getPrefix() + "§r §c§m          §r");
                sender.sendMessage("§6SkDynmap version: §e" + skdynmap.getDescription().getVersion());
                sender.sendMessage("§6Author: §e" + skdynmap.getDescription().getAuthors().toString());
                sender.sendMessage("§6Help/Documentation: §ehttps://github.com/SkylyxxFR/skdynmap");
            } else if (args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("update")) {
                String update = null;
                try {
                    update = SkDynmap.getInstance().checkForUpdates();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (update == null) {
                    sender.sendMessage(Util.getPrefix() + "§cAn error happend while trying to check for SkDynmap update !");
                } else {
                    if (update.equalsIgnoreCase("up-to-date")) {
                        sender.sendMessage(Util.getPrefix() + "§aGood ! You are running the latest version of SkDynmap !");
                    } else {
                        sender.sendMessage(Util.getPrefix() + "§eSkDynmap v" + update + " is available ! Download it at §6" + SkDynmap.getInstance().getDescription().getWebsite() + "/releases §e!");
                    }
                }
            } else {
                sendHelp(sender);
            }
            return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§c§m          §r " + Util.getPrefix() + "§r §c§m          §r");
        sender.sendMessage("§6/skdynmap §ereload§b: Reload SkDynmap's configuration files");
        sender.sendMessage("§6/skdynmap §einfo§b: Get informations about SkDynmap");
        sender.sendMessage("§6/skdynmap §e(check|update)§b: Check for an update of SkDynmap");
        sender.sendMessage("§c§m          §r " + Util.getPrefix() + "§r §c§m          §r");
    }
}
