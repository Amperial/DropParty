package me.ampayne2.DropParty;

import me.ampayne2.DropParty.command.commands.CommandRemoveItempoint;
import me.ampayne2.DropParty.command.commands.CommandSetItempoint;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DropPartyGlowstoneListener implements Listener {
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		String playerName = event.getPlayer().getDisplayName();
		Player player = event.getPlayer();
		if (!CommandSetItempoint.isSetting(playerName)
				&& !CommandRemoveItempoint.isRemoving(playerName)) {
			return;
		}
		if (CommandSetItempoint.isSetting(playerName)
				&& CommandRemoveItempoint.isRemoving(playerName)) {
			return;
		}

		Block placedBlock = event.getBlock();

		try {
			if (placedBlock.getType() != Material.GLOWSTONE) {
				return;
			}
		} catch (NullPointerException ex) {
			return;
		}

		if (CommandSetItempoint.isSetting(playerName)) {
			CommandSetItempoint.saveItempoint(player, playerName, placedBlock.getX(),
					placedBlock.getY(), placedBlock.getZ());
		}

		if (CommandRemoveItempoint.isRemoving(playerName)) {
			CommandRemoveItempoint.deleteItempoint(player, playerName,
					placedBlock.getX(), placedBlock.getY(),
					placedBlock.getZ());
		}
		event.setCancelled(true);
	}

}
