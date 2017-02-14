/*
 * This file is part of DropParty.
 *
 * Copyright (c) 2013-2014 <http://dev.bukkit.org/server-mods/dropparty//>
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
package ninja.amp.dropparty;

import ninja.amp.dropparty.message.DPMessage;
import ninja.amp.dropparty.modes.PlayerModeController;
import ninja.amp.dropparty.parties.Party;
import org.bukkit.Location;
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
                            if (party.hasChest(chest)) {
                                dropParty.getMessenger().sendMessage(player, DPMessage.CHEST_ALREADYEXISTS);
                            } else {
                                party.addChest(new DPChest(dropParty, party, chest));
                                dropParty.getMessenger().sendMessage(player, DPMessage.SET_CHEST, party.getName());
                            }
                        } else {
                            return;
                        }
                        break;
                    case SETTING_ITEM_POINTS:
                        block = block.getRelative(event.getBlockFace());
                        if (party.hasItemPoint(block.getLocation())) {
                            dropParty.getMessenger().sendMessage(player, DPMessage.ITEMPOINT_ALREADYEXISTS);
                        } else {
                            party.addItemPoint(new DPItemPoint(dropParty, party, block.getLocation()));
                            dropParty.getMessenger().sendMessage(player, DPMessage.SET_ITEMPOINT, party.getName());
                        }
                        break;
                    case SETTING_FIREWORK_POINTS:
                        block = block.getRelative(event.getBlockFace());
                        Location fireworkPointLocation = block.getLocation().clone().add(0.5, 0, 0.5);
                        if (party.hasFireworkPoint(fireworkPointLocation)) {
                            dropParty.getMessenger().sendMessage(player, DPMessage.FIREWORKPOINT_ALREADYEXISTS);
                        } else {
                            party.addFireworkPoint(new DPFireworkPoint(dropParty, party, fireworkPointLocation));
                            dropParty.getMessenger().sendMessage(player, DPMessage.SET_FIREWORKPOINT, party.getName());
                        }
                        break;
                    case REMOVING_CHESTS:
                        if (block.getType() == Material.CHEST) {
                            Chest chest = (Chest) block.getState();
                            if (party.hasChest(chest)) {
                                party.removeChest(chest);
                                dropParty.getMessenger().sendMessage(player, DPMessage.REMOVE_CHEST, party.getName());
                            } else {
                                dropParty.getMessenger().sendMessage(player, DPMessage.CHEST_DOESNTEXIST);
                            }
                        } else {
                            return;
                        }
                        break;
                    case REMOVING_ITEM_POINTS:
                        block = block.getRelative(event.getBlockFace());
                        if (party.hasItemPoint(block.getLocation())) {
                            party.removeItemPoint(block.getLocation());
                            dropParty.getMessenger().sendMessage(player, DPMessage.REMOVE_ITEMPOINT, party.getName());
                        } else {
                            dropParty.getMessenger().sendMessage(player, DPMessage.ITEMPOINT_DOESNTEXIST, party.getName());
                        }
                        break;
                    case REMOVING_FIREWORK_POINTS:
                        block = block.getRelative(event.getBlockFace());
                        fireworkPointLocation = block.getLocation().clone().add(0.5, 0, 0.5);
                        if (party.hasFireworkPoint(fireworkPointLocation)) {
                            party.removeFireworkPoint(fireworkPointLocation);
                            dropParty.getMessenger().sendMessage(player, DPMessage.REMOVE_FIREWORKPOINT, party.getName());
                        } else {
                            dropParty.getMessenger().sendMessage(player, DPMessage.FIREWORKPOINT_DOESNTEXIST);
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
            for (Party party : dropParty.getPartyManager().getParties()) {
                if (party.hasChest(chest)) {
                    event.setCancelled(true);
                    dropParty.getMessenger().sendMessage(player, DPMessage.CHEST_CANTBREAK);
                    return;
                }
            }
        }
    }

}
