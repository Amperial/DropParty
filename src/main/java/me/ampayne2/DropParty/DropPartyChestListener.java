package me.ampayne2.DropParty;

import me.ampayne2.DropParty.command.commands.CommandRemoveChest;
import me.ampayne2.DropParty.command.commands.CommandSetChest;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class DropPartyChestListener implements Listener {

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

	@EventHandler
	public void onChestBreak(BlockBreakEvent event) {
		Block clickedBlock = event.getBlock();
		Player player = event.getPlayer();
		String playerName = event.getPlayer().getDisplayName();

		try {
			if (clickedBlock.getType() != Material.CHEST) {
				return;
			}
		} catch (NullPointerException ex) {
			return;
		}

		CommandRemoveChest.deleteChest(player, playerName, clickedBlock.getX(),
				clickedBlock.getY(), clickedBlock.getZ());
	}

}
