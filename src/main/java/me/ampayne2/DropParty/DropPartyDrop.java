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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class DropPartyDrop extends BukkitRunnable{
	
	private static CommandSender sender;
	
	public static void setSender(CommandSender ssender){
		sender = ssender;
	}

	@Override
	public void run() {
		dropItems(sender);
	}
	
	public static void dropItems(CommandSender sender){
		Chest[] chests = DropPartyChest.getChests(sender);
		Location[] itemPoints = DropPartyItempoint.getItempoints(sender);
		ItemStack itemStack = DropPartyChest.getNextItemStack(sender, chests);
		DropPartyItempoint.dropItemStack(itemStack, itemPoints);
		if(DropParty.isRunning()){
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DropParty.getInstance(), new DropPartyDrop(), 5);
		}
	}
}