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

import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyChestsTable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DropPartyChest {
	
	public static Chest[] getChests(CommandSender sender){
		List<DropPartyChestsTable> list = DatabaseManager.getDatabase()
				.select(DropPartyChestsTable.class).execute().find();
		ListIterator<DropPartyChestsTable> li = list.listIterator();
		Chest[] chests = new Chest[list.size()];
		if (list.size() == 0) {
			sender.sendMessage(ChatColor.AQUA + "No Drop Party Chests Found.");
			DropParty.toggleRunning(sender.getName(), sender);
		}
		int id = 0;
		while (li.hasNext()) {
			DropPartyChestsTable entry = li.next();
			World tworld = Bukkit.getServer().getWorld(entry.world);
			Location chestloc = new Location(tworld, entry.x, entry.y, entry.z);
			chests[id] = (Chest) chestloc.getBlock().getState();
			id++;
		}
		return chests;
		
	}
	
	public static ItemStack getNextItemStack(CommandSender sender, Chest[] chests){
		int chest = 0;
		Inventory inv = chests[chest].getBlockInventory();
		int slotindex = 0;
		while(DropParty.isRunning()) {
			if(slotindex == 27){
				slotindex = 0;
				if(chests.length == chest+1){
					sender.sendMessage(ChatColor.AQUA + "Drop Party has run out of items.");
					DropParty.toggleRunning(sender.getName(), sender);
				}else{
					chest++;
					inv = chests[chest].getBlockInventory();
				}

			}
			if(inv.getItem(slotindex) != null){
				ItemStack itemStack = inv.getItem(slotindex);
				inv.setItem(slotindex, null);
				return itemStack;
			}
			slotindex++;
		}
		return null;
	}
}
