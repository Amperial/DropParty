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
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import me.ampayne2.DropParty.command.commands.remove.CommandRemoveChest;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPChestsTable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DPChest {

	public static Map<String, Chest[]> chests = new HashMap<String, Chest[]>();

	public static Chest[] getChests(CommandSender sender, String dpid) {
		Player player = (Player) sender;
		List<DPChestsTable> list = DatabaseManager.getDatabase().select(DPChestsTable.class).where().equal("dpid", dpid).execute().find();
		if (list.size() == 0) {
			return null;
		}
		ListIterator<DPChestsTable> li = list.listIterator();
		Chest[] chests = new Chest[list.size()];
		if (list.size() == 0) {
			DPPartyController.stop(player, dpid);
		}
		int id = 0;
		while (li.hasNext()) {
			DPChestsTable entry = li.next();
			World tworld = Bukkit.getServer().getWorld(entry.world);
			Location chestloc = new Location(tworld, entry.x, entry.y, entry.z);
			if (chestloc.getBlock().getState() instanceof Chest) {
				chests[id] = (Chest) chestloc.getBlock().getState();
				id++;
			} else {
				CommandRemoveChest.removeChest(player, player.getName(), dpid, entry.world, entry.x, entry.y, entry.z);
			}
		}
		return chests;
	}

	public static ItemStack getNextItemStack(CommandSender sender, Chest[] chests, String dpid, Integer maxstack) {
		int chest = 0;
		Inventory inv = chests[chest].getBlockInventory();
		int slotindex = 0;
		while (DPPartyController.isRunning(dpid)) {
			if (slotindex == 27) {
				slotindex = 0;
				if (chests.length == chest + 1) {
					Player player = (Player) sender;
					DPMessageController.sendMessage(player, DPMessageController.getMessage("dppartyoutofitems"), dpid);
					DPPartyController.stop(player, dpid);
				} else {
					chest++;
					inv = chests[chest].getBlockInventory();
				}
			}
			if (inv.getItem(slotindex) != null) {
				ItemStack itemStack = inv.getItem(slotindex);
				if (itemStack.getAmount() <= maxstack) {
					inv.setItem(slotindex, null);
					return itemStack;
				} else {
					itemStack.setAmount(itemStack.getAmount() - maxstack);
					return new ItemStack(itemStack.getType(), maxstack);
				}
			}
			slotindex++;
		}
		return null;
	}
}
