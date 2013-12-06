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
package me.ampayne2.dropparty;

import me.ampayne2.dropparty.command.CommandController;
import me.ampayne2.dropparty.config.ConfigManager;
import me.ampayne2.dropparty.message.Message;
import me.ampayne2.dropparty.message.RecipientHandler;
import me.ampayne2.dropparty.modes.PlayerModeController;
import me.ampayne2.dropparty.parties.PartyManager;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the Drop Party plugin.
 */
public class DropParty extends JavaPlugin {
    private ConfigManager configManager;
    private Message message;
    private PartyManager partyManager;
    private PlayerModeController playerModeController;
    private CommandController commandController;
    private DPMetrics dpMetrics;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        message = new Message(this)
                .registerRecipient(CommandSender.class, new RecipientHandler() {
                    @Override
                    public void sendMessage(Object recipient, String message) {
                        ((CommandSender) recipient).sendMessage(message);
                    }})
                .registerRecipient(Server.class, new RecipientHandler() {
                    @Override
                    public void sendMessage(Object recipient, String message) {
                        ((Server) recipient).broadcastMessage(message);
                    }});
        partyManager = new PartyManager(this);
        playerModeController = new PlayerModeController(this);
        commandController = new CommandController(this);
        getCommand("dropparty").setExecutor(commandController);
        dpMetrics = new DPMetrics(this);
    }

    @Override
    public void onDisable() {
        dpMetrics.destroyGraphs();
        dpMetrics = null;
        commandController = null;
        playerModeController.clearModes();
        playerModeController = null;
        partyManager.stopParties();
        partyManager = null;
        message = null;
        configManager = null;
    }

    /**
     * Gets the drop party config manager.
     *
     * @return The ConfigManager instance.
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Gets the drop party message manager.
     *
     * @return The Message instance.
     */
    public Message getMessage() {
        return message;
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
     * Gets the drop party command controller.
     *
     * @return The CommandController instance.
     */
    public CommandController getCommandController() {
        return commandController;
    }

    /**
     * Gets the drop party metrics wrapper.
     *
     * @return The DPMetrics instance.
     */
    public DPMetrics getDpMetrics() {
        return dpMetrics;
    }
}
