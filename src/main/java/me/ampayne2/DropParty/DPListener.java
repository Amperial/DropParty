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

import me.ampayne2.dropparty.modes.PlayerModeController;
import me.ampayne2.dropparty.parties.ChestParty;
import me.ampayne2.dropparty.parties.Party;
import me.ampayne2.dropparty.parties.PartyType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            String playerName = player.getName();
            PlayerModeController modeController = dropParty.getPlayerModeController();
            if (modeController.hasMode(playerName)) {
                Party party = modeController.getPlayerModeParty(playerName);
                Block block = event.getClickedBlock();
                switch (modeController.getPlayerMode(playerName)) {
                    case SETTING_CHESTS:
                        if (block.getType() == Material.CHEST) {
                            Chest chest = (Chest) block.getState();
                            ChestParty chestParty = (ChestParty) party;
                            if (chestParty.hasChest(chest)) {
                                dropParty.getMessage().sendMessage(player, "error.chest.alreadyexists");
                            } else {
                                chestParty.addChest(new DPChest(dropParty, party, chest));
                                dropParty.getMessage().sendMessage(player, "set.chest", party.getName());
                            }
                        } else {
                            return;
                        }
                        break;
                    case SETTING_ITEM_POINTS:
                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            block = block.getRelative(event.getBlockFace());
                        }
                        if (party.hasItemPoint(block.getLocation())) {
                            dropParty.getMessage().sendMessage(player, "error.itempoint.alreadyexists");
                        } else {
                            party.addItemPoint(new DPItemPoint(dropParty, party, block.getLocation()));
                            dropParty.getMessage().sendMessage(player, "set.itempoint", party.getName());
                        }
                        break;
                    case SETTING_FIREWORK_POINTS:
                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            block = block.getRelative(event.getBlockFace());
                        }
                        if (party.hasFireworkPoint(block.getLocation())) {
                            dropParty.getMessage().sendMessage(player, "error.fireworkpoint.alreadyexists");
                        } else {
                            party.addFireworkPoint(new DPFireworkPoint(dropParty, party, block.getLocation()));
                            dropParty.getMessage().sendMessage(player, "set.fireworkpoint", party.getName());
                        }
                        break;
                    case REMOVING_CHESTS:
                        if (block.getType() == Material.CHEST) {
                            Chest chest = (Chest) block.getState();
                            ChestParty chestParty = (ChestParty) party;
                            if (chestParty.hasChest(chest)) {
                                chestParty.removeChest(chest);
                                dropParty.getMessage().sendMessage(player, "remove.chest", party.getName());
                            } else {
                                dropParty.getMessage().sendMessage(player, "error.chest.doesntexist");
                            }
                        } else {
                            return;
                        }
                        break;
                    case REMOVING_ITEM_POINTS:
                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            block = block.getRelative(event.getBlockFace());
                        }
                        if (party.hasItemPoint(block.getLocation())) {
                            party.removeItemPoint(block.getLocation());
                            dropParty.getMessage().sendMessage(player, "remove.itempoint", party.getName());
                        } else {
                            dropParty.getMessage().sendMessage(player, "error.itempoint.doesntexist", party.getName());
                        }
                        break;
                    case REMOVING_FIREWORK_POINTS:
                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            block = block.getRelative(event.getBlockFace());
                        }
                        if (party.hasFireworkPoint(block.getLocation())) {
                            party.removeFireworkPoint(block.getLocation());
                            dropParty.getMessage().sendMessage(player, "remove.fireworkpoint", party.getName());
                        } else {
                            dropParty.getMessage().sendMessage(player, "error.fireworkpoint.doesntexist");
                        }
                        break;
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (event.getBlock().getType() == Material.CHEST) {
            Chest chest = (Chest) event.getBlock().getState();
            for (Party party : dropParty.getPartyManager().getParties(PartyType.CHEST_PARTY).values()) {
                if (((ChestParty) party).hasChest(chest)) {
                    event.setCancelled(true);
                    dropParty.getMessage().sendMessage(player, "error.chest.cantbreak");
                    return;
                }
            }
        }
    }
}
