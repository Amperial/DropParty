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

import ninja.amp.dropparty.parties.Party;
import ninja.amp.dropparty.parties.PartySetting;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A drop party chest.
 */
public class DPChest {

    private final DropParty dropParty;
    private final Party party;
    private final Chest chest;
    private final Inventory inventory;
    private final boolean isDoubleChest;
    private int currentSlot = 0;
    private int nextAmount = 0;

    /**
     * Creates a new DPChest.
     *
     * @param dropParty The DropParty instance.
     * @param party     The party of the DPChest.
     * @param chest     The chest.
     */
    public DPChest(DropParty dropParty, Party party, Chest chest) {
        this.dropParty = dropParty;
        this.party = party;
        this.chest = chest;
        this.inventory = chest.getInventory();
        this.isDoubleChest = inventory.getHolder() instanceof DoubleChest;
    }

    /**
     * Gets the DPChest's party.
     *
     * @return The DPChest's party.
     */
    public Party getParty() {
        return party;
    }

    /**
     * Gets the DPChest's chest.
     *
     * @return The DPChest's chest.
     */
    public Chest getChest() {
        return chest;
    }

    /**
     * Checks if the chest is a double chest.
     *
     * @return True if the chest is a double chest, else false.
     */
    public boolean isDoubleChest() {
        return isDoubleChest;
    }

    /**
     * Gets the next item stack in the chest.
     *
     * @return The next item stack.
     */
    public ItemStack getNextItemStack() {
        if (isDoubleChest) {
            return getNextItemStack(inventory.getHolder().getInventory());
        } else {
            return getNextItemStack(inventory);
        }
    }

    /**
     * Gets the next item stack in the inventory.
     *
     * @param inventory The inventory.
     * @return The next item stack.
     */
    public ItemStack getNextItemStack(Inventory inventory) {
        if (party.get(PartySetting.EMPTY_CHEST, Boolean.class)) {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);
                if (itemStack != null) {
                    int maxStackSize = party.get(PartySetting.MAX_STACK_SIZE, Integer.class);
                    if (itemStack.getAmount() <= maxStackSize) {
                        inventory.setItem(i, null);
                        return itemStack;
                    } else {
                        itemStack.setAmount(itemStack.getAmount() - maxStackSize);
                        ItemStack newItemStack = itemStack.clone();
                        newItemStack.setAmount(maxStackSize);
                        return newItemStack;
                    }
                }
            }
        } else {
            if (currentSlot == inventory.getSize()) {
                return null;
            }
            for (int i = currentSlot; i < inventory.getSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);
                if (itemStack == null) {
                    currentSlot++;
                } else {
                    if (nextAmount == 0) {
                        nextAmount = itemStack.getAmount();
                    }
                    int maxStackSize = party.get(PartySetting.MAX_STACK_SIZE, Integer.class);
                    if (nextAmount <= maxStackSize) {
                        ItemStack newItemStack = itemStack.clone();
                        newItemStack.setAmount(nextAmount);
                        currentSlot++;
                        nextAmount = 0;
                        return newItemStack;
                    } else {
                        nextAmount = nextAmount - maxStackSize;
                        ItemStack newItemStack = itemStack.clone();
                        newItemStack.setAmount(maxStackSize);
                        return newItemStack;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Resets the current slot of the chest.
     */
    public void resetCurrentSlot() {
        currentSlot = 0;
        nextAmount = 0;
    }

    /**
     * Converts the chest into a string for storage in a config.
     *
     * @return The string representation of the chest.
     */
    public String toConfig() {
        return party.getName() + ";" + DPUtils.locationToString(chest.getLocation());
    }

    /**
     * Converts a string representation of a chest into a chest.
     *
     * @param dropParty The DropParty instance.
     * @param party     The party of the chest.
     * @param string    The string representation of the chest.
     * @return The chest.
     */
    public static DPChest fromConfig(DropParty dropParty, Party party, String string) {
        try {
            String[] parts = string.split(";");
            if (parts.length == 2) {
                String partyName = parts[0];
                if (partyName.equals(party.getName())) {
                    Location location = DPUtils.stringToLocation(parts[1]);
                    Block block = location.getWorld().getBlockAt(location);
                    if (block.getType() == Material.CHEST) {
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
