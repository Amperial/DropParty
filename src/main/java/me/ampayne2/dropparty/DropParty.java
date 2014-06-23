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
package me.ampayne2.dropparty;

import me.ampayne2.amplib.AmpJavaPlugin;
import me.ampayne2.amplib.command.Command;
import me.ampayne2.amplib.command.CommandGroup;
import me.ampayne2.amplib.command.commands.AboutCommand;
import me.ampayne2.amplib.command.commands.HelpCommand;
import me.ampayne2.amplib.command.commands.ReloadCommand;
import me.ampayne2.amplib.messenger.DefaultMessage;
import me.ampayne2.amplib.messenger.Messenger;
import me.ampayne2.dropparty.commands.Create;
import me.ampayne2.dropparty.commands.Delete;
import me.ampayne2.dropparty.commands.ResetVotes;
import me.ampayne2.dropparty.commands.Start;
import me.ampayne2.dropparty.commands.Stop;
import me.ampayne2.dropparty.commands.Teleport;
import me.ampayne2.dropparty.commands.Update;
import me.ampayne2.dropparty.commands.Vote;
import me.ampayne2.dropparty.commands.list.ListChests;
import me.ampayne2.dropparty.commands.list.ListFireworkPoints;
import me.ampayne2.dropparty.commands.list.ListItemPoints;
import me.ampayne2.dropparty.commands.list.ListParties;
import me.ampayne2.dropparty.commands.list.ListPartySettings;
import me.ampayne2.dropparty.commands.remove.RemoveChest;
import me.ampayne2.dropparty.commands.remove.RemoveFireworkPoint;
import me.ampayne2.dropparty.commands.remove.RemoveItemPoint;
import me.ampayne2.dropparty.commands.set.SetChest;
import me.ampayne2.dropparty.commands.set.SetFireworkPoint;
import me.ampayne2.dropparty.commands.set.SetItemPoint;
import me.ampayne2.dropparty.commands.set.SetPartySetting;
import me.ampayne2.dropparty.commands.set.SetTeleport;
import me.ampayne2.dropparty.config.ConfigType;
import me.ampayne2.dropparty.message.DPMessage;
import me.ampayne2.dropparty.modes.PlayerModeController;
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
    private DPMetrics dpMetrics;
    private UpdateManager updateManager;

    @Override
    public void onEnable() {
        DefaultMessage.PREFIX.setMessage("&6[&5Drop Party&6] &7");
        DefaultMessage.RELOAD.setMessage("Reloaded Drop Party.");
        enableAmpLib();
        getConfigManager().registerConfigTypes(EnumSet.allOf(ConfigType.class));
        FileConfiguration config = getConfig();
        Messenger.PRIMARY_COLOR = ChatColor.valueOf(config.getString("colors.primary", "DARK_PURPLE"));
        Messenger.SECONDARY_COLOR = ChatColor.valueOf(config.getString("colors.secondary", "GRAY"));
        Messenger.HIGHLIGHT_COLOR = ChatColor.valueOf(config.getString("colors.highlights", "GOLD"));
        getMessenger().registerMessages(EnumSet.allOf(DPMessage.class));
        partyManager = new PartyManager(this);
        playerModeController = new PlayerModeController(this);
        dpMetrics = new DPMetrics(this);
        new DPListener(this);
        if (Bukkit.getPluginManager().isPluginEnabled("Votifier")) {
            new VoteListener(this);
        }
        updateManager = new UpdateManager(this, this.getFile());

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
                .addChildCommand(new Update(this))
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
        updateManager = null;
        HandlerList.unregisterAll(this);
        dpMetrics.destroyGraphs();
        dpMetrics = null;
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

    /**
     * Gets the drop party metrics wrapper.
     *
     * @return The DPMetrics instance.
     */
    public DPMetrics getDpMetrics() {
        return dpMetrics;
    }

    /**
     * Gets the update manager.
     *
     * @return The UpdateManager instance.
     */
    public UpdateManager getUpdateManager() {
        return updateManager;
    }
}
