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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

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
    protected boolean startPeriodically;
    protected long startPeriod;
    protected Location teleport;
    protected List<DPItemPoint> itemPoints = new ArrayList<>();
    protected List<DPFireworkPoint> fireworkPoints = new ArrayList<>();

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

    public String getName() {
        return partyName;
    }

    public PartyType getType() {
        return type;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void teleport(Player player) {
        player.teleport(teleport);
        dropParty.getMessage().sendMessage(player, "party.teleport", partyName);
    }

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

    public void stop() {
        if (isRunning) {
            isRunning = false;
            dropParty.getServer().getScheduler().cancelTask(taskId);
            dropParty.getMessage().sendMessage(dropParty.getServer(), "broadcst.stop", partyName);
        }
    }

    public abstract boolean dropNext();

    public boolean dropItemStack(ItemStack itemStack) {
        if (itemStack != null && itemPoints.size() > 0) {
            Random generator = new Random();
            DPItemPoint itemPoint = itemPoints.get(generator.nextInt(itemPoints.size()));
            itemPoint.getLocation().getWorld().dropItemNaturally(itemPoint.getLocation(), itemStack);
            return true;
        } else {
            return false;
        }
    }

    public void addItemPoint(DPItemPoint itemPoint, boolean saveToConfig) {
        itemPoints.add(itemPoint);
        if (saveToConfig) {
            FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
            String path = "Parties." + partyName + ".itempoints";
            List<String> dpItemPoints = partyConfig.getStringList(path);
            dpItemPoints.add(itemPoint.toConfig());
            partyConfig.set(path, dpItemPoints);
        }
    }

    public void removeItemPoint(Location location, boolean saveToConfig) {
        for (DPItemPoint dpItemPoint : new HashSet<>(itemPoints)) {
            if (dpItemPoint.getLocation().equals(location)) {
                itemPoints.remove(dpItemPoint);
                FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
                String path = "Parties." + partyName + ".itempoints";
                List<String> dpItemPoints = partyConfig.getStringList(path);
                dpItemPoints.remove(dpItemPoint.toConfig());
                partyConfig.set(path, dpItemPoints);
            }
        }
    }

    public List<DPItemPoint> getItemPoints() {
        return itemPoints;
    }

    public void addFireworkPoint(DPFireworkPoint fireworkPoint, boolean saveToConfig) {
        fireworkPoints.add(fireworkPoint);
        if (saveToConfig) {
            FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
            String path = "Parties." + partyName + ".fireworkpoints";
            List<String> dpFireworkPoints = partyConfig.getStringList(path);
            dpFireworkPoints.add(fireworkPoint.toConfig());
            partyConfig.set(path, dpFireworkPoints);
        }
    }

    public void removeFireworkPoint(Location location) {
        for (DPFireworkPoint dpFireworkPoint : new HashSet<>(fireworkPoints)) {
            if (dpFireworkPoint.getLocation().equals(location)) {
                fireworkPoints.remove(dpFireworkPoint);
                FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
                String path = "Parties." + partyName + ".fireworkpoints";
                List<String> dpFireworkPoints = partyConfig.getStringList(path);
                dpFireworkPoints.remove(dpFireworkPoint.toConfig());
                partyConfig.set(path, dpFireworkPoints);
            }
        }
    }

    public List<DPFireworkPoint> getFireworkPoints() {
        return fireworkPoints;
    }

    public Location getTeleport() {
        return teleport;
    }

    public void setTeleport(Location teleport, boolean saveToConfig) {
        this.teleport = teleport;
        if (saveToConfig) {
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".teleport", teleport);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    public long getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(long maxLength, boolean saveToConfig) {
        this.maxLength = maxLength;
        if (saveToConfig) {
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".maxlength", maxLength);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    public long getItemDelay() {
        return itemDelay;
    }

    public void setItemDelay(long itemDelay, boolean saveToConfig) {
        this.itemDelay = itemDelay;
        if (saveToConfig) {
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".itemdelay", itemDelay);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxStackSize(int maxStackSize, boolean saveToConfig) {
        this.maxStackSize = maxStackSize;
        if (saveToConfig) {
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".maxstacksize", maxStackSize);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    public int getFireworkAmount() {
        return fireworkAmount;
    }

    public void setFireworkAmount(int fireworkAmount, boolean saveToConfig) {
        this.fireworkAmount = fireworkAmount;
        if (saveToConfig) {
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".fireworkamount", fireworkAmount);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    public long getFireworkDelay() {
        return fireworkDelay;
    }

    public void setFireworkDelay(long fireworkDelay, boolean saveToConfig) {
        this.fireworkDelay = fireworkDelay;
        if (saveToConfig) {
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".fireworkdelay", fireworkDelay);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    public boolean startsPeriodically() {
        return startPeriodically;
    }

    public void setStartPeriodically(boolean startPeriodically, boolean saveToConfig) {
        this.startPeriodically = startPeriodically;
        if (saveToConfig) {
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".startperiodically", startPeriodically);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    public long getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(long startPeriod, boolean saveToConfig) {
        this.startPeriod = startPeriod;
        if (saveToConfig) {
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + ".startperiod", startPeriod);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        }
    }

    public void save(ConfigurationSection section) {
        section.set("name", partyName);
        section.set("type", type);
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

    public static Party fromConfig(ConfigurationSection section) {
        try {
            String partyName = section.getString("name");
            PartyType partyType = PartyType.valueOf(section.getString("type"));
            long maxLength = section.getLong("maxlength");
            long itemDelay = section.getLong("itemdelay");
            int maxStackSize = section.getInt("maxStackSize");
            int fireworkAmount = section.getInt("fireworkamount");
            long fireworkDelay = section.getLong("fireworkdelay");
            boolean startPeriodically = section.getBoolean("startperiodically");
            long startPeriod = section.getLong("startperiod");
            Location teleport = DPUtils.stringToLocation(section.getString("teleport"));

        } catch (Exception e) {
            return null;
        }
    }
}
