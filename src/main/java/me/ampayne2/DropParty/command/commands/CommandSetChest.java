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
package me.ampayne2.DropParty.command.commands;

import java.util.ArrayList;
import me.ampayne2.DropParty.command.interfaces.DropPartyCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyChestsTable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetChest implements DropPartyCommand {

	private final static ArrayList<String> playersSetting = new ArrayList<String>();

	public static void toggleSetting(String playerName, CommandSender sender) {
		if (isSetting(playerName)) {
			playersSetting.remove(playerName);
			sender.sendMessage(ChatColor.AQUA + "SetChest mode deactivated.");
		} else {
			playersSetting.add(playerName);
			sender.sendMessage(ChatColor.AQUA + "SetChest mode activated.");
		}
	}

	public static boolean isSetting(String playerName) {
		return playersSetting.contains(playerName);
	}

	public static void saveChest(Player player, String playerName, int x,
			int y, int z) {
		if (DatabaseManager.getDatabase().select(DropPartyChestsTable.class)
				.where().equal("x", x).and().equal("y", y).and().equal("z", z)
				.execute().findOne() != null) {
			player.sendMessage(ChatColor.RED
					+ "This chest is already a drop party chest.");
			return;
		}
		playersSetting.remove(playerName);
		DropPartyChestsTable table = new DropPartyChestsTable();
		table.x = x;
		table.y = y;
		table.z = z;
		DatabaseManager.getDatabase().save(table);
		player.sendMessage(ChatColor.AQUA + "Chest Set Successfully.");

	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String playerName = sender.getName();
		if (CommandRemoveChest.isRemoving(playerName)) {
			CommandRemoveChest.toggleRemoving(playerName, sender);
		}
		toggleSetting(playerName, sender);
	}
}
