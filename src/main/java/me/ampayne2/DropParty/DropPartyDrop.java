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
