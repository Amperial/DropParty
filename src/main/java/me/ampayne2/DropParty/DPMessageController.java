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
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DPMessageController {
	public static Map<String, String> messages = new HashMap<String, String>();

	public DPMessageController(Plugin plugin) {
		// prefix
		messages.put("dpprefix", plugin.getConfig().getString("messages.dpprefix"));

		// command messages
		messages.put("dpcreate", plugin.getConfig().getString("messages.dpcreate"));
		messages.put("dpdelete", plugin.getConfig().getString("messages.dpdelete"));
		messages.put("dpstart", plugin.getConfig().getString("messages.dpstart"));
		messages.put("dpstop", plugin.getConfig().getString("messages.dpstop"));
		messages.put("dpsetchest", plugin.getConfig().getString("messages.dpsetchest"));
		messages.put("dpsetchestmode", plugin.getConfig().getString("messages.dpsetchestmode"));
		messages.put("dpsetchestmodeoff", plugin.getConfig().getString("messages.dpsetchestmodeoff"));
		messages.put("dpsetitempoint", plugin.getConfig().getString("messages.dpsetitempoint"));
		messages.put("dpsetitempointmode", plugin.getConfig().getString("messages.dpsetitempointmode"));
		messages.put("dpsetitempointmodeoff", plugin.getConfig().getString("messages.dpsetitempointmodeoff"));
		messages.put("dpsetteleport", plugin.getConfig().getString("messages.dpsetteleport"));
		messages.put("dpsetitemdelay", plugin.getConfig().getString("messages.dpsetitemdelay"));
		messages.put("dpsetmaxlength", plugin.getConfig().getString("messages.dpsetmaxlength"));
		messages.put("dpsetmaxstack", plugin.getConfig().getString("messages.dpsetmaxstack"));
		messages.put("dpremovechest", plugin.getConfig().getString("messages.dpremovechest"));
		messages.put("dpremoveitempoint", plugin.getConfig().getString("messages.dpremoveitempoint"));
		messages.put("dpteleport", plugin.getConfig().getString("messages.dpteleport"));

		// list messages
		messages.put("dplistparties", plugin.getConfig().getString("messages.dplistparties"));
		messages.put("dplistparties.party", plugin.getConfig().getString("messages.dplistparties.party"));
		messages.put("dplistsettings", plugin.getConfig().getString("messages.dplistsettings"));
		messages.put("dplistsettings.itemdelay", plugin.getConfig().getString("messages.dplistsettings.itemdelay"));
		messages.put("dplistsettings.maxlength", plugin.getConfig().getString("messages.dplistsettings.maxlength"));
		messages.put("dplistsettings.maxstack", plugin.getConfig().getString("messages.dplistsettings.maxstack"));
		messages.put("dplistteleport", plugin.getConfig().getString("messages.dplistteleport"));
		messages.put("dplistteleport.world", plugin.getConfig().getString("messages.dplistteleport.world"));
		messages.put("dplistteleport.x", plugin.getConfig().getString("messages.dplistteleport.x"));
		messages.put("dplistteleport.y", plugin.getConfig().getString("messages.dplistteleport.y"));
		messages.put("dplistteleport.z", plugin.getConfig().getString("messages.dplistteleport.z"));
		messages.put("dplistteleport.pitch", plugin.getConfig().getString("messages.dplistteleport.pitch"));
		messages.put("dplistteleport.yaw", plugin.getConfig().getString("messages.dplistteleport.yaw"));
		messages.put("dplistchests", plugin.getConfig().getString("messages.dplistchests"));
		messages.put("dplistchests.chest", plugin.getConfig().getString("messages.dplistchests.chest"));
		messages.put("dplistitempoints", plugin.getConfig().getString("messages.dplistitempoints"));
		messages.put("dplistitempoints.itempoint", plugin.getConfig().getString("messages.dplistitempoints.itempoint"));

		// broadcasts
		messages.put("dpannouncestart", plugin.getConfig().getString("messages.dpannouncestart"));
		messages.put("dpannouncestop", plugin.getConfig().getString("messages.dpannouncestop"));

		// errors
		messages.put("dpcreateerror", plugin.getConfig().getString("messages.dpcreateerror"));
		messages.put("dpdeleteerror", plugin.getConfig().getString("messages.dpdeleteerror"));
		messages.put("dpstarterror", plugin.getConfig().getString("messages.dpstarterror"));
		messages.put("dpstoperror", plugin.getConfig().getString("messages.dpstoperror"));
		messages.put("dpsetchesterror", plugin.getConfig().getString("messages.dpsetchesterror"));
		messages.put("dpsetitempointerror", plugin.getConfig().getString("messages.dpsetitempointerror"));
		messages.put("dpsetteleporterror", plugin.getConfig().getString("messages.dpsetteleporterror"));
		messages.put("dpsetitemdelayerror", plugin.getConfig().getString("messages.dpsetitemdelayerror"));
		messages.put("dpsetmaxlengtherror", plugin.getConfig().getString("messages.dpsetmaxlengtherror"));
		messages.put("dpsetmaxstackerror", plugin.getConfig().getString("messages.dpsetmaxstackerror"));
		messages.put("dpremovechesterror", plugin.getConfig().getString("messages.dpremovechesterror"));
		messages.put("dpremoveitempointerror", plugin.getConfig().getString("messages.dpremoveitempointerror"));
		messages.put("dpteleporterror", plugin.getConfig().getString("messages.dpteleporterror"));

		// other
		messages.put("dpchestalreadyexists", plugin.getConfig().getString("messages.dpchestalreadyexists"));
		messages.put("dpitempointalreadyexists", plugin.getConfig().getString("messages.dpitempointalreadyexists"));
		messages.put("dppartydoesntexist", plugin.getConfig().getString("messages.dppartydoesntexist"));
		messages.put("dppartyoutofitems", plugin.getConfig().getString("messages.dppartyoutofitems"));
		messages.put("dpnopartiesfound", plugin.getConfig().getString("messages.dpnopartiesfound"));
		messages.put("dpnoteleportfound", plugin.getConfig().getString("messages.dpnoteleportfound"));
		messages.put("dpnochestsfound", plugin.getConfig().getString("messages.dpnochestsfound"));
		messages.put("dpnoitempointsfound", plugin.getConfig().getString("messages.dpnoitempointsfound"));
		messages.put("dpargumentserror", plugin.getConfig().getString("messages.dpargumentserror"));
		messages.put("dpnopermission", plugin.getConfig().getString("messages.dpnopermission"));
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
