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
package me.ampayne2.DropParty.command.commands.remove;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.command.commands.set.CommandSetChest;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPChestsTable;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRemoveChest implements DPCommand {

	public static Map<String, String> playersRemoving = new HashMap<String, String>();

	@Override
	public void execute(CommandSender sender, String[] args) {
		String dpid;
		if (args.length == 1) {
			dpid = args[0];
		} else if (args.length == 2) {
			removeChestID(sender, args[1], args[0]);
			return;
		} else {
			return;
		}
		Player player = (Player) sender;
		if (DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().find() == null) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dppartydoesntexist"), dpid);
			return;
		}
		if (CommandSetChest.isSetting(player.getName()) && !isRemoving(player.getName())) {
			CommandSetChest.toggleSetting(player, dpid);
		}
		toggleRemoving(player, dpid);
	}

	public static void toggleRemoving(Player player, String dpid) {
		String playername = player.getName();
		if (isRemoving(playername) && getRemoving(playername).equals(dpid)) {
			playersRemoving.remove(playername);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpremovechestmodeoff"), dpid);
		} else {
			playersRemoving.put(playername, dpid);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpremovechestmode"), dpid);
		}
	}

	public static boolean isRemoving(String playername) {
		if (playersRemoving.containsKey(playername)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getRemoving(String playername) {
		return playersRemoving.get(playername);
	}

	public static void removeChest(Player player, String playerName, String dpid, String world, int x, int y, int z) {

		if (DatabaseManager.getDatabase().select(DPChestsTable.class).where().equal("world", world).and().equal("x", x).and().equal("y", y).and().equal("z", z).execute().findOne() == null) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpchestdoesntexist"), dpid);
			return;
		}

		DatabaseManager.getDatabase().remove(
				DatabaseManager.getDatabase().select(DPChestsTable.class).where().equal("dpid", dpid).and().equal("world", world).and().equal("x", x).and().equal("y", y).and().equal("z", z).execute()
						.findOne());
		DPMessageController.sendMessage(player, DPMessageController.getMessage("dpremovechest"), dpid);
	}

	public static void removeChestID(CommandSender sender, String dpid, String chest) {
		List<DPChestsTable> list = DatabaseManager.getDatabase().select(DPChestsTable.class).where().equal("dpid", dpid).execute().find();
		Player player = (Player) sender;
		int chestid;
		try {
			chestid = Integer.parseInt(chest);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "'" + chest + "'" + " is not an integer.");
			return;
		}
		if (!(list.size() >= chestid)) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpchestiddoesntexist"), dpid);
			return;
		}
		DPChestsTable entry = list.get(chestid - 1);
		DatabaseManager.getDatabase().remove(entry);
		DPMessageController.sendMessage(player, DPMessageController.getMessage("dpremovechest"), dpid);
	}
}
