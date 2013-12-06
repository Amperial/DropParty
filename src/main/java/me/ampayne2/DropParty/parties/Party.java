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
package me.ampayne2.dropparty.parties;

import me.ampayne2.dropparty.DropParty;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Party {
    private final DropParty dropParty;
    private final String partyName;
    private long maxLength;
    private long itemDelay;
    private int maxStackSize;
    private int fireworkAmount;
    private long fireworkDelay;
    private Set<DPChest> chests = new HashSet<>();
    private Set<DPItemPoint> itemPoints = new HashSet<>();
    private Set<DPFireworkPoint> fireworkPoints = new HashSet<>();
    private Location teleport;
    private boolean isRunning = false;
    private int taskId;

    public Party(DropParty dropParty, String partyName, Location teleport) {
        this.dropParty = dropParty;
        this.partyName = partyName;
        this.teleport = teleport;
        FileConfiguration config = dropParty.getConfig();
        maxLength = config.getLong("defaultsettings.maxlength", 6000);
        itemDelay = config.getLong("defaultsettings.itemdelay", 5);
        maxStackSize = config.getInt("defaultsettings.maxstacksize", 8);
        fireworkAmount = config.getInt("defaultsettings.fireworkamount", 8);
        fireworkDelay = config.getLong("defaultsettings.fireworkdelay", 2);
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            taskId = dropParty.getServer().getScheduler().scheduleSyncRepeatingTask(dropParty, new Runnable() {
                @Override
                public void run() {

                }
            }, 0, itemDelay);
        }
    }

    public void stop() {
        if (isRunning) {
            isRunning = false;
            dropParty.getServer().getScheduler().cancelTask(taskId);
        }
    }

    public void teleport(Player player) {
        player.teleport(teleport);
        dropParty.getMessage().sendMessage(player, "party.teleport", partyName);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void addChest(DPChest chest) {
        chests.add(chest);
    }

    public void addItemPoint(DPItemPoint itemPoint) {
        itemPoints.add(itemPoint);
    }

    public void addFireworkPoint(DPFireworkPoint fireworkPoint) {
        fireworkPoints.add(fireworkPoint);
    }



    public Set<DPChest> getChests() {
        return chests;
    }

    public Set<DPItemPoint> getItemPoints() {
        return itemPoints;
    }

    public Set<DPFireworkPoint> getFireworkPoints() {
        return fireworkPoints;
    }

    public Location getTeleport() {
        return teleport;
    }

    public String getName() {
        return partyName;
    }

    public long getMaxLength() {
        return maxLength;
    }

    public long getItemDelay() {
        return itemDelay;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public int getFireworkAmount() {
        return fireworkAmount;
    }

    public long getFireworkDelay() {
        return fireworkDelay;
    }

    public void setMaxLength(long maxLength) {
        this.maxLength = maxLength;
        // TODO: Set max length in config.
    }

    public void setItemDelay(long itemDelay) {
        this.itemDelay = itemDelay;
        // TODO: Set item delay in config.
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        // TODO: Set max stack size in config.
    }

    public void setFireworkAmount(int fireworkAmount) {
        this.fireworkAmount = fireworkAmount;
        // TODO: Set firework amount in config.
    }

    public void setFireworkDelay(long fireworkDelay) {
        this.fireworkDelay = fireworkDelay;
        // TODO: Set firework delay in config.
    }
}
