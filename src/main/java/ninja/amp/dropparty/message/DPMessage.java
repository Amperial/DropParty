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
package ninja.amp.dropparty.message;

import ninja.amp.amplib.messenger.Message;

/**
 * Messages in Drop Party.
 */
public enum DPMessage implements Message {
    PARTY_CREATE("Party.Create", "Created drop party &5%s&7."),
    PARTY_DELETE("Party.Delete", "Deleted drop party &5%s&7."),
    PARTY_START("Party.Start", "Started drop party &5%s&7."),
    PARTY_ALREADYRUNNING("Party.AlreadyRunning", "&4Drop party &5%s&4 is already running."),
    PARTY_NOITEMS("Party.NoItems", "&4Drop party &5%s&4 has no items to drop."),
    PARTY_STOP("Party.Stop", "Stopped drop party &5%s&7."),
    PARTY_NOTRUNNING("Party.NotRunning", "&4Drop party &5%s&4 is not running."),
    PARTY_STOPFIREWORKS("Party.StopFireworks", "Stopped the fireworks of drop party &5%s&7."),
    PARTY_TELEPORT("Party.Teleport", "Teleported to drop party &5%s&7."),
    PARTY_VOTE("Party.Vote", "Voted for drop party &5%s&7."),
    PARTY_CANNOTVOTE("Party.CannotVote", "&4Drop party &5%s&4 can not be started by voting."),
    PARTY_ALREADYVOTED("Party.AlreadyVoted", "&4Already voted for drop party &5%s&4."),
    PARTY_RESETVOTES("Party.ResetVotes", "Reset votes for drop party &5%s&7."),
    PARTY_ALREADYEXISTS("Party.AlreadyExists", "&4Drop party &5%s&4 already exists."),
    PARTY_DOESNTEXIST("Party.DoesntExist", "&4Drop party &5%s&4 doesn't exist."),
    PARTY_NOTAPARTYSETTING("Party.NotAPartySetting", "&4Not a valid party setting."),
    PARTY_NONEEXIST("Party.NoneExist", "&4No drop parties found. Create one with /dp create"),
    PARTY_NOTSPECIFIED("Party.NotSpecified", "&4Please specify a drop party."),

    BROADCAST_START("Broadcast.Start", "Drop party &5%s&7 started! Teleport with /dp teleport &5%s&7"),
    BROADCAST_STOP("Broadcast.Stop", "Drop party &5%s&7 has ended!"),

    MODE_ENABLE("Mode.Enable", "Enabled &5%s&7 for drop party &5%s&7."),
    MODE_DISABLE("Mode.Disable", "Disabled &5%s&7 for drop party &5%s&7."),

    SET_CHEST("Set.Chest", "Set chest for drop party &5%s&7."),
    SET_ITEMPOINT("Set.ItemPoint", "Set item point for drop party &5%s&7."),
    SET_FIREWORKPOINT("Set.FireworkPoint", "Set firework point for drop party &5%s&7."),
    SET_TELEPORT("Set.Teleport", "Set teleport for drop party &5%s&7."),
    SET_PARTYSETTING("Set.PartySetting", "Set party setting &5%s&7 of drop party &5%s&7 to &5%s&7."),

    REMOVE_CHEST("Remove.Chest", "Removed chest from drop party &5%s&7."),
    REMOVE_ITEMPOINT("Remove.ItemPoint", "Removed item point from drop party &5%s&7."),
    REMOVE_FIREWORKPOINT("Remove.FireworkPoint", "Removed item point from drop party &5%s&7."),
    REMOVE_TELEPORT("Remove.Teleport", "Removed teleport from drop party &5%s&7."),

    CHEST_ALREADYEXISTS("Chest.AlreadyExists", "&4A drop party chest already exists at this location."),
    CHEST_DOESNTEXIST("Chest.DoesntExist", "&4No drop party chests exist at this location."),
    CHEST_IDDOESNTEXIST("Chest.IdDoesntExist", "&4No chest of the id &5%s&4 of the drop party &5%s&4 found."),
    CHEST_CANTBREAK("Chest.CantBreak", "&4You must remove this chest from all drop parties before breaking it."),

    ITEMPOINT_ALREADYEXISTS("ItemPoint.AlreadyExists", "&4An item point already exists at this location."),
    ITEMPOINT_DOESNTEXIST("ItemPoint.DoesntExist", "&4No drop party item points exist at this location."),
    ITEMPOINT_IDDOESNTEXIST("ItemPoint.IdDoesntExist", "&4No item point of the id &5%s&4 of the drop party &5%s&4 found."),

    FIREWORKPOINT_ALREADYEXISTS("FireworkPoint.AlreadyExists", "&4A firework point already exists at this location."),
    FIREWORKPOINT_DOESNTEXIST("FireworkPoint.DoesntExist", "&4No drop party firework points exist at this location."),
    FIREWORKPOINT_IDDOESNTEXIST("FireworkPoint.IdDoesntExist", "&4No firework point of the id &5%s&4 of the drop party &5%s&4 found.");

    private String message;
    private final String path;
    private final String defaultMessage;

    DPMessage(String path, String defaultMessage) {
        this.message = defaultMessage;
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    /**
     * Gets the message string.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message string.
     *
     * @param message The message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the path to the message.
     *
     * @return The path to the message.
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the default message string of the message.
     *
     * @return The default message.
     */
    public String getDefault() {
        return defaultMessage;
    }

    @Override
    public String toString() {
        return message;
    }

}
