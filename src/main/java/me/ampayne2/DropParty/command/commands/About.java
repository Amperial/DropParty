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
package me.ampayne2.dropparty.command.commands;

import me.ampayne2.dropparty.DropParty;
import me.ampayne2.dropparty.command.DPCommand;
import me.ampayne2.dropparty.message.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that lists some information about drop party.
 */
public class About extends DPCommand {
    private final DropParty dropParty;
    private final static String HEADER = Messenger.HIGHLIGHT_COLOR + "<-------<| " + Messenger.PRIMARY_COLOR + "About Drop Party " + Messenger.HIGHLIGHT_COLOR + "|>------->";
    private final static String URL = Messenger.SECONDARY_COLOR + "dev.bukkit.org/server-mods/dropparty/";
    private final static String AUTHOR = Messenger.SECONDARY_COLOR + "Author: ampayne2";
    private final static String VERSION = Messenger.SECONDARY_COLOR + "Version: ";
    private final static String COMMANDS = Messenger.SECONDARY_COLOR + "Commands: /dp help [page]";

    public About(DropParty dropParty) {
        super(dropParty, "", "Lists some information about drop party.", "/dp", new Permission("dropparty.about", PermissionDefault.TRUE), false);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        sender.sendMessage(HEADER);
        sender.sendMessage(URL);
        sender.sendMessage(AUTHOR);
        sender.sendMessage(VERSION + dropParty.getDescription().getVersion());
        sender.sendMessage(COMMANDS);
    }
}
