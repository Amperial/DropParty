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

import me.ampayne2.DropParty.command.commands.CommandRemoveChest;
import me.ampayne2.DropParty.command.commands.CommandSetChest;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyChestsTable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class DropPartyChestListener implements Listener {

	@EventHandler
	public void onChestHit(PlayerInteractEvent event) {
		if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		String playerName = event.getPlayer().getDisplayName();
		Player player = event.getPlayer();
		if (!CommandSetChest.isSetting(playerName)
				&& !CommandRemoveChest.isRemoving(playerName)) {
			return;
		}
		if (CommandSetChest.isSetting(playerName)
				&& CommandRemoveChest.isRemoving(playerName)) {
			return;
		}

		Block clickedBlock = event.getClickedBlock();

		try {
			if (clickedBlock.getType() != Material.CHEST) {
				return;
			}
		} catch (NullPointerException ex) {
			return;
		}
		clickedBlock.getWorld().getName();

		if (CommandSetChest.isSetting(playerName)) {
			CommandSetChest.saveChest(player, playerName, clickedBlock
					.getWorld().getName(), clickedBlock.getX(), clickedBlock
					.getY(), clickedBlock.getZ());
		}

		if (CommandRemoveChest.isRemoving(playerName)) {
			if (DatabaseManager.getDatabase()
					.select(DropPartyChestsTable.class).where()
					.equal("world", clickedBlock.getWorld().getName()).and()
					.equal("x", clickedBlock.getX()).and()
					.equal("y", clickedBlock.getY()).and()
					.equal("z", clickedBlock.getZ()).execute().findOne() == null) {
				player.sendMessage(ChatColor.RED
						+ "This chest is not a drop party chest.");
				event.setCancelled(true);
				return;
			}
			CommandRemoveChest.deleteChest(player, playerName, clickedBlock
					.getWorld().getName(), clickedBlock.getX(), clickedBlock
					.getY(), clickedBlock.getZ());
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void onChestBreak(BlockBreakEvent event) {
		Block clickedBlock = event.getBlock();
		Player player = event.getPlayer();
		String playerName = event.getPlayer().getDisplayName();

		try {
			if (clickedBlock.getType() != Material.CHEST) {
				return;
			}
		} catch (NullPointerException ex) {
			return;
		}

		CommandRemoveChest.deleteChest(player, playerName, clickedBlock
				.getWorld().getName(), clickedBlock.getX(),
				clickedBlock.getY(), clickedBlock.getZ());
	}

}
