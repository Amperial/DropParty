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

import me.ampayne2.dropparty.DPFireworkPoint;
import me.ampayne2.dropparty.DPItemPoint;
import me.ampayne2.dropparty.DPUtils;
import me.ampayne2.dropparty.DropParty;
import me.ampayne2.dropparty.config.ConfigType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Controls and contains all the information of a drop party.
 */
public abstract class Party {
    protected final DropParty dropParty;
    protected final String partyName;
    protected final PartyType type;
    protected long maxLength;
    protected long currentLength;
    protected long itemDelay;
    protected int maxStackSize;
    protected int fireworkAmount;
    protected long fireworkDelay;
    protected boolean isRunning = false;
    protected int taskId;
    protected int periodicTaskId;
    protected boolean startPeriodically;
    protected long startPeriod;
    protected Location teleport;
    protected List<DPItemPoint> itemPoints = new ArrayList<>();
    protected List<DPFireworkPoint> fireworkPoints = new ArrayList<>();

    /**
     * Creates a Party from default settings.
     *
     * @param dropParty The DropParty instance.
     * @param partyName The name of the party.
     * @param type      The type of the party.
     * @param teleport  The teleport location of the party.
     */
    public Party(DropParty dropParty, String partyName, PartyType type, Location teleport) {
        this.dropParty = dropParty;
        this.partyName = partyName;
        this.type = type;
        this.teleport = teleport;

        FileConfiguration config = dropParty.getConfig();
        maxLength = config.getLong("defaultsettings.maxlength", 6000);
        itemDelay = config.getLong("defaultsettings.itemdelay", 5);
        maxStackSize = config.getInt("defaultsettings.maxstacksize", 8);
        fireworkAmount = config.getInt("defaultsettings.fireworkamount", 8);
        fireworkDelay = config.getLong("defaultsettings.fireworkdelay", 2);
        startPeriodically = config.getBoolean("defaultsettings.startperiodically", false);
        startPeriod = config.getLong("defaultsettings.startperiod", 144000);
    }

    /**
     * Loads a Party from a ConfigurationSection.
     *
     * @param dropParty The DropParty instance.
     * @param section   The ConfigurationSection.
     */
    public Party(DropParty dropParty, ConfigurationSection section) {
        this.dropParty = dropParty;
        this.partyName = section.getString("name");
        this.type = PartyType.valueOf(section.getString("type"));
        maxLength = section.getLong("maxlength");
        itemDelay = section.getLong("itemdelay");
        maxStackSize = section.getInt("maxStackSize");
        fireworkAmount = section.getInt("fireworkamount");
        fireworkDelay = section.getLong("fireworkdelay");
        startPeriodically = section.getBoolean("startperiodically");
        startPeriod = section.getLong("startperiod");
        teleport = DPUtils.stringToLocation(section.getString("teleport"));

        List<String> dpItemPoints = section.getStringList("itempoints");
        for (String itemPoint : dpItemPoints) {
            itemPoints.add(DPItemPoint.fromConfig(dropParty, this, itemPoint));
        }

        List<String> dpFireworkPoints = section.getStringList("fireworkpoints");
        for (String fireworkPoint : dpFireworkPoints) {
            fireworkPoints.add(DPFireworkPoint.fromConfig(dropParty, this, fireworkPoint));
        }
    }

    /**
     * Starts the periodic start timer.
     */
    public void startPeriodicTimer() {
        if (startPeriodically) {
            periodicTaskId = dropParty.getServer().getScheduler().scheduleSyncRepeatingTask(dropParty, new Runnable() {
                @Override
                public void run() {
                    start();
                }
            }, startPeriod, startPeriod);
        }
    }

    /**
     * Stops the periodic start timer.
     */
    public void stopPeriodicTimer() {
        dropParty.getServer().getScheduler().cancelTask(periodicTaskId);
    }

    /**
     * Resets the periodic start timer.
     */
    public void resetPeriodicTimer() {
        stopPeriodicTimer();
        startPeriodicTimer();
    }

    /**
     * Gets the name of the party.
     *
     * @return The name of the party.
     */
    public String getName() {
        return partyName;
    }

    /**
     * Gets the type of the party.
     *
     * @return The type of the party.
     */
    public PartyType getType() {
        return type;
    }

    /**
     * Checks if the party is running.
     *
     * @return True if the party is running, else false.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Teleports a player to the party.
     *
     * @param player The player to teleport to the party.
     */
    public void teleport(Player player) {
        player.teleport(teleport);
        dropParty.getMessage().sendMessage(player, "party.teleport", partyName);
    }

