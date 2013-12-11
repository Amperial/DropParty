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
package me.ampayne2.dropparty.command.commands.remove;

import me.ampayne2.dropparty.DropParty;
import me.ampayne2.dropparty.command.DPCommand;
import me.ampayne2.dropparty.modes.PlayerMode;
import me.ampayne2.dropparty.parties.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class RemoveFireworkPoint extends DPCommand {
    private final DropParty dropParty;

    public RemoveFireworkPoint(DropParty dropParty) {
        super(dropParty, "fireworkpoint", new Permission("dropparty.remove.fireworkpoint", PermissionDefault.OP), 1, 2, true);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String partyName = args[0];
        if (dropParty.getPartyManager().hasParty(partyName)) {
            Party party = dropParty.getPartyManager().getParty(partyName);
            if (args.length == 1) {
                dropParty.getPlayerModeController().setPlayerMode((Player) sender, PlayerMode.REMOVING_FIREWORK_POINTS, party);
            } else {
                try {
                    int id = Integer.parseInt(args[1]);
                    if (party.getFireworkPoints().size() > id) {
                        // TODO: Remove fireworkpoint of id
                    } else {
                        dropParty.getMessage().sendMessage(sender, "error.fireworkpoint.iddoesntexist", args[1], partyName);
                    }
                } catch (NumberFormatException e) {
                    dropParty.getMessage().sendMessage(sender, "error.numberformat");
                }
            }
        } else {
            dropParty.getMessage().sendMessage(sender, "error.party.doesntexist", partyName);
        }
    }
}
