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

import me.ampayne2.DropParty.command.commands.set.CommandSetItempoint;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DPGlowstoneListener implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Block placedBlock = event.getBlock();
		Player player = event.getPlayer();
		try {
			if (placedBlock.getType() != Material.GLOWSTONE) {
				return;
			}
		} catch (NullPointerException ex) {
			return;
		}
		if (!CommandSetItempoint.isSetting(player.getName())) {
			return;
		}
		String dpid = CommandSetItempoint.getSetting(player.getName());
		CommandSetItempoint.saveItemPoint(player, player.getName(), dpid, placedBlock.getWorld().getName(), placedBlock.getX(), placedBlock.getY(), placedBlock.getZ());
		event.setCancelled(true);
	}

}
