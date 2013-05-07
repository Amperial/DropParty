package me.ampayne2.DropParty;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DropPartyMessageController{
	public static Map<String, String> messages = new HashMap<String, String>();
	public DropPartyMessageController(Plugin plugin){
		messages.put("dpcreate", plugin.getConfig().getString("messages.dpcreate"));
		messages.put("dpremove", plugin.getConfig().getString("messages.dpremove"));
		messages.put("dpstart", plugin.getConfig().getString("messages.dpstart"));
		messages.put("dpstop", plugin.getConfig().getString("messages.dpstop"));
		messages.put("dpannouncestart", plugin.getConfig().getString("messages.dpannouncestart"));
		messages.put("dpannouncestop", plugin.getConfig().getString("messages.dpannouncestop"));
		messages.put("dpsetchest", plugin.getConfig().getString("messages.dpsetchest"));
		messages.put("dpsetitempoint", plugin.getConfig().getString("messages.dpsetitempoint"));
		messages.put("dpsetteleport", plugin.getConfig().getString("messages.dpsetteleport"));
		messages.put("dpsetitemdelay", plugin.getConfig().getString("messages.dpsetitemdelay"));
		messages.put("dpsetmaxlength", plugin.getConfig().getString("messages.dpsetmaxlength"));
		messages.put("dpsetmaxstack", plugin.getConfig().getString("messages.dpsetmaxstack"));
		messages.put("dpremovechest", plugin.getConfig().getString("messages.dpremovechest"));
		messages.put("dpremoveitempoint", plugin.getConfig().getString("messages.dpremoveitempoint"));
		messages.put("dpteleport", plugin.getConfig().getString("messages.dpteleport"));
		messages.put("dpcreateerror", plugin.getConfig().getString("messages.dpcreateerror"));
		messages.put("dpremoveerror", plugin.getConfig().getString("messages.dpremoveerror"));
		messages.put("dpstarterror", plugin.getConfig().getString("messages.dpstarterror"));
		messages.put("dpstoperror", plugin.getConfig().getString("messages.dpstoperror"));
		messages.put("dpannouncestarterror", plugin.getConfig().getString("messages.dpannouncestarterror"));
		messages.put("dpannouncestoperror", plugin.getConfig().getString("messages.dpannouncestoperror"));
		messages.put("dpsetchesterror", plugin.getConfig().getString("messages.dpsetchesterror"));
		messages.put("dpsetitempointerror", plugin.getConfig().getString("messages.dpsetitempointerror"));
		messages.put("dpsetteleporterror", plugin.getConfig().getString("messages.dpsetteleporterror"));
		messages.put("dpsetitemdelayerror", plugin.getConfig().getString("messages.dpsetitemdelayerror"));
		messages.put("dpsetmaxlengtherror", plugin.getConfig().getString("messages.dpsetmaxlengtherror"));
		messages.put("dpsetmaxstackerror", plugin.getConfig().getString("messages.dpsetmaxstackerror"));
		messages.put("dpremovechesterror", plugin.getConfig().getString("messages.dpremovechesterror"));
		messages.put("dpremoveitempointerror", plugin.getConfig().getString("messages.dpremoveitempointerror"));
		messages.put("dpteleporterror", plugin.getConfig().getString("messages.dpteleporterror"));
		messages.put("dppartydoesntexist", plugin.getConfig().getString("messages.dppartydoesntexist"));
	}
	
	public static String getMessage(String messagetype){
		return messages.get(messagetype);
	}
	
	public static void sendMessage(Player player, String message, String dpid){
		if(message == null)return;
		message = message.replace("%PartyName%", dpid);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
}
