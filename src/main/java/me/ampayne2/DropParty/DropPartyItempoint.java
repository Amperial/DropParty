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

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyItempointsTable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class DropPartyItempoint {
	
	public static Location[] getItempoints(CommandSender sender){
		
		List<DropPartyItempointsTable> list = DatabaseManager.getDatabase()
				.select(DropPartyItempointsTable.class).execute().find();
		ListIterator<DropPartyItempointsTable> li = list.listIterator();
		Location[] itemPoints = new Location[list.size()];
		if (list.size() == 0) {
			sender.sendMessage(ChatColor.AQUA + "No Drop Party Item Points Found.");
			DropParty.toggleRunning(sender.getName(), sender);
		}
		int id = 0;
		while (li.hasNext()) {
			DropPartyItempointsTable entry = li.next();
			World tworld = Bukkit.getServer().getWorld(entry.world);
			itemPoints[id] = new Location(tworld, entry.x, entry.y, entry.z);
			id++;
		}
		return itemPoints;
		
	}
	
	public static void dropItemStack(ItemStack itemStack, Location[] itemPoints){
		Random generator = new Random();
		int itempoint = generator.nextInt(itemPoints.length);
		World world = itemPoints[itempoint].getWorld();
		if(itemStack != null){
			world.dropItemNaturally(itemPoints[itempoint], itemStack);
		}
	}
}
