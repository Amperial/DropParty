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
package ninja.amp.dropparty.commands.remove;

import ninja.amp.dropparty.DropParty;
import ninja.amp.dropparty.message.DPMessage;
import ninja.amp.dropparty.modes.PlayerMode;
import ninja.amp.dropparty.parties.Party;
import ninja.amp.amplib.command.Command;
import ninja.amp.amplib.messenger.DefaultMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that removes a chest of a certain id or sets the sender to chest removal mode.
 */
public class RemoveChest extends Command {

    private final DropParty dropParty;

    public RemoveChest(DropParty dropParty) {
        super(dropParty, "chest");
        setDescription("Removes a chest or sets you to chest removal mode.");
        setCommandUsage("/dp remove chest <party> [id]");
        setPermission(new Permission("dropparty.remove.chest", PermissionDefault.OP));
        setArgumentRange(1, 2);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String partyName = args[0];
        if (dropParty.getPartyManager().hasParty(partyName)) {
            Party party = dropParty.getPartyManager().getParty(partyName);
            if (args.length == 1) {
                dropParty.getPlayerModeController().setPlayerMode((Player) sender, PlayerMode.REMOVING_CHESTS, party);
            } else {
                try {
                    int id = Integer.parseInt(args[1]);
                    if (party.getChests().size() > id) {
                        party.removeChest(party.getChests().get(id));
                        dropParty.getMessenger().sendMessage(sender, DPMessage.REMOVE_CHEST, partyName);
                    } else {
                        dropParty.getMessenger().sendMessage(sender, DPMessage.CHEST_IDDOESNTEXIST, args[1], partyName);
                    }
                } catch (NumberFormatException e) {
                    dropParty.getMessenger().sendMessage(sender, DefaultMessage.ERROR_NUMBERFORMAT);
                }
            }
        } else {
            dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_DOESNTEXIST, partyName);
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        return args.length == 0 ? dropParty.getPartyManager().getPartyList() : new ArrayList<>();
    }

}
