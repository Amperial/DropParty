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
package ninja.amp.dropparty.modes;

import ninja.amp.dropparty.DropParty;
import ninja.amp.dropparty.message.DPMessage;
import ninja.amp.dropparty.parties.Party;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Controls player's drop party selection and removal modes.
 */
public class PlayerModeController implements Listener {

    private final DropParty dropParty;
    private final Map<String, PlayerMode> playerModes = new HashMap<>();
    private final Map<String, Party> playerModeParties = new HashMap<>();

    /**
     * Creates a new player mode controller.
     *
     * @param dropParty The DropParty instance.
     */
    public PlayerModeController(DropParty dropParty) {
        this.dropParty = dropParty;
        dropParty.getServer().getPluginManager().registerEvents(this, dropParty);
    }

    /**
     * Checks if a player has a mode.
     *
     * @param playerName The name of the player.
     * @return True if the player has a mode, else false.
     */
    public boolean hasMode(String playerName) {
        return playerModes.containsKey(playerName);
    }

    /**
     * Gets the player's mode.
     *
     * @param playerName The name of the player.
     * @return The player's mode.
     */
    public PlayerMode getPlayerMode(String playerName) {
        return playerModes.get(playerName);
    }

    /**
     * Gets the player's mode party.
     *
     * @param playerName The name of the player.
     * @return The player's mode party.
     */
    public Party getPlayerModeParty(String playerName) {
        return playerModeParties.get(playerName);
    }

    /**
     * Sets the player's mode.
     *
     * @param player     The player.
     * @param playerMode The mode to set the player to.
     * @param party      The party of the player's mode.
     */
    public void setPlayerMode(Player player, PlayerMode playerMode, Party party) {
        String playerName = player.getName();
        if (playerModes.containsKey(playerName)) {
            PlayerMode currentMode = playerModes.get(playerName);
            Party currentParty = playerModeParties.get(playerName);
            dropParty.getMessenger().sendMessage(player, DPMessage.MODE_DISABLE, currentMode.getName(), currentParty.getName());
            if (currentMode.equals(playerMode) && currentParty.equals(party)) {
                playerModes.remove(playerName);
                playerModeParties.remove(playerName);
                return;
            }
        }
        playerModes.put(playerName, playerMode);
        playerModeParties.put(playerName, party);
        dropParty.getMessenger().sendMessage(player, DPMessage.MODE_ENABLE, playerMode.getName(), party.getName());
    }

    /**
     * Clears all player's modes.
     */
    public void clearModes() {
        playerModes.clear();
        playerModeParties.clear();
    }

    /**
     * Clears all player's modes of a certain party.
     *
     * @param party The party to clear.
     */
    public void clearModes(Party party) {
        for (String playerName : new HashSet<>(playerModeParties.keySet())) {
            if (playerModeParties.get(playerName).equals(party)) {
                playerModes.remove(playerName);
                playerModeParties.remove(playerName);
            }
        }
    }

    /**
     * Clears a player's mode on quit.
     *
     * @param event The PlayerQuitEvent.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        playerModes.remove(playerName);
        playerModeParties.remove(playerName);
    }

}
