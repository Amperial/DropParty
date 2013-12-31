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
import me.ampayne2.dropparty.command.commands.*;
import me.ampayne2.dropparty.command.commands.list.*;
import me.ampayne2.dropparty.command.commands.remove.RemoveChest;
import me.ampayne2.dropparty.command.commands.remove.RemoveFireworkPoint;
import me.ampayne2.dropparty.command.commands.remove.RemoveItemPoint;
import me.ampayne2.dropparty.command.commands.set.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * The drop party command executor.
 */
public class CommandController implements TabExecutor {
    private final DropParty dropParty;
    private final Command mainCommand;

    /**
     * Creates a new command controller.
     *
     * @param dropParty The DropParty instance.
     */
    public CommandController(DropParty dropParty) {
        this.dropParty = dropParty;

        mainCommand = new Command(dropParty, "dropparty", new Permission("dropparty.all", PermissionDefault.OP), false)
                .addChildCommand(new About(dropParty))
                .addChildCommand(new Help(dropParty))
                .addChildCommand(new Create(dropParty))
                .addChildCommand(new Delete(dropParty))
                .addChildCommand(new Start(dropParty))
                .addChildCommand(new Stop(dropParty))
                .addChildCommand(new Teleport(dropParty))
                .addChildCommand(new Vote(dropParty))
                .addChildCommand(new ResetVotes(dropParty))
                .addChildCommand(new Command(dropParty, "set", new Permission("dropparty.set.all", PermissionDefault.OP), false)
                        .addChildCommand(new SetChest(dropParty))
                        .addChildCommand(new SetFireworkPoint(dropParty))
                        .addChildCommand(new SetItemPoint(dropParty))
                        .addChildCommand(new SetPartySetting(dropParty))
                        .addChildCommand(new SetTeleport(dropParty)))
                .addChildCommand(new Command(dropParty, "remove", new Permission("dropparty.remove.all", PermissionDefault.OP), false)
                        .addChildCommand(new RemoveChest(dropParty))
                        .addChildCommand(new RemoveFireworkPoint(dropParty))
                        .addChildCommand(new RemoveItemPoint(dropParty)))
                .addChildCommand(new Command(dropParty, "list", new Permission("dropparty.list.all", PermissionDefault.TRUE), false)
                        .addChildCommand(new ListChests(dropParty))
                        .addChildCommand(new ListFireworkPoints(dropParty))
                        .addChildCommand(new ListItemPoints(dropParty))
                        .addChildCommand(new ListParties(dropParty))
                        .addChildCommand(new ListSettings(dropParty)));

        dropParty.getCommand(mainCommand.getName()).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("dropparty")) {
            String subCommand = args.length > 0 ? args[0] : "";
            if (mainCommand.hasChildCommand(subCommand)) {
                if (subCommand.equals("")) {
                    mainCommand.execute(subCommand, sender, args);
                } else {
                    String[] newArgs;
                    if (args.length == 1) {
                        newArgs = new String[0];
                    } else {
                        newArgs = new String[args.length - 1];
                        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                    }
                    mainCommand.execute(subCommand, sender, newArgs);
                }
            } else {
                dropParty.getMessenger().sendMessage(sender, "error.command.invalidsubcommand", "\"" + subCommand + "\"", "\"dropparty\"");
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("dropparty")) {
            Command command = mainCommand;
            if (args.length > 0) {
                int commandAmount = 1;
                for (String arg : args) {
                    if (command.hasChildCommand(arg)) {
                        command = command.getChildCommand(arg);
                        commandAmount++;
                    }
                }
                String[] newArgs;
                if (args.length == 1) {
                    newArgs = new String[0];
                } else {
                    newArgs = new String[args.length - commandAmount];
                    System.arraycopy(args, commandAmount, newArgs, 0, args.length - commandAmount);
                }
                return command.getTabCompleteList(newArgs);
            }
            return command.getTabCompleteList(args);
        }
        return new ArrayList<>();
    }

    /**
     * Gets the main drop party command.
     *
     * @return The main command.
     */
    public Command getMainCommand() {
        return mainCommand;
    }
}
