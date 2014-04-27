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
package me.ampayne2.dropparty.message;

import me.ampayne2.dropparty.DropParty;
import me.ampayne2.dropparty.config.ConfigType;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages drop party message sending, logging, and debugging.
 */
public class Messenger {
    private final boolean debug;
    private final Logger log;
    private final Map<String, String> messages = new HashMap<>();
    private final Map<Class<?>, RecipientHandler> recipientHandlers = new HashMap<>();

    public static ChatColor PRIMARY_COLOR;
    public static ChatColor SECONDARY_COLOR;
    public static ChatColor HIGHLIGHT_COLOR;


    /**
     * Creates a new message manager.
     *
     * @param dropParty The DropParty instance.
     */
    public Messenger(DropParty dropParty) {
        debug = dropParty.getConfig().getBoolean("debug", false);
        log = dropParty.getLogger();
        FileConfiguration messageConfig = dropParty.getConfigManager().getConfig(ConfigType.MESSAGE);
        for (DPMessage message : DPMessage.class.getEnumConstants()) {
            messageConfig.addDefault(message.getPath(), message.getDefault());
        }
        messageConfig.options().copyDefaults(true);
        dropParty.getConfigManager().getConfigAccessor(ConfigType.MESSAGE).saveConfig();
        for (DPMessage message : DPMessage.class.getEnumConstants()) {
            message.setMessage(ChatColor.translateAlternateColorCodes('&', messageConfig.getString(message.getPath())));
        }
        FileConfiguration mainConfig = dropParty.getConfig();
        try {
            PRIMARY_COLOR = ChatColor.valueOf(mainConfig.getString("colors.primary", "DARK_PURPLE"));
        } catch (IllegalArgumentException e) {
            PRIMARY_COLOR = ChatColor.DARK_PURPLE;
        }
        try {
            SECONDARY_COLOR = ChatColor.valueOf(mainConfig.getString("colors.secondary", "GRAY"));
        } catch (IllegalArgumentException e) {
            SECONDARY_COLOR = ChatColor.GRAY;
        }
        try {
            HIGHLIGHT_COLOR = ChatColor.valueOf(mainConfig.getString("colors.highlights", "GOLD"));
        } catch (IllegalArgumentException e) {
            HIGHLIGHT_COLOR = ChatColor.GOLD;
        }
    }

    /**
     * Registers a recipient with a RecipientHandler.
     *
     * @param recipientClass   The recipient's class.
     * @param recipientHandler The RecipientHandler.
     * @return The Messenger instance.
     */
    public Messenger registerRecipient(Class recipientClass, RecipientHandler recipientHandler) {
        recipientHandlers.put(recipientClass, recipientHandler);
        return this;
    }

    /**
     * Sends a {@link me.ampayne2.dropparty.message.DPMessage} to a recipient.
     *
     * @param recipient The recipient of the message.
     * @param message   The {@link me.ampayne2.dropparty.message.DPMessage}.
     * @param replace   Strings to replace any occurences of %s in the message with.
     * @return True if the message was sent, else false.
     */
    public boolean sendMessage(Object recipient, DPMessage message, String... replace) {
        return sendRawMessage(recipient, DPMessage.PREFIX + (replace == null ? message.getMessage() : String.format(message.getMessage(), (Object[]) replace)));
    }

    /**
     * Sends a raw message to a recipient.
     *
     * @param recipient The recipient of the message. Type of recipient must be registered.
     * @param message   The message.
     * @return True if the message was sent, else false.
     */
    public boolean sendRawMessage(Object recipient, String message) {
        if (recipient != null && message != null) {
            for (Class<?> recipientClass : recipientHandlers.keySet()) {
                if (recipientClass.isAssignableFrom(recipient.getClass())) {
                    recipientHandlers.get(recipientClass).sendMessage(recipient, message);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Logs one or more messages to the console.
     *
     * @param level    the level to log the message at.
     * @param messages the message(s) to log.
     */
    public void log(Level level, String... messages) {
        for (String message : messages) {
            log.log(level, message);
        }
    }

    /**
     * Decides whether or not to print the stack trace of an exception.
     *
     * @param e the exception to debug.
     */
    public void debug(Exception e) {
        if (debug) {
            log.severe("");
            log.severe("Internal error!");
            log.severe("If this bug hasn't been reported please open a ticket at https://github.com/ampayne2/DropParty/issues");
            log.severe("Include the following into your bug report:");
            log.severe(" ======= SNIP HERE =======");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            for (String l : sw.toString().replace("\r", "").split("\n")) {
                log.severe(l);
            }
            pw.close();
            try {
                sw.close();
            } catch (IOException e1) {
                log.log(Level.SEVERE, "An error occured in debugging an exception", e);
            }
            log.severe(" ======= SNIP HERE =======");
            log.severe("");
        }
    }

    /**
     * Decides whether or not to print a debug message.
     *
     * @param message the message to debug.
     */
    public void debug(String message) {
        if (debug) {
            log.log(Level.INFO, message);
        }
    }

    /**
     * Gets the logger.
     *
     * @return The logger.
     */
    public Logger getLogger() {
        return log;
    }
}
