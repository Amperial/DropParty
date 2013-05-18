/*
 * This file is part of DropParty.
 *
 * Copyright (c) 2013-2013
 *
 * DropParty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DropParty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DropParty.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.DropParty;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DPMessageController {
	
	public static Map<String, String> messages = new HashMap<String, String>();

	public DPMessageController(Plugin plugin) {
		FileConfiguration config = plugin.getConfig();

		// prefix
		messages.put("dpprefix", config.getString("messages.dpprefix"));

		// command messages
		messages.put("dpcreate", config.getString("messages.dpcreate"));
		messages.put("dpdelete", config.getString("messages.dpdelete"));
		messages.put("dpstart", config.getString("messages.dpstart"));
		messages.put("dpstop", config.getString("messages.dpstop"));
		messages.put("dpsetchest", config.getString("messages.dpsetchest"));
		messages.put("dpsetchestmode", config.getString("messages.dpsetchestmode"));
		messages.put("dpsetchestmodeoff", config.getString("messages.dpsetchestmodeoff"));
		messages.put("dpremovechestmode", config.getString("messages.dpremovechestmode"));
		messages.put("dpremovechestmodeoff", config.getString("messages.dpremovechestmodeoff"));
		messages.put("dpsetitempoint", config.getString("messages.dpsetitempoint"));
		messages.put("dpsetitempointmode", config.getString("messages.dpsetitempointmode"));
		messages.put("dpsetitempointmodeoff", config.getString("messages.dpsetitempointmodeoff"));
		messages.put("dpsetteleport", config.getString("messages.dpsetteleport"));
		messages.put("dpsetitemdelay", config.getString("messages.dpsetitemdelay"));
		messages.put("dpsetmaxlength", config.getString("messages.dpsetmaxlength"));
		messages.put("dpsetmaxstack", config.getString("messages.dpsetmaxstack"));
		messages.put("dpremovechest", config.getString("messages.dpremovechest"));
		messages.put("dpremoveitempoint", config.getString("messages.dpremoveitempoint"));
		messages.put("dpremoveteleport", config.getString("messages.dpremoveteleport"));
		messages.put("dpteleport", config.getString("messages.dpteleport"));

		// list messages
		messages.put("dplistparties", config.getString("messages.dplistparties.dplistparties"));
		messages.put("dplistparties.party", config.getString("messages.dplistparties.party"));
		messages.put("dplistsettings", config.getString("messages.dplistsettings.dplistsettings"));
		messages.put("dplistsettings.itemdelay", config.getString("messages.dplistsettings.itemdelay"));
		messages.put("dplistsettings.maxlength", config.getString("messages.dplistsettings.maxlength"));
		messages.put("dplistsettings.maxstack", config.getString("messages.dplistsettings.maxstack"));
		messages.put("dplistteleport", config.getString("messages.dplistteleport.dplistteleport"));
		messages.put("dplistteleport.world", config.getString("messages.dplistteleport.world"));
		messages.put("dplistteleport.x", config.getString("messages.dplistteleport.x"));
		messages.put("dplistteleport.y", config.getString("messages.dplistteleport.y"));
		messages.put("dplistteleport.z", config.getString("messages.dplistteleport.z"));
		messages.put("dplistteleport.pitch", config.getString("messages.dplistteleport.pitch"));
		messages.put("dplistteleport.yaw", config.getString("messages.dplistteleport.yaw"));
		messages.put("dplistchests", config.getString("messages.dplistchests.dplistchests"));
		messages.put("dplistchests.chest", config.getString("messages.dplistchests.chest"));
		messages.put("dplistitempoints", config.getString("messages.dplistitempoints.dplistitempoints"));
		messages.put("dplistitempoints.itempoint", config.getString("messages.dplistitempoints.itempoint"));

		// broadcasts
		messages.put("dpannouncestart", config.getString("messages.dpannouncestart"));
		messages.put("dpannouncestop", config.getString("messages.dpannouncestop"));

		// errors
		messages.put("dpcreateerror", config.getString("messages.dpcreateerror"));
		messages.put("dpdeleteerror", config.getString("messages.dpdeleteerror"));
		messages.put("dpstarterror", config.getString("messages.dpstarterror"));
		messages.put("dpstoperror", config.getString("messages.dpstoperror"));
		messages.put("dpsetchesterror", config.getString("messages.dpsetchesterror"));
		messages.put("dpsetitempointerror", config.getString("messages.dpsetitempointerror"));
		messages.put("dpsetteleporterror", config.getString("messages.dpsetteleporterror"));
		messages.put("dpsetitemdelayerror", config.getString("messages.dpsetitemdelayerror"));
		messages.put("dpsetmaxlengtherror", config.getString("messages.dpsetmaxlengtherror"));
		messages.put("dpsetmaxstackerror", config.getString("messages.dpsetmaxstackerror"));
		messages.put("dpremovechesterror", config.getString("messages.dpremovechesterror"));
		messages.put("dpremoveitempointerror", config.getString("messages.dpremoveitempointerror"));
		messages.put("dpremoveteleporterror", config.getString("messages.dpremoveteleporterror"));
		messages.put("dpteleporterror", config.getString("messages.dpteleporterror"));

		// other
		messages.put("dpchestalreadyexists", config.getString("messages.dpchestalreadyexists"));
		messages.put("dpitempointalreadyexists", config.getString("messages.dpitempointalreadyexists"));
		messages.put("dpitempointiddoesntexist", config.getString("messages.dpitempointiddoesntexist"));
		messages.put("dppartydoesntexist", config.getString("messages.dppartydoesntexist"));
		messages.put("dpchestdoesntexist", config.getString("messages.dpchestdoesntexist"));
		messages.put("dpchestiddoesntexist", config.getString("messages.dpchestiddoesntexist"));
		messages.put("dppartyalreadyexists", config.getString("messages.dppartyalreadyexists"));
		messages.put("dppartyoutofitems", config.getString("messages.dppartyoutofitems"));
		messages.put("dppartyalreadyrunning", config.getString("messages.dppartyalreadyrunning"));
		messages.put("dppartynotrunning", config.getString("messages.dppartynotrunning"));
		messages.put("dpnopartiesfound", config.getString("messages.dpnopartiesfound"));
		messages.put("dpnoteleportfound", config.getString("messages.dpnoteleportfound"));
		messages.put("dpnochestsfound", config.getString("messages.dpnochestsfound"));
		messages.put("dpnoitempointsfound", config.getString("messages.dpnoitempointsfound"));
		messages.put("dpargumentserror", config.getString("messages.dpargumentserror"));
		messages.put("dpnopermission", config.getString("messages.dpnopermission"));
	}

	public static String getMessage(String messagetype) {
		return messages.get(messagetype);
	}

	public static void sendMessage(Player player, String message, String dpid) {
		if (message == null) {
			player.sendMessage("null");
			return;
		}
		message = message.replace("%PartyName%", dpid);
		message = messages.get("dpprefix").concat(" " + message);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	public static void broadcastMessage(String message, String dpid) {
		if (message == null)
			return;
		message = message.replace("%PartyName%", dpid);
		message = messages.get("dpprefix").concat(" " + message);
		Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
}
