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
package me.ampayne2.DropParty.listeners;

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
		Block clickedBlock = event.getClickedBlock();
		try {
			if (clickedBlock.getType() != Material.CHEST) {
				return;
			}
		} catch (NullPointerException ex) {
			return;
		}
		clickedBlock.getWorld().getName();
		event.setCancelled(true);
	}

	@EventHandler
	public void onChestBreak(BlockBreakEvent event) {
		Block clickedBlock = event.getBlock();
		Player player = event.getPlayer();
		try {
			if (clickedBlock.getType() != Material.CHEST) {
				return;
			}
		} catch (NullPointerException ex) {
			return;
		}
		if(DatabaseManager.getDatabase()
				.select(DropPartyChestsTable.class).where()
				.equal("world", clickedBlock.getWorld()).and()
				.equal("x", clickedBlock.getX()).and()
				.equal("y", clickedBlock.getY()).and()
				.equal("z", clickedBlock.getZ()).execute().find() != null){
			if(!player.hasPermission("dropparty.removechest")){
				player.sendMessage("You do not have permission to break a Drop Party Chest.");
				event.setCancelled(true);
				return;
			}
			DatabaseManager.getDatabase().remove(DatabaseManager.getDatabase()
					.select(DropPartyChestsTable.class).where()
					.equal("world", clickedBlock.getWorld()).and()
					.equal("x", clickedBlock.getX()).and()
					.equal("y", clickedBlock.getY()).and()
					.equal("z", clickedBlock.getZ()).execute().find());
			player.sendMessage(ChatColor.AQUA + "Drop Party Chest Removed.");
		}
	}
}
