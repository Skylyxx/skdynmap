package fr.skylyxx.skdynmap.commands;

import fr.skylyxx.skdynmap.SkDynmap;
import fr.skylyxx.skdynmap.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CMDSkDynmap implements CommandExecutor {
    private final SkDynmap skdynmap = SkDynmap.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c§m          §r " + Util.getPrefix() + "§r §c§m          §r");
            sender.sendMessage("§6/skdynmap §ereload§b: Reload SkDynmap's configuration files");
            sender.sendMessage("§6/skdynmap §einfo§b: Get informations about SkDynmap");
            sender.sendMessage("§c§m          §r " + Util.getPrefix() + "§r §c§m          §r");
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                skdynmap.reloadSkDynmapConfig();
                sender.sendMessage(Util.getPrefix() + "§aConfiguration reloaded !");
            } else if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage("§c§m          §r " + Util.getPrefix() + "§r §c§m          §r");
                sender.sendMessage("§6SkDynmap version: §e" + skdynmap.getDescription().getVersion());
                sender.sendMessage("§6Author: §e" + skdynmap.getDescription().getAuthors().toString());
                sender.sendMessage("§6Help/Documentation: §ehttps://github.com/SkylyxxFR/skdynmap");
            } else {
                sender.sendMessage("§c§m          §r " + Util.getPrefix() + "§r §c§m          §r");
                sender.sendMessage("§6/skdynmap §ereload§b: Reload SkDynmap's configuration files");
                sender.sendMessage("§6/skdynmap §einfo§b: Get informations about SkDynmap");
                sender.sendMessage("§c§m          §r " + Util.getPrefix() + "§r §c§m          §r");
            }
            return true;
        }

        return false;
    }
}