    /**
     * Starts the party.
     */
    public void start() {
        if (!isRunning) {
            isRunning = true;
            taskId = dropParty.getServer().getScheduler().scheduleSyncRepeatingTask(dropParty, new Runnable() {
                @Override
                public void run() {
                    currentLength += itemDelay;
                    if (currentLength > maxLength || !dropNext()) {
                        stop();
                        currentLength = 0;
                    }
                }
            }, 0, itemDelay);
            dropParty.getMessage().sendMessage(dropParty.getServer(), "broadcast.start", partyName, partyName);
        }
    }

    /**
     * Stops the party.
     */
    public void stop() {
        if (isRunning) {
            isRunning = false;
            dropParty.getServer().getScheduler().cancelTask(taskId);
            dropParty.getMessage().sendMessage(dropParty.getServer(), "broadcast.stop", partyName);
        }
    }

    /**
     * Drops the next item stack in the party.
     *
     * @return True if the next item stack was dropped successfully.
     */
    public abstract boolean dropNext();

    /**
     * Drops an item stack at a random item point.
     *
     * @param itemStack The item stack to drop.
     * @return True if the item stack is not null or air, and has a place to drop.
     */
    public boolean dropItemStack(ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR && itemPoints.size() > 0) {
            Random generator = new Random();
            DPItemPoint itemPoint = itemPoints.get(generator.nextInt(itemPoints.size()));
            System.out.println("dropping " + itemStack + " at " + itemPoint);
            itemPoint.getLocation().getWorld().dropItemNaturally(itemPoint.getLocation(), itemStack);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the party has an item point.
     *
     * @param location The location of the item point.
     * @return True if the party has the item point, else false.
     */
    public boolean hasItemPoint(Location location) {
        for (DPItemPoint dpItemPoint : itemPoints) {
            if (dpItemPoint.getLocation().equals(location)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds an item point.
     *
     * @param itemPoint The item point.
     */
    public void addItemPoint(DPItemPoint itemPoint) {
        itemPoints.add(itemPoint);
        FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
        String path = "Parties." + partyName + ".itempoints";
        List<String> dpItemPoints = partyConfig.getStringList(path);
        dpItemPoints.add(itemPoint.toConfig());
        partyConfig.set(path, dpItemPoints);
        dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
    }

    /**
     * Removes an item point.
     *
     * @param location The location of the item point.
     */
    public void removeItemPoint(Location location) {
        for (DPItemPoint dpItemPoint : new HashSet<>(itemPoints)) {
            if (dpItemPoint.getLocation().equals(location)) {
                itemPoints.remove(dpItemPoint);
                FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
                String path = "Parties." + partyName + ".itempoints";
                List<String> dpItemPoints = partyConfig.getStringList(path);
                dpItemPoints.remove(dpItemPoint.toConfig());
                partyConfig.set(path, dpItemPoints);
                dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
            }
        }
    }

    /**
     * Gets the item points.
     *
     * @return The points items can spawn at during a party.
     */
    public List<DPItemPoint> getItemPoints() {
        return itemPoints;
    }

    /**
     * Checks if the party has a firework point.
     *
     * @param location The location of the firework point.
     * @return True if the party has the firework point, else false.
     */
    public boolean hasFireworkPoint(Location location) {
        for (DPFireworkPoint dpFireworkPoint : fireworkPoints) {
            if (dpFireworkPoint.getLocation().equals(location)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a firework point.
     *
     * @param fireworkPoint The firework point.
     */
    public void addFireworkPoint(DPFireworkPoint fireworkPoint) {
        fireworkPoints.add(fireworkPoint);
        FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
        String path = "Parties." + partyName + ".fireworkpoints";
        List<String> dpFireworkPoints = partyConfig.getStringList(path);
        dpFireworkPoints.add(fireworkPoint.toConfig());
        partyConfig.set(path, dpFireworkPoints);
        dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
    }

    /**
     * Removes a firework point.
     *
     * @param location The location of the firework point.
     */
    public void removeFireworkPoint(Location location) {
        for (DPFireworkPoint dpFireworkPoint : new HashSet<>(fireworkPoints)) {
            if (dpFireworkPoint.getLocation().equals(location)) {
                fireworkPoints.remove(dpFireworkPoint);
                FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
                String path = "Parties." + partyName + ".fireworkpoints";
                List<String> dpFireworkPoints = partyConfig.getStringList(path);
                dpFireworkPoints.remove(dpFireworkPoint.toConfig());
                partyConfig.set(path, dpFireworkPoints);
                dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
            }
        }
    }

    /**
     * Gets the firework points.
     *
     * @return The points fireworks can spawn at after a party.
     */
    public List<DPFireworkPoint> getFireworkPoints() {
        return fireworkPoints;
    }

    /**
     * Gets the teleport.
     *
     * @return The teleport location of the party.
     */
    public Location getTeleport() {
        return teleport;
    }

    /**
     * Sets the teleport.
     *
     * @param teleport The teleport location of the party.
     */
    public void setTeleport(Location teleport) {
        if (teleport != this.teleport) {
            this.teleport = teleport;
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".teleport", DPUtils.locationToString(teleport));
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    /**
     * Gets the max length.
     *
     * @return The max amount of ticks the party can last.
     */
    public long getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the max length.
     *
     * @param maxLength The max amount of ticks the party can last.
     */
    public void setMaxLength(long maxLength) {
        if (maxLength != this.maxLength) {
            this.maxLength = maxLength;
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".maxlength", maxLength);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    /**
     * Gets the item delay.
     *
     * @return The ticks between each item dropped.
     */
    public long getItemDelay() {
        return itemDelay;
    }

    /**
     * Sets the item delay.
     *
     * @param itemDelay The ticks between each item dropped.
     */
    public void setItemDelay(long itemDelay) {
        if (itemDelay != this.itemDelay) {
            this.itemDelay = itemDelay;
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".itemdelay", itemDelay);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    /**
     * Gets the max stack size.
     *
     * @return The max stack size of dropped items.
     */
    public int getMaxStackSize() {
        return maxStackSize;
    }

    /**
     * Sets the max stack size.
     *
     * @param maxStackSize The max stack size of dropped items.
     */
    public void setMaxStackSize(int maxStackSize) {
        if (maxStackSize != this.maxStackSize) {
            this.maxStackSize = maxStackSize;
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".maxstacksize", maxStackSize);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    /**
     * Gets the firework amount.
     *
     * @return The amount of fireworks launched after the party.
     */
    public int getFireworkAmount() {
        return fireworkAmount;
    }

    /**
     * Sets the firework amount.
     *
     * @param fireworkAmount The amount of fireworks launched after the party.
     */
    public void setFireworkAmount(int fireworkAmount) {
        if (fireworkAmount != this.fireworkAmount) {
            this.fireworkAmount = fireworkAmount;
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".fireworkamount", fireworkAmount);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    /**
     * Gets the firework delay.
     *
     * @return The ticks between each firework launch.
     */
    public long getFireworkDelay() {
        return fireworkDelay;
    }

    /**
     * Sets the firework delay.
     *
     * @param fireworkDelay The ticks between each firework launch.
     */
    public void setFireworkDelay(long fireworkDelay) {
        if (fireworkDelay != this.fireworkDelay) {
            this.fireworkDelay = fireworkDelay;
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".fireworkdelay", fireworkDelay);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    /**
     * Checks if the party starts periodically.
     *
     * @return True if the party starts periodically, else false.
     */
    public boolean startsPeriodically() {
        return startPeriodically;
    }

    /**
     * Sets if the party starts periodically.
     *
     * @param startPeriodically If the party should start periodically.
     */
    public void setStartPeriodically(boolean startPeriodically) {
        if (startPeriodically != this.startPeriodically) {
            this.startPeriodically = startPeriodically;
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".startperiodically", startPeriodically);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
            if (startPeriodically) {
                startPeriodicTimer();
            } else {
                stopPeriodicTimer();
            }
        }
    }

    /**
     * Gets the start period.
     *
     * @return The ticks between periodic starts.
     */
    public long getStartPeriod() {
        return startPeriod;
    }

    /**
     * Sets the start period.
     *
     * @param startPeriod The ticks between periodic starts.
     */
    public void setStartPeriod(long startPeriod) {
        if (startPeriod != this.startPeriod) {
            this.startPeriod = startPeriod;
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".startperiod", startPeriod);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
            resetPeriodicTimer();
        }
    }

    /**
     * Saves the party to a ConfigurationSection.
     *
     * @param section The ConfigurationSection to save the party to.
     */
    public void save(ConfigurationSection section) {
        section.set("name", partyName);
        section.set("type", type.name());
        section.set("maxlength", maxLength);
        section.set("itemdelay", itemDelay);
        section.set("maxstacksize", maxStackSize);
        section.set("fireworkamount", fireworkAmount);
        section.set("fireworkdelay", fireworkDelay);
        section.set("startperiodically", startPeriodically);
        section.set("startperiod", startPeriod);
        section.set("teleport", DPUtils.locationToString(teleport));
        List<String> dpItemPoints = new ArrayList<>();
        for (DPItemPoint itemPoint : itemPoints) {
            dpItemPoints.add(itemPoint.toConfig());
        }
        section.set("itempoints", dpItemPoints);
        List<String> dpFireworkPoints = new ArrayList<>();
        for (DPFireworkPoint fireworkPoint : fireworkPoints) {
            dpFireworkPoints.add(fireworkPoint.toConfig());
        }
        section.set("fireworkpoints", dpFireworkPoints);
    }
}
