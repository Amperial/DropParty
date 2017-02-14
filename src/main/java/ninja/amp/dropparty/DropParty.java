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
package ninja.amp.dropparty;

import ninja.amp.dropparty.commands.Create;
import ninja.amp.dropparty.commands.Delete;
import ninja.amp.dropparty.commands.ResetVotes;
import ninja.amp.dropparty.commands.Start;
import ninja.amp.dropparty.commands.Stop;
import ninja.amp.dropparty.commands.Teleport;
import ninja.amp.dropparty.commands.Vote;
import ninja.amp.dropparty.commands.list.ListChests;
import ninja.amp.dropparty.commands.list.ListFireworkPoints;
import ninja.amp.dropparty.commands.list.ListItemPoints;
import ninja.amp.dropparty.commands.list.ListParties;
import ninja.amp.dropparty.commands.list.ListPartySettings;
import ninja.amp.dropparty.commands.remove.RemoveChest;
import ninja.amp.dropparty.commands.remove.RemoveFireworkPoint;
import ninja.amp.dropparty.commands.remove.RemoveItemPoint;
import ninja.amp.dropparty.commands.set.SetChest;
import ninja.amp.dropparty.commands.set.SetFireworkPoint;
import ninja.amp.dropparty.commands.set.SetItemPoint;
import ninja.amp.dropparty.commands.set.SetPartySetting;
import ninja.amp.dropparty.commands.set.SetTeleport;
import ninja.amp.dropparty.config.ConfigType;
import ninja.amp.dropparty.message.DPMessage;
import ninja.amp.dropparty.modes.PlayerModeController;
import ninja.amp.amplib.AmpJavaPlugin;
import ninja.amp.amplib.command.Command;
import ninja.amp.amplib.command.CommandGroup;
import ninja.amp.amplib.command.commands.AboutCommand;
import ninja.amp.amplib.command.commands.HelpCommand;
import ninja.amp.amplib.command.commands.ReloadCommand;
import ninja.amp.amplib.messenger.DefaultMessage;
import ninja.amp.amplib.messenger.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.EnumSet;

/**
 * The main class of the Drop Party plugin.
 */
public class DropParty extends AmpJavaPlugin {
    private PartyManager partyManager;
    private PlayerModeController playerModeController;

    @Override
    public void onEnable() {
        DefaultMessage.PREFIX.setMessage("&6[&5Drop Party&6] &7");
        DefaultMessage.RELOAD.setMessage("Reloaded Drop Party.");
        enableAmpLib();
        getConfigManager().registerConfigTypes(EnumSet.allOf(ConfigType.class));
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        Messenger.PRIMARY_COLOR = ChatColor.valueOf(config.getString("colors.primary", "DARK_PURPLE"));
        Messenger.SECONDARY_COLOR = ChatColor.valueOf(config.getString("colors.secondary", "GRAY"));
        Messenger.HIGHLIGHT_COLOR = ChatColor.valueOf(config.getString("colors.highlights", "GOLD"));
        config.set("configversion", getDescription().getVersion());
        saveConfig();
        getMessenger().registerMessages(EnumSet.allOf(DPMessage.class));
        partyManager = new PartyManager(this);
        playerModeController = new PlayerModeController(this);
        new DPListener(this);
        if (Bukkit.getPluginManager().isPluginEnabled("Votifier")) {
            new VoteListener(this);
        }

        Command about = new AboutCommand(this);
        about.setCommandUsage("/dp");
        Command help = new HelpCommand(this);
        help.setCommandUsage("/dp help");
        Command reload = new ReloadCommand(this);
        reload.setCommandUsage("/dp reload");
        CommandGroup dropParty = new CommandGroup(this, "dropparty")
                .addChildCommand(about)
                .addChildCommand(help)
                .addChildCommand(reload)
                .addChildCommand(new Create(this))
                .addChildCommand(new Delete(this))
                .addChildCommand(new Start(this))
                .addChildCommand(new Stop(this))
                .addChildCommand(new Teleport(this))
                .addChildCommand(new Vote(this))
                .addChildCommand(new ResetVotes(this))
                .addChildCommand(new CommandGroup(this, "set")
                        .addChildCommand(new SetPartySetting(this))
                        .addChildCommand(new SetChest(this))
                        .addChildCommand(new SetItemPoint(this))
                        .addChildCommand(new SetFireworkPoint(this))
                        .addChildCommand(new SetTeleport(this)))
                .addChildCommand(new CommandGroup(this, "remove")
                        .addChildCommand(new RemoveChest(this))
                        .addChildCommand(new RemoveItemPoint(this))
                        .addChildCommand(new RemoveFireworkPoint(this)))
                .addChildCommand(new CommandGroup(this, "list")
                        .addChildCommand(new ListParties(this))
                        .addChildCommand(new ListPartySettings(this))
                        .addChildCommand(new ListChests(this))
                        .addChildCommand(new ListItemPoints(this))
                        .addChildCommand(new ListFireworkPoints(this)));
        dropParty.setPermission(new Permission("dropparty.admin", PermissionDefault.OP));
        getCommandController().addCommand(dropParty);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        playerModeController.clearModes();
        playerModeController = null;
        partyManager.stopParties();
        partyManager = null;
        disableAmpLib();
    }

    /**
     * Gets the drop party party manager.
     *
     * @return The PartyManager instance.
     */
    public PartyManager getPartyManager() {
        return partyManager;
    }

    /**
     * Gets the drop party player mode controller.
     *
     * @return The PlayerModeController instance.
     */
    public PlayerModeController getPlayerModeController() {
        return playerModeController;
    }

}
