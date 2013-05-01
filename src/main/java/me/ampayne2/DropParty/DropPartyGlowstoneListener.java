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

import me.ampayne2.DropParty.command.commands.CommandRemoveItempoint;
import me.ampayne2.DropParty.command.commands.CommandSetItempoint;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyItempointsTable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DropPartyGlowstoneListener implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		String playerName = event.getPlayer().getDisplayName();
		Player player = event.getPlayer();
		if (!CommandSetItempoint.isSetting(playerName)
				&& !CommandRemoveItempoint.isRemoving(playerName)) {
			return;
		}
		if (CommandSetItempoint.isSetting(playerName)
				&& CommandRemoveItempoint.isRemoving(playerName)) {
			return;
		}

		Block placedBlock = event.getBlock();

		try {
			if (placedBlock.getType() != Material.GLOWSTONE) {
				return;
			}
		} catch (NullPointerException ex) {
			return;
		}

		if (CommandSetItempoint.isSetting(playerName)) {
			CommandSetItempoint.saveItempoint(player, playerName, placedBlock
					.getWorld().getName(), placedBlock.getX(), placedBlock
					.getY(), placedBlock.getZ());
		}

		if (CommandRemoveItempoint.isRemoving(playerName)) {
			if (DatabaseManager.getDatabase()
					.select(DropPartyItempointsTable.class).where()
					.equal("world", placedBlock.getWorld().getName()).and()
					.equal("x", placedBlock.getX()).and()
					.equal("y", placedBlock.getY()).and()
					.equal("z", placedBlock.getZ()).execute().findOne() == null) {
				player.sendMessage(ChatColor.RED
						+ "There is no Drop Party Itempoint here.");
				event.setCancelled(true);
				return;
			}
			CommandRemoveItempoint.deleteItempoint(player, playerName,
					placedBlock.getX(), placedBlock.getY(), placedBlock.getZ());
		}
		event.setCancelled(true);
	}

}
