package net.d3vr.dUralChat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main
extends JavaPlugin
{
	@Override
	public void onEnable() {
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new ChatListener(this), this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 1) {
            return false;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            this.reloadConfig();
            this.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "[dUralChat] Config reloaded");
            return true;
        }
        return args[0].equalsIgnoreCase("help");
	}
}
