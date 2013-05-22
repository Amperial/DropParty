package me.ampayne2.DropParty.listeners;

import me.ampayne2.DropParty.command.commands.set.CommandSetFireworkpoint;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DPGlassListener implements Listener{
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Block placedBlock = event.getBlock();
		Player player = event.getPlayer();
		try {
			if (placedBlock.getType() != Material.GLASS) {
				return;
			}
		} catch (NullPointerException ex) {
			return;
		}
		if (!CommandSetFireworkpoint.isSetting(player.getName())) {
			return;
		}
		String dpid = CommandSetFireworkpoint.getSetting(player.getName());
		CommandSetFireworkpoint.saveFireworkPoint(player, player.getName(), dpid, placedBlock.getWorld().getName(), placedBlock.getX(), placedBlock.getY(), placedBlock.getZ());
		event.setCancelled(true);
	}

}