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
package ninja.amp.dropparty.parties;

import ninja.amp.dropparty.DPChest;
import ninja.amp.dropparty.DPFireworkPoint;
import ninja.amp.dropparty.DPItemPoint;
import ninja.amp.dropparty.DPUtils;
import ninja.amp.dropparty.DropParty;
import ninja.amp.dropparty.config.ConfigType;
import ninja.amp.dropparty.message.DPMessage;
import ninja.amp.amplib.messenger.Messenger;
import ninja.amp.amplib.messenger.PageList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controls and contains all the information of a drop party.
 */
public class Party {

    private final DropParty dropParty;
    private final String partyName;
    private Location teleport;
    private final Map<PartySetting, Object> partySettings = new HashMap<>();
    private final PageList settingsList;
    private final List<DPChest> chests = new ArrayList<>();
    private final PageList chestList;
    private final List<DPItemPoint> itemPoints = new ArrayList<>();
    private final PageList itemPointList;
    private final List<DPFireworkPoint> fireworkPoints = new ArrayList<>();
    private final PageList fireworkPointList;
    private final Set<String> voters = new HashSet<>();

    private boolean isRunning = false;
    private int taskId;
    private long currentLength;
    private int currentChest = 0;
    private boolean isShootingFireworks = false;
    private int fireworkTaskId;
    private int fireworksShot;
    private int periodicTaskId;

    private final static Random RANDOM = new Random();

