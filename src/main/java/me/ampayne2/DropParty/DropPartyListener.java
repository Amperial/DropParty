package me.ampayne2.DropParty;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class DropPartyListener implements Listener {

	public static DropParty plugin;

	@EventHandler
	public void onChestHit(PlayerInteractEvent event) {
		if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK)
			return;

		if (!plugin.isSelecting(event.getPlayer().getDisplayName()))
			return;

		Block clickedBlock = event.getClickedBlock();

		try {
			if (clickedBlock.getType() != Material.CHEST)
				return;
		} catch (NullPointerException ex) {
			return;
		}

		Player player = event.getPlayer();
		// Code that saves the chest to mysql goes here
		event.setCancelled(true);
	}

}
