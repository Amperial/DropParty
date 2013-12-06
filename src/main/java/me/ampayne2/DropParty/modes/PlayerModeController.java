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
package me.ampayne2.dropparty.modes;

import me.ampayne2.dropparty.DropParty;
import me.ampayne2.dropparty.parties.Party;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerModeController implements Listener {
    private final DropParty dropParty;
    private Map<String, DPMode> playerModes = new HashMap<>();
    private Map<String, Party> playerModeParties = new HashMap<>();

    public PlayerModeController(DropParty dropParty) {
        this.dropParty = dropParty;
        dropParty.getServer().getPluginManager().registerEvents(this, dropParty);
    }

    public DPMode getPlayerMode(String playerName) {
        return playerModes.get(playerName);
    }

    public Party getPlayerModeParty(String playerName) {
        return playerModeParties.get(playerName);
    }

    public void setPlayerMode(Player player, DPMode dpMode, Party party) {
        String playerName = player.getName();
        if (playerModes.containsKey(playerName)) {
            DPMode currentMode = playerModes.get(playerName);
            Party currentParty = playerModeParties.get(playerName);
            dropParty.getMessage().sendMessage(player, "mode.off", currentMode.getName(), currentParty.getName());
            if (currentMode.equals(dpMode) && currentParty.equals(party)) {
                return;
            }
        }
        playerModes.put(playerName, dpMode);
        playerModeParties.put(playerName, party);
        dropParty.getMessage().sendMessage(player, "mode.on", dpMode.getName(), party.getName());
    }

    public void clearModes() {
        playerModes.clear();
        playerModeParties.clear();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        playerModes.remove(playerName);
        playerModeParties.remove(playerName);
    }
}
