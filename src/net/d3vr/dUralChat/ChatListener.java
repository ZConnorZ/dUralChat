package net.d3vr.dUralChat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.List;

public class ChatListener implements Listener {
	public Main plugin;
    public ChatListener(final Main instance) {
        this.plugin = instance;
    }
    
    @EventHandler
    public void PlayerCommandPreprocessEvent(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        for (final String s : this.plugin.getConfig().getConfigurationSection("delays").getValues(false).keySet()) {
            if (!event.getMessage().toLowerCase().startsWith(String.valueOf(s.toLowerCase()) + " ") && !event.getMessage().equalsIgnoreCase(s)) {
                continue;
            }
            if (Cooldown.hasCooldown(player.getName(), s)) {
                player.sendMessage(this.plugin.getConfig().getString("DELAY_MESSAGE").replaceAll("&", "§").replaceAll("<delay>", new StringBuilder().append(Cooldown.getCooldown(player.getName(), s)).toString()));
                event.setCancelled(true);
                return;
            }
            Cooldown.setCooldown(player.getName(), this.plugin.getConfig().getLong("delays." + s + ".delay") * 1000L, s);
        }
        if (!player.hasPermission("UralChat.ignore")) {
            boolean b = true;
            for (final String s2 : this.plugin.getConfig().getStringList("commands")) {
                if (event.getMessage().toLowerCase().startsWith(String.valueOf(s2.toLowerCase()) + " ") || event.getMessage().equalsIgnoreCase(s2)) {
                    b = false;
                }
            }
            if (b) {
                return;
            }
            final List<String> list = (List<String>)this.plugin.getConfig().getStringList("list");
            String msg = event.getMessage().toLowerCase();
            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                msg = msg.replaceAll(onlinePlayers.getName().toLowerCase(), "");
            }
            for (int x = 0; x < list.toArray().length; ++x) {
                String word = (String)list.toArray()[x];
                word = word.toLowerCase();
                if (msg.contains(word)) {
                    if (this.plugin.getConfig().getBoolean("BEEP_WORD")) {
                        event.setMessage(event.getMessage().toLowerCase().replaceAll(word.toLowerCase(), this.plugin.getConfig().getString("NEW_MESSAGE")));
                    }
                    if (this.plugin.getConfig().getBoolean("REPLACE_MESSAGE")) {
                        event.setMessage(this.plugin.getConfig().getString("NEW_MESSAGE"));
                    }
                    else if (!this.plugin.getConfig().getBoolean("BEEP_WORD")) {
                        event.setCancelled(true);
                    }
                    if (this.plugin.getConfig().getBoolean("EXPLOSION_ON_SWEAR")) {
                        event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), 0.0f);
                    }
                    this.plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), this.plugin.getConfig().getString("COMMAND_ON_SWEAR").replace("<player>", event.getPlayer().getName()));
                    if (Boolean.parseBoolean(this.plugin.getConfig().getString("KICK_ON_SWEAR"))) {
                        player.kickPlayer(this.plugin.getConfig().getString("KICK_MESSAGE").replaceAll("&", "§"));
                    }
                    else {
                        player.sendMessage(this.plugin.getConfig().getString("KICK_MESSAGE").replaceAll("&", "§"));
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        if (Cooldown.hasCooldown(player.getName(), "chat")) {
            player.sendMessage(this.plugin.getConfig().getString("DELAY_MESSAGE_CHAT").replaceAll("&", "§").replaceAll("<delay>", new StringBuilder().append(Cooldown.getCooldown(player.getName(), "chat")).toString()));
            event.setCancelled(true);
            return;
        }
        Cooldown.setCooldown(player.getName(), this.plugin.getConfig().getLong("delays.chat.delay") * 1000L, "chat");
        if (!player.hasPermission("UralChat.ignore")) {
            final List<String> list = (List<String>)this.plugin.getConfig().getStringList("list");
            String msg = event.getMessage().toLowerCase();
            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                msg = msg.replaceAll(onlinePlayers.getName().toLowerCase(), "");
            }
            for (int x = 0; x < list.toArray().length; ++x) {
                String word = (String)list.toArray()[x];
                word = word.toLowerCase();
                if (msg.contains(word)) {
                    if (this.plugin.getConfig().getBoolean("BEEP_WORD")) {
                        event.setMessage(event.getMessage().toLowerCase().replaceAll(word.toLowerCase(), this.plugin.getConfig().getString("NEW_MESSAGE")));
                    }
                    if (this.plugin.getConfig().getBoolean("REPLACE_MESSAGE")) {
                        event.setMessage(this.plugin.getConfig().getString("NEW_MESSAGE"));
                    }
                    else if (!this.plugin.getConfig().getBoolean("BEEP_WORD")) {
                        event.setCancelled(true);
                    }
                    if (this.plugin.getConfig().getBoolean("EXPLOSION_ON_SWEAR")) {
                        event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), 0.0f);
                    }
                    this.plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), this.plugin.getConfig().getString("COMMAND_ON_SWEAR").replace("<player>", event.getPlayer().getName()));
                    if (this.plugin.getConfig().getBoolean("KICK_ON_SWEAR")) {
                        Bukkit.getScheduler().runTask(this.plugin, new Runnable() {
							@Override
							public void run() {
							player.kickPlayer(plugin.getConfig().getString("KICK_MESSAGE"));
							}
                        	
                        });
                    }
                    else {
                        player.sendMessage(this.plugin.getConfig().getString("KICK_MESSAGE").replaceAll("&", "§"));
                    }
                }
            }
        }
    }
}