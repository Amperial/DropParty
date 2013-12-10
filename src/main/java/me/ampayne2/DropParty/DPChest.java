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

import me.ampayne2.dropparty.parties.Party;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DPChest {
    private final DropParty dropParty;
    private final Party party;
    private final Chest chest;
    private final Inventory inventory;
    private final boolean isDoubleChest;

    public DPChest(DropParty dropParty, Party party, Chest chest) {
        this.dropParty = dropParty;
        this.party = party;
        this.chest = chest;
        this.inventory = chest.getBlockInventory();
        this.isDoubleChest = chest.getBlockInventory() instanceof DoubleChestInventory;
    }

    public Party getParty() {
        return party;
    }

    public Chest getChest() {
        return chest;
    }

    public boolean isDoubleChest() {
        return isDoubleChest;
    }

    public ItemStack getNextItemStack() {
        for (int i = 0; i < (isDoubleChest ? 54 : 27); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) {
                if (itemStack.getAmount() <= party.getMaxStackSize()) {
                    inventory.setItem(i, null);
                    return itemStack;
                } else {
                    itemStack.setAmount(itemStack.getAmount() - party.getMaxStackSize());
                    ItemStack newItemStack = itemStack.clone();
                    newItemStack.setAmount(party.getMaxStackSize());
                    return newItemStack;
                }
            }
        }
        return null;
    }

    public String toConfig() {
        return new StringBuilder()
                .append(party.getName()).append(":")
                .append(DPUtils.locationToString(chest.getLocation()))
                .toString();
    }

    public static DPChest fromConfig(DropParty dropParty, String string) {
        try {
            String[] parts = string.split(":", 2);
            if (parts.length == 2) {
                String partyName = parts[0];
                if (dropParty.getPartyManager().hasParty(partyName)) {
                    Block block = DPUtils.stringToLocation(parts[1]).getBlock();
                    if (block.getType() == Material.CHEST) {
                        Party party = dropParty.getPartyManager().getParty(partyName);
                        return new DPChest(dropParty, party, (Chest) block.getState());
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
