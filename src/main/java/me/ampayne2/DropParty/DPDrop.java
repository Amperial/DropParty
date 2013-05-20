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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class DPDrop extends BukkitRunnable {

	CommandSender sender = null;
	String party = null;
	Long delay = null;
	Long length = null;
	Long currentLength = null;
	int stack = 0;

	public DPDrop(CommandSender ssender, String dpid, Long itemdelay, Long maxlength, Long clength, int maxstack) {
		sender = ssender;
		party = dpid;
		delay = itemdelay;
		length = maxlength;
		currentLength = clength + delay;
		stack = maxstack;
	}

	@Override
	public void run() {
		dropItems(sender, party, delay, length, currentLength, stack);
	}

	public static void dropItems(CommandSender sender, String dpid, Long delay, Long length, Long clength, int stack) {
		Chest[] chests = DPChest.getChests(sender, dpid);
		Location[] itemPoints = DPItemPoint.getItemPoints(sender, dpid);
		if (chests == null || itemPoints == null) {
			if (chests == null) {
				DPMessageController.sendMessage((Player) sender, DPMessageController.getMessage("dpnochestsfound"), dpid);
			}
			if (itemPoints == null) {
				DPMessageController.sendMessage((Player) sender, DPMessageController.getMessage("dpnoitempointsfound"), dpid);
			}
			DPPartyController.stop((Player) sender, dpid);
			return;
		}
		ItemStack itemStack = DPChest.getNextItemStack(sender, chests, dpid, stack);
		DPItemPoint.dropItemStack(itemStack, itemPoints);
		if (DPPartyController.isRunning(dpid) && clength < length) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DropParty.getInstance(), new DPDrop(sender, dpid, delay, length, clength, stack), delay);
		} else if (clength >= length) {
			DPPartyController.stop((Player) sender, dpid);
		}
	}

}