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
package us.divinerealms.neon.dropparty;

import us.divinerealms.neon.dropparty.commands.Create;
import us.divinerealms.neon.dropparty.commands.Delete;
import us.divinerealms.neon.dropparty.commands.ResetVotes;
import us.divinerealms.neon.dropparty.commands.Start;
import us.divinerealms.neon.dropparty.commands.Stop;
import us.divinerealms.neon.dropparty.commands.Teleport;
import us.divinerealms.neon.dropparty.commands.Vote;
import us.divinerealms.neon.dropparty.commands.list.ListChests;
import us.divinerealms.neon.dropparty.commands.list.ListFireworkPoints;
import us.divinerealms.neon.dropparty.commands.list.ListItemPoints;
import us.divinerealms.neon.dropparty.commands.list.ListParties;
import us.divinerealms.neon.dropparty.commands.list.ListPartySettings;
import us.divinerealms.neon.dropparty.commands.remove.RemoveChest;
import us.divinerealms.neon.dropparty.commands.remove.RemoveFireworkPoint;
import us.divinerealms.neon.dropparty.commands.remove.RemoveItemPoint;
import us.divinerealms.neon.dropparty.commands.set.SetChest;
import us.divinerealms.neon.dropparty.commands.set.SetFireworkPoint;
import us.divinerealms.neon.dropparty.commands.set.SetItemPoint;
import us.divinerealms.neon.dropparty.commands.set.SetPartySetting;
import us.divinerealms.neon.dropparty.commands.set.SetTeleport;
import us.divinerealms.neon.dropparty.config.ConfigType;
import us.divinerealms.neon.dropparty.message.DPMessage;
import us.divinerealms.neon.dropparty.modes.PlayerModeController;
import us.divinerealms.neon.amplib.AmpJavaPlugin;
import us.divinerealms.neon.amplib.command.Command;
import us.divinerealms.neon.amplib.command.CommandGroup;
import us.divinerealms.neon.amplib.command.commands.AboutCommand;
import us.divinerealms.neon.amplib.command.commands.HelpCommand;
import us.divinerealms.neon.amplib.command.commands.ReloadCommand;
import us.divinerealms.neon.amplib.messenger.DefaultMessage;
import us.divinerealms.neon.amplib.messenger.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.io.UnsupportedEncodingException;
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
        try {
            enableAmpLib();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getConfigManager().registerConfigTypes(EnumSet.allOf(ConfigType.class));
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        Messenger.PRIMARY_COLOR = ChatColor.valueOf(config.getString("colors.primary", "DARK_PURPLE"));
        Messenger.SECONDARY_COLOR = ChatColor.valueOf(config.getString("colors.secondary", "GRAY"));
        Messenger.HIGHLIGHT_COLOR = ChatColor.valueOf(config.getString("colors.highlights", "GOLD"));
        config.set("configversion", getDescription().getVersion());
        saveConfig();
        try {
            getMessenger().registerMessages(EnumSet.allOf(DPMessage.class));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            partyManager = new PartyManager(this);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
