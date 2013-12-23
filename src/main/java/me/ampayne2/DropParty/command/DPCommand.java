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
package me.ampayne2.dropparty.command;

import me.ampayne2.dropparty.DropParty;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

/**
 * The base layout for a drop party command.
 */
public abstract class DPCommand extends Command {
    private final String commandUsage;
    private final String description;

    /**
     * Creates a new DPCommand that must have an amount of args within a range.
     *
     * @param dropParty     The DropParty instance.
     * @param name          The name of the command.
     * @param permission    The permission of the command.
     * @param minArgsLength The minimum required args length of the command.
     * @param maxArgsLength The maximum required args length of the command.
     * @param playerOnly    If the command can only be run by a player.
     */
    public DPCommand(DropParty dropParty, String name, String description, String commandUsage, Permission permission, int minArgsLength, int maxArgsLength, boolean playerOnly) {
        super(dropParty, name, permission, minArgsLength, maxArgsLength, playerOnly);
        this.commandUsage = commandUsage;
        this.description = description;
    }

    /**
     * Creates a new DPCommand that must have an exact amount of args.
     *
     * @param dropParty       The DropParty instance.
     * @param name            The name of the command.
     * @param permission      The permission of the command.
     * @param exactArgsLength The exact required args length of the command.
     * @param playerOnly      If the command can only be run by a player.
     */
    public DPCommand(DropParty dropParty, String name, String description, String commandUsage, Permission permission, int exactArgsLength, boolean playerOnly) {
        super(dropParty, name, permission, exactArgsLength, playerOnly);
        this.commandUsage = commandUsage;
        this.description = description;
    }

    /**
     * Creates a new DPCommand that can have any amount of args.
     *
     * @param dropParty  The DropParty instance.
     * @param name       The name of the command.
     * @param permission The permission of the command.
     * @param playerOnly If the command can only be run by a player.
     */
    public DPCommand(DropParty dropParty, String name, String description, String commandUsage, Permission permission, boolean playerOnly) {
        super(dropParty, name, permission, playerOnly);
        this.commandUsage = commandUsage;
        this.description = description;
    }

    /**
     * Gets the DPCommand's command usage.
     *
     * @return The command usage.
     */
    public String getCommandUsage() {
        return commandUsage;
    }

    /**
     * Gets the DPCommand's description.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    @Override
    public abstract void execute(String command, CommandSender sender, String[] args);
}
