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
package me.ampayne2.dropparty.commands;

import me.ampayne2.dropparty.DropParty;
import ninja.amp.amplib.command.Command;
import ninja.amp.amplib.messenger.DefaultMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that updates drop party.
 */
public class Update extends Command {
    private final DropParty dropParty;
    private boolean needsConfirmation = true;
    private boolean alreadyUpdated = false;

    public Update(DropParty dropParty) {
        super(dropParty, "update");
        setDescription("Updates the drop party plugin.");
        setCommandUsage("/dp update");
        setPermission(new Permission("dropparty.update", PermissionDefault.OP));
        setArgumentRange(0, 0);
        setPlayerOnly(false);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        if (alreadyUpdated) {
            dropParty.getMessenger().sendRawMessage(sender, DefaultMessage.PREFIX + "Already updated! Restart required.");
        } else {
            if (dropParty.getUpdateManager().updateAvailable()) {
                if (needsConfirmation) {
                    dropParty.getMessenger().sendRawMessage(sender, DefaultMessage.PREFIX + "Are you sure? Type /dp update to confirm");
                    needsConfirmation = false;
                } else {
                    dropParty.getUpdateManager().update();
                    dropParty.getMessenger().sendRawMessage(sender, DefaultMessage.PREFIX + "Update started! Check console for progress.");
                    alreadyUpdated = true;
                }
            } else {
                dropParty.getMessenger().sendRawMessage(sender, DefaultMessage.PREFIX + "No update available!");
            }
        }
    }
}
