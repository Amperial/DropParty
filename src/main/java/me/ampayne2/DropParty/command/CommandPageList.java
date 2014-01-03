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
import me.ampayne2.dropparty.message.PageList;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * A PageList that lists all of the drop party commands and their description.
 */
public class CommandPageList extends PageList {
    public CommandPageList(DropParty dropParty) {
        super(dropParty, "Commands", 8);
        List<String> strings = new ArrayList<>();
        for (Command command : dropParty.getCommandController().getMainCommand().getChildren(true)) {
            strings.add(ChatColor.DARK_PURPLE + ((DPCommand) command).getCommandUsage());
            strings.add(ChatColor.GRAY + "-" + ((DPCommand) command).getDescription());
        }
        setStrings(strings);
    }
}