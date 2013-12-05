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
import me.ampayne2.dropparty.command.interfaces.Command;
import me.ampayne2.dropparty.command.interfaces.DPCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A command that can contain other commands.
 */
public class SubCommand implements Command {
    private final DropParty dropParty;
    private Map<String, Command> commandList = new HashMap<>();
    private Map<String, String> permissionList = new HashMap<>();
    private Map<String, Integer> argsLength = new HashMap<>();
    private Map<String, Boolean> playerOnly = new HashMap<>();

    /**
     * Creates a new SubCommand.
     *
     * @param dropParty The DropParty instance.
     */
    public SubCommand(DropParty dropParty) {
        this.dropParty = dropParty;
    }

    /**
     * Adds a command to the SubCommand.
     *
     * @param command    The command to add.
     * @param name       The name of the command.
     * @param permission The permission of the command.
     * @param argsLength The args length of the command.
     * @param playerOnly If the command can only be executed by a player.
     */
    public void addCommand(Command command, String name, String permission, Integer argsLength, boolean playerOnly) {
        commandList.put(name, command);
        if (command instanceof DPCommand) {
            permissionList.put(name, permission);
            this.argsLength.put(name, argsLength);
            this.playerOnly.put(name, playerOnly);
        }
    }

    /**
     * Checks if a command exists.
     *
     * @param name The name of the command.
     * @return True if the command exists, else false.
     */
    public boolean commandExists(String name) {
        return commandList.containsKey(name);
    }

    /**
     * Executes a command in the subcommand.
     *
     * @param command The name of the command.
     * @param sender  The sender of the command.
     * @param args    The arguments of the command.
     */
    public void execute(String command, CommandSender sender, String[] args) {
        if (commandExists(command)) {
            Command entry = commandList.get(command);
            if (entry instanceof DPCommand) {
                if (argsLength.get(command) == -1 || argsLength.get(command) == args.length) {
                    if (checkPermission(sender, permissionList.get(command))) {
                        if (sender instanceof Player || !playerOnly.get(command)) {
                            ((DPCommand) entry).execute(sender, args);
                        } else {
                            dropParty.getMessage().sendMessage(sender, "commands.notaplayer");
                        }
                    } else {
                        dropParty.getMessage().sendMessage(sender, "permissions.nopermission", command);
                    }
                } else {
                    dropParty.getMessage().sendMessage(sender, "commandusages." + command);
                }
            } else if (entry instanceof SubCommand) {
                SubCommand subCommand = (SubCommand) entry;

                String subSubCommand = "";
                if (args.length != 0) {
                    subSubCommand = args[0];
                }

                if (subCommand.commandExists(subSubCommand)) {
                    String[] newArgs;
                    if (args.length == 0) {
                        newArgs = args;
                    } else {
                        newArgs = new String[args.length - 1];
                        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                    }
                    subCommand.execute(subSubCommand, sender, newArgs);
                } else {
                    dropParty.getMessage().sendMessage(sender, "commands.invalidsubcommand", "\"" + subSubCommand + "\"", "\"" + command + "\"");
                    dropParty.getMessage().sendMessage(sender, "commands.validsubcommands", subCommand.getSubCommandList());
                }
            }
        }
    }

    /**
     * Gets this SubCommand's sub commands.
     *
     * @return The list of sub commands.
     */
    public String getSubCommandList() {
        return Arrays.toString(commandList.keySet().toArray());
    }

    /**
     * Checks if a sender has a certain permission.
     *
     * @param sender     The sender.
     * @param permission The permission.
     * @return True if the sender has the permission, else false.
     */
    private boolean checkPermission(CommandSender sender, String permission) {
        boolean result = true;
        if (permission != null) {
            result = sender.hasPermission(permission);
        }
        return result;
    }
}
