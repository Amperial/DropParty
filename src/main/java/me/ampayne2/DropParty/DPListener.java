/*
 * This file is part of DropParty.
 *
 * Copyright (c) 2013-2013 <http://dev.bukkit.org/server-mods/dropparty//>
 *
 * DropParty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DropParty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DropParty.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.dropparty;

import me.ampayne2.dropparty.modes.PlayerMode;
import me.ampayne2.dropparty.modes.PlayerModeController;
import me.ampayne2.dropparty.parties.ChestParty;
import me.ampayne2.dropparty.parties.Party;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * The drop party listener.
 */
public class DPListener implements Listener {
    private final DropParty dropParty;

    /**
     * Creates a new drop party listener.
     *
     * @param dropParty The DropParty instance.
     */
    public DPListener(DropParty dropParty) {
        this.dropParty = dropParty;
        dropParty.getServer().getPluginManager().registerEvents(this, dropParty);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        PlayerModeController modeController = dropParty.getPlayerModeController();
        if (modeController.hasMode(playerName)) {
            PlayerMode playerMode = modeController.getPlayerMode(playerName);
            Party party = modeController.getPlayerModeParty(playerName);
            Block block = event.getBlock();
            Material material = block.getType();
            if (material == Material.GLASS) {
                Location location = block.getLocation();
                if (playerMode == PlayerMode.SETTING_ITEM_POINTS) {
                    if (party.hasItemPoint(location)) {
                        dropParty.getMessage().sendMessage(player, "error.itempoint.alreadyexists");
                    } else {
                        party.addItemPoint(new DPItemPoint(dropParty, party, block.getLocation()));
                        dropParty.getMessage().sendMessage(player, "set.itempoint", party.getName());
                    }
                } else if (playerMode == PlayerMode.REMOVING_ITEM_POINTS) {
                    if (party.hasItemPoint(location)) {
                        party.removeItemPoint(location);
                        dropParty.getMessage().sendMessage(player, "remove.itempoint", party.getName());
                    } else {
                        dropParty.getMessage().sendMessage(player, "error.itempoint.doesntexist", party.getName());
                    }
                }
            } else if (material == Material.GLOWSTONE) {
                Location location = block.getLocation();
                if (playerMode == PlayerMode.SETTING_FIREWORK_POINTS) {
                    if (party.hasFireworkPoint(location)) {
                        dropParty.getMessage().sendMessage(player, "error.fireworkpoint.alreadyexists");
                    } else {
                        party.addFireworkPoint(new DPFireworkPoint(dropParty, party, block.getLocation()));
                        dropParty.getMessage().sendMessage(player, "set.fireworkpoint", party.getName());
                    }
                } else if (playerMode == PlayerMode.REMOVING_FIREWORK_POINTS) {
                    if (party.hasFireworkPoint(location)) {
                        party.removeFireworkPoint(location);
                        dropParty.getMessage().sendMessage(player, "remove.fireworkpoint", party.getName());
                    } else {
                        dropParty.getMessage().sendMessage(player, "error.fireworkpoint.doesntexist");
                    }
                }
            } else {
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        PlayerModeController modeController = dropParty.getPlayerModeController();
        if (event.getBlock().getType() == Material.CHEST && modeController.hasMode(playerName)) {
            if (modeController.getPlayerMode(playerName) == PlayerMode.SETTING_CHESTS) {
                ChestParty party = (ChestParty) dropParty.getPlayerModeController().getPlayerModeParty(playerName);
                Chest chest = (Chest) event.getBlock().getState();
                if (party.hasChest(chest)) {
                    dropParty.getMessage().sendMessage(player, "error.chest.alreadyexists");
                } else {
                    party.addChest(new DPChest(dropParty, party, (Chest) event.getBlock().getState()));
                    dropParty.getMessage().sendMessage(player, "set.chest", party.getName());
                }
                event.setCancelled(true);
            } else if (modeController.getPlayerMode(playerName) == PlayerMode.REMOVING_CHESTS) {
                ChestParty party = (ChestParty) dropParty.getPlayerModeController().getPlayerModeParty(playerName);
                Chest chest = (Chest) event.getBlock().getState();
                if (party.hasChest(chest)) {
                    party.removeChest((Chest) event.getBlock().getState());
                    dropParty.getMessage().sendMessage(player, "remove.chest", party.getName());
                } else {
                    dropParty.getMessage().sendMessage(player, "error.chest.doesntexist");
                }
                event.setCancelled(true);
            }
        }
    }
}
