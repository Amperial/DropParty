package me.ampayne2.DropParty;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class DropPartyListener implements Listener {

	@EventHandler
	public void onChestHit(PlayerInteractEvent event) {
		if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		String playerName = event.getPlayer().getDisplayName();
		Player player = event.getPlayer();
		if (!CommandSetChest.isSetting(playerName)
				&& !CommandRemoveChest.isRemoving(playerName)) {
			return;
		}
		if (CommandSetChest.isSetting(playerName)
				&& CommandRemoveChest.isRemoving(playerName)) {
			return;
		}

		Block clickedBlock = event.getClickedBlock();

		try {
			if (clickedBlock.getType() != Material.CHEST) {
				return;
			}
		} catch (NullPointerException ex) {
			return;
		}

		if (CommandSetChest.isSetting(playerName)) {
			CommandSetChest.saveChest(player, playerName, clickedBlock.getX(),
					clickedBlock.getY(), clickedBlock.getZ());
		}

		if (CommandRemoveChest.isRemoving(playerName)) {
			CommandRemoveChest.deleteChest(player, playerName,
					clickedBlock.getX(), clickedBlock.getY(),
					clickedBlock.getZ());
		}
		event.setCancelled(true);
	}

}
