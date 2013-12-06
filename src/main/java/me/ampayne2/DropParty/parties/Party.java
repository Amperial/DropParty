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

import java.util.HashSet;
import java.util.Set;

public class Party {
    private final DropParty dropParty;
    private final String partyName;
    private long maxLength = 6000;
    private int itemDelay = 5;
    private int maxStackSize = 8;
    private int fireworkAmount = 8;
    private long fireworkDelay = 2;
    private Set<DPChest> chests = new HashSet<>();
    private Set<DPItemPoint> itemPoints = new HashSet<>();
    private Set<DPFireworkPoint> fireworkPoints = new HashSet<>();

    public Party(DropParty dropParty, String partyName) {
        this.dropParty = dropParty;
        this.partyName = partyName;
    }

    public void start() {
    }

    public void stop() {
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

    public String getName() {
        return partyName;
    }

    public long getMaxLength() {
        return maxLength;
    }

    public int getItemDelay() {
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

    public void setItemDelay(int itemDelay) {
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
