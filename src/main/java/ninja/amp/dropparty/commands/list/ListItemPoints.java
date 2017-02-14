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
package ninja.amp.dropparty.commands.list;

import ninja.amp.dropparty.DropParty;
import ninja.amp.dropparty.message.DPMessage;
import ninja.amp.dropparty.parties.Party;
import ninja.amp.amplib.command.Command;
import ninja.amp.amplib.messenger.PageList;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that lists the item points of a drop party.
 */
public class ListItemPoints extends Command {

    private final DropParty dropParty;

    public ListItemPoints(DropParty dropParty) {
        super(dropParty, "itempoints");
        setDescription("Lists the item points of a drop party.");
        setCommandUsage("/dp list itempoints <party> [page]");
        setPermission(new Permission("dropparty.list.itempoints", PermissionDefault.TRUE));
        setArgumentRange(1, 2);
        setPlayerOnly(false);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String partyName = args[0];
        if (dropParty.getPartyManager().hasParty(partyName)) {
            Party party = dropParty.getPartyManager().getParty(partyName);
            int pageNumber = 1;
            if (args.length == 2) {
                pageNumber = PageList.getPageNumber(args[1]);
            }
            party.getItemPointList().sendPage(pageNumber, sender);
        } else {
            dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_DOESNTEXIST, partyName);
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        return args.length == 0 ? dropParty.getPartyManager().getPartyList() : new ArrayList<>();
    }

}