    /**
     * Creates a Party from default settings.
     *
     * @param dropParty The DropParty instance.
     * @param partyName The name of the party.
     * @param teleport  The teleport location of the party.
     */
    public Party(DropParty dropParty, String partyName, Location teleport) {
        this.dropParty = dropParty;
        this.partyName = partyName;
        this.teleport = teleport;

        FileConfiguration config = dropParty.getConfig();
        for (PartySetting partySetting : PartySetting.class.getEnumConstants()) {
            Object value = null;
            if (partySetting.getType() == Integer.class) {
                value = config.getInt("defaultsettings." + partySetting.getName(), (int) partySetting.getDefault());
            } else if (partySetting.getType() == Long.class) {
                value = config.getLong("defaultsettings." + partySetting.getName(), (long) partySetting.getDefault());
            } else if (partySetting.getType() == Boolean.class) {
                value = config.getBoolean("defaultsettings." + partySetting.getName(), (boolean) partySetting.getDefault());
            }
            partySettings.put(partySetting, value);
        }
        settingsList = new PageList(dropParty, "Settings", 9);
        updateSettingsList();

        chestList = new PageList(dropParty, "Chests", 9);
        itemPointList = new PageList(dropParty, "Item Points", 9);
        fireworkPointList = new PageList(dropParty, "Firework Points", 9);
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
        teleport = DPUtils.stringToLocation(section.getString("teleport"));
        for (PartySetting partySetting : PartySetting.class.getEnumConstants()) {
            Object value = null;
            if (partySetting.getType() == Integer.class) {
                value = section.getInt(partySetting.getName(), (int) partySetting.getDefault());
            } else if (partySetting.getType() == Long.class) {
                value = section.getLong(partySetting.getName(), (long) partySetting.getDefault());
            } else if (partySetting.getType() == Boolean.class) {
                value = section.getBoolean(partySetting.getName(), (boolean) partySetting.getDefault());
            }
            partySettings.put(partySetting, value);
        }
        settingsList = new PageList(dropParty, "Settings", 9);
        updateSettingsList();

        List<String> dpChests = section.getStringList("chests");
        for (String chest : dpChests) {
            chests.add(DPChest.fromConfig(dropParty, this, chest));
        }
        chestList = new PageList(dropParty, "Chests", 9);
        updateChestList();

        for (String itemPoint : section.getStringList("itempoints")) {
            itemPoints.add(DPItemPoint.fromConfig(dropParty, this, itemPoint));
        }
        itemPointList = new PageList(dropParty, "Item Points", 9);
        updateItemPointList();

        for (String fireworkPoint : section.getStringList("fireworkpoints")) {
            fireworkPoints.add(DPFireworkPoint.fromConfig(dropParty, this, fireworkPoint));
        }
        fireworkPointList = new PageList(dropParty, "Firework Points", 9);
        updateFireworkPointList();
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
     * Checks if the party is running.
     *
     * @return True if the party is running, else false.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Checks if the party can start.
     *
     * @return True if the party isn't running and has items to drop, else false.
     */
    public boolean canStart() {
        if (isRunning) {
            return false;
        }

        boolean hasItems = false;
        for (DPChest chest : chests) {
            for (ItemStack stack : chest.getChest().getInventory().getContents()) {
                if (stack != null) {
                    hasItems = true;
                    break;
                }
            }
        }
        return hasItems;
    }

    /**
     * Starts the party.
     */
    public void start() {
        voters.clear();
        stopShootingFireworks();
        final long maxLength = get(PartySetting.MAX_LENGTH, Long.class);
        final long itemDelay = get(PartySetting.ITEM_DELAY, Long.class);
        taskId = dropParty.getServer().getScheduler().scheduleSyncRepeatingTask(dropParty, () -> {
            currentLength += itemDelay;
            if (currentLength > maxLength || !dropNext()) {
                stop(true);
                currentLength = 0;
                currentChest = 0;
            }
        }, 0, itemDelay);
        dropParty.getMessenger().sendMessage(dropParty.getServer(), DPMessage.BROADCAST_START, partyName, partyName);
        isRunning = true;
    }

    /**
     * Stops the party.
     *
     * @param shootFireworks If the party should shoot fireworks.
     */
    public void stop(boolean shootFireworks) {
        if (isRunning) {
            isRunning = false;
            dropParty.getServer().getScheduler().cancelTask(taskId);
            dropParty.getMessenger().sendMessage(dropParty.getServer(), DPMessage.BROADCAST_STOP, partyName);

            chests.forEach(DPChest::resetCurrentSlot);

            if (shootFireworks) {
                startShootingFireworks();
            }
        }
    }

    /**
     * Teleports a player to the party.
     *
     * @param player The player to teleport to the party.
     */
    public void teleport(Player player) {
        player.teleport(teleport);
        dropParty.getMessenger().sendMessage(player, DPMessage.PARTY_TELEPORT, partyName);
    }

    /**
     * Checks if the party is shooting fireworks.
     *
     * @return True if the party is shooting fireworks, else false.
     */
    public boolean isShootingFireworks() {
        return isShootingFireworks;
    }

    /**
     * Starts shooting fireworks.
     */
    public void startShootingFireworks() {
        if (!isShootingFireworks && fireworkPoints.size() > 0) {
            isShootingFireworks = true;
            final int fireworkAmount = get(PartySetting.FIREWORK_AMOUNT, Integer.class);
            fireworkTaskId = dropParty.getServer().getScheduler().scheduleSyncRepeatingTask(dropParty, () -> {
                fireworksShot++;
                if (fireworksShot <= fireworkAmount) {
                    fireworkPoints.get(RANDOM.nextInt(fireworkPoints.size())).spawnFirework();
                } else {
                    stopShootingFireworks();
                }
            }, 0, get(PartySetting.FIREWORK_DELAY, Long.class));
        }
    }

    /**
     * Stops shooting fireworks.
     */
    public void stopShootingFireworks() {
        if (isShootingFireworks) {
            isShootingFireworks = false;
            fireworksShot = 0;
            dropParty.getServer().getScheduler().cancelTask(fireworkTaskId);
        }
    }

    /**
     * Drops the next item stack in the party.
     *
     * @return True if the next item stack was dropped successfully.
     */
    public boolean dropNext() {
        if (chests.size() > 0 && itemPoints.size() > 0) {
            ItemStack itemStack = null;
            if (get(PartySetting.EMPTY_CHEST, Boolean.class)) {
                for (DPChest chest : chests) {
                    itemStack = chest.getNextItemStack();
                    if (itemStack != null) {
                        break;
                    }
                }
            } else {
                if (currentChest == chests.size()) {
                    return false;
                }
                for (int i = currentChest; i < chests.size(); i++) {
                    itemStack = chests.get(i).getNextItemStack();
                    if (itemStack != null) {
                        break;
                    }
                    currentChest++;
                }
            }
            return itemStack != null && dropItemStack(itemStack);
        } else {
            return false;
        }
    }

    /**
     * Drops an item stack at a random item point.
     *
     * @param itemStack The item stack to drop.
     * @return True if the item stack is not null or air, and has a place to drop.
     */
    public boolean dropItemStack(ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR && itemPoints.size() > 0) {
            DPItemPoint itemPoint = itemPoints.get(RANDOM.nextInt(itemPoints.size()));
            itemPoint.getLocation().getWorld().dropItemNaturally(itemPoint.getLocation(), itemStack);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Starts the periodic start timer.
     */
    public void startPeriodicTimer() {
        if (get(PartySetting.START_PERIODICALLY, Boolean.class)) {
            long startPeriod = get(PartySetting.START_PERIOD, Long.class);
            periodicTaskId = dropParty.getServer().getScheduler().scheduleSyncRepeatingTask(dropParty, () -> {
                if (canStart()) {
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
     * Checks if the party has a chest.
     *
     * @param chest The chest.
     * @return True if the party has a chest, else false.
     */
    public boolean hasChest(Chest chest) {
        if (chest.getInventory().getHolder() instanceof DoubleChest) {
            DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
            for (DPChest dpChest : chests) {
                if (dpChest.getChest().equals(doubleChest.getLeftSide()) || dpChest.getChest().equals(doubleChest.getRightSide())) {
                    return true;
                }
            }
        } else {
            for (DPChest dpChest : chests) {
                if (dpChest.getChest().equals(chest)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds a chest.
     *
     * @param chest The chest.
     */
    public void addChest(DPChest chest) {
        chests.add(chest);
        FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
        String path = "Parties." + partyName + ".chests";
        List<String> dpChests = partyConfig.getStringList(path);
        dpChests.add(chest.toConfig());
        partyConfig.set(path, dpChests);
        dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        updateChestList();
    }

    /**
     * Removes a chest.
     *
     * @param chest The chest.
     */
    public void removeChest(Chest chest) {
        new HashSet<>(chests).stream().filter(dpChest -> dpChest.getChest().equals(chest)).forEach(this::removeChest);
    }

    /**
     * Removes a chest.
     *
     * @param dpChest The chest.
     */
    public void removeChest(DPChest dpChest) {
        chests.remove(dpChest);
        chests.remove(dpChest);
        FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
        String path = "Parties." + partyName + ".chests";
        List<String> dpChests = partyConfig.getStringList(path);
        dpChests.remove(dpChest.toConfig());
        partyConfig.set(path, dpChests);
        dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        updateChestList();
    }

    /**
     * Gets the chests.
     *
     * @return The chests items are taken from during a party.
     */
    public List<DPChest> getChests() {
        return chests;
    }

    /**
     * Gets the PageList of the chests.
     *
     * @return The PageList of chests.
     */
    public PageList getChestList() {
        return chestList;
    }

    /**
     * Updates the PageList of chests.
     */
    public void updateChestList() {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < chests.size(); i++) {
            Location location = chests.get(i).getChest().getLocation();
            strings.add(Messenger.SECONDARY_COLOR + "-ID:" + i + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
        }
        chestList.setStrings(strings);
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
        updateItemPointList();
    }

    /**
     * Removes an item point at a location.
     *
     * @param location The location of the item point.
     */
    public void removeItemPoint(Location location) {
        new HashSet<>(itemPoints).stream().filter(itemPoint -> itemPoint.getLocation().equals(location)).forEach(this::removeItemPoint);
    }

    /**
     * Removes an item point.
     *
     * @param itemPoint The item point.
     */
    public void removeItemPoint(DPItemPoint itemPoint) {
        itemPoints.remove(itemPoint);
        FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
        String path = "Parties." + partyName + ".itempoints";
        List<String> dpItemPoints = partyConfig.getStringList(path);
        dpItemPoints.remove(itemPoint.toConfig());
        partyConfig.set(path, dpItemPoints);
        dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        updateItemPointList();
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
     * Gets the PageList of item points.
     *
     * @return The PageList of item points.
     */
    public PageList getItemPointList() {
        return itemPointList;
    }

    /**
     * Updates the PageList of item points.
     */
    public void updateItemPointList() {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < itemPoints.size(); i++) {
            Location location = itemPoints.get(i).getLocation();
            strings.add(Messenger.SECONDARY_COLOR + "-ID:" + i + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
        }
        itemPointList.setStrings(strings);
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
        updateFireworkPointList();
    }

    /**
     * Removes a firework point at a location.
     *
     * @param location The location of the firework point.
     */
    public void removeFireworkPoint(Location location) {
        new HashSet<>(fireworkPoints).stream().filter(fireworkPoint -> fireworkPoint.getLocation().equals(location)).forEach(this::removeFireworkPoint);
    }

    /**
     * Removes a firework point.
     *
     * @param fireworkPoint The firework point.
     */
    public void removeFireworkPoint(DPFireworkPoint fireworkPoint) {
        fireworkPoints.remove(fireworkPoint);
        FileConfiguration partyConfig = dropParty.getConfigManager().getConfig(ConfigType.PARTY);
        String path = "Parties." + partyName + ".fireworkpoints";
        List<String> dpFireworkPoints = partyConfig.getStringList(path);
        dpFireworkPoints.remove(fireworkPoint.toConfig());
        partyConfig.set(path, dpFireworkPoints);
        dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
        updateFireworkPointList();
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
     * Gets the PageList of firework points.
     *
     * @return The PageList of firework points.
     */
    public PageList getFireworkPointList() {
        return fireworkPointList;
    }

    /**
     * Updates the PageList of firework points.
     */
    public void updateFireworkPointList() {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < fireworkPoints.size(); i++) {
            Location location = fireworkPoints.get(i).getLocation();
            strings.add(Messenger.SECONDARY_COLOR + "-ID:" + i + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
        }
        fireworkPointList.setStrings(strings);
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
     * Gets the value of a {@link ninja.amp.dropparty.parties.PartySetting} of the party.
     *
     * @param partySetting The {@link ninja.amp.dropparty.parties.PartySetting}.
     * @return The value.
     */
    public Object get(PartySetting partySetting) {
        return partySettings.get(partySetting);
    }

    /**
     * Gets the value of a {@link ninja.amp.dropparty.parties.PartySetting} of the party.
     *
     * @param partySetting The {@link ninja.amp.dropparty.parties.PartySetting}.
     * @param type         The value's class.
     * @param <T>          The value's type.
     * @return The value.
     */
    public <T> T get(PartySetting partySetting, Class<T> type) {
        return type.cast(partySettings.get(partySetting));
    }

    /**
     * Sets the value of a {@link ninja.amp.dropparty.parties.PartySetting} of the party.
     *
     * @param partySetting The {@link ninja.amp.dropparty.parties.PartySetting}.
     * @param value        The value.
     * @throws IllegalArgumentException If the value is not valid.
     */
    public void set(PartySetting partySetting, Object value) throws IllegalArgumentException {
        if (!partySetting.getType().isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException();
        }

        value = partySetting.makeValid(value);
        if (!partySettings.get(partySetting).equals(value)) {
            partySettings.put(partySetting, value);
            dropParty.getConfigManager().getConfig(ConfigType.PARTY).set("Parties." + partyName + "." + partySetting.getName(), value);
            dropParty.getConfigManager().getConfigAccessor(ConfigType.PARTY).saveConfig();
            if (partySetting == PartySetting.START_PERIODICALLY) {
                if ((boolean) value) {
                    startPeriodicTimer();
                } else {
                    stopPeriodicTimer();
                }
            } else if (partySetting == PartySetting.START_PERIOD) {
                resetPeriodicTimer();
            } else if (partySetting == PartySetting.VOTE_TO_START) {
                clearVotes();
            } else if (partySetting == PartySetting.REQUIRED_VOTES) {
                voters.clear();
            }
            updateSettingsList();
        }
    }

    /**
     * Gets the PageList of party settings.
     *
     * @return The PageList of party settings.
     */
    public PageList getSettingsList() {
        return settingsList;
    }

    /**
     * Updates the PageList of party settings.
     */
    public void updateSettingsList() {
        List<String> strings = new ArrayList<>();
        for (PartySetting partySetting : PartySetting.class.getEnumConstants()) {
            strings.add(Messenger.PRIMARY_COLOR + partySetting.getDisplayName() + ": " + Messenger.SECONDARY_COLOR + partySettings.get(partySetting).toString());
        }
        settingsList.setStrings(strings);
    }

    /**
     * Checks if a player has voted for the party.
     *
     * @param playerName The name of the player.
     * @return True if the player has voted, else false.
     */
    public boolean hasVoted(String playerName) {
        return voters.contains(playerName);
    }

    /**
     * Adds a vote to start the party.
     *
     * @param playerName The name of the player who voted.
     */
    public void addVote(String playerName) {
        if (get(PartySetting.VOTE_TO_START, Boolean.class) && !isRunning) {
            voters.add(playerName);
            if (voters.size() >= get(PartySetting.REQUIRED_VOTES, Integer.class) && canStart()) {
                start();
            }
        }
    }

    /**
     * Gets the current amount of votes.
     *
     * @return How many players have voted for the party.
     */
    public int getVotes() {
        return voters.size();
    }

    /**
     * Clears the votes of the party.
     */
    public void clearVotes() {
        voters.clear();
    }

    /**
     * Saves the party to a ConfigurationSection.
     *
     * @param section The ConfigurationSection to save the party to.
     */
    public void save(ConfigurationSection section) {
        section.set("name", partyName);
        for (PartySetting partySetting : PartySetting.class.getEnumConstants()) {
            section.set(partySetting.getName(), partySettings.get(partySetting));
        }
        section.set("teleport", DPUtils.locationToString(teleport));
        List<String> dpChests = chests.stream().map(DPChest::toConfig).collect(Collectors.toList());
        section.set("chests", dpChests);
        List<String> dpItemPoints = itemPoints.stream().map(DPItemPoint::toConfig).collect(Collectors.toList());
        section.set("itempoints", dpItemPoints);
        List<String> dpFireworkPoints = fireworkPoints.stream().map(DPFireworkPoint::toConfig).collect(Collectors.toList());
        section.set("fireworkpoints", dpFireworkPoints);
    }

}
