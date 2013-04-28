package me.ampayne2.DropParty;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class DropPartyListener implements Listener {

	CommandSetChest setChest = new CommandSetChest();

	@EventHandler
	public void onChestHit(PlayerInteractEvent event) {
		if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK)
			return;

		if (!setChest.isSelecting(event.getPlayer().getDisplayName()))
			return;

		Block clickedBlock = event.getClickedBlock();

		try {
			if (clickedBlock.getType() != Material.CHEST)
				return;
		} catch (NullPointerException ex) {
			return;
		}
		setChest.saveChest(clickedBlock.getX(), clickedBlock.getY(),
				clickedBlock.getZ());
		event.setCancelled(true);
	}

}
