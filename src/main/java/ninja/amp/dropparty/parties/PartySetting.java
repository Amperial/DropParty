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
package ninja.amp.dropparty.parties;

import java.util.ArrayList;
import java.util.List;

/**
 * An enumeration of the settings of a drop party.
 */
public enum PartySetting {
    MAX_LENGTH("maxlength", "max length", Long.class, 6000L) {
        @Override
        public Object makeValid(Object value) {
            if ((long) value < 20) {
                return 20L;
            } else {
                return value;
            }
        }
    },
    ITEM_DELAY("itemdelay", "item delay", Long.class, 5L) {
        @Override
        public Object makeValid(Object value) {
            if ((long) value < 1) {
                return 1L;
            } else {
                return value;
            }
        }
    },
    MAX_STACK_SIZE("maxstacksize", "max stack size", Integer.class, 8) {
        @Override
        public Object makeValid(Object value) {
            if ((int) value < 1) {
                return 1;
            } else if ((int) value > 64) {
                return 64;
            } else {
                return value;
            }
        }
    },
    FIREWORK_AMOUNT("fireworkamount", "firework amount", Integer.class, 8) {
        @Override
        public Object makeValid(Object value) {
            if ((int) value < 0) {
                return 0;
            } else {
                return value;
            }
        }
    },
    FIREWORK_DELAY("fireworkdelay", "firework delay", Long.class, 2L) {
        @Override
        public Object makeValid(Object value) {
            if ((long) value < 1) {
                return 1L;
            } else {
                return value;
            }
        }
    },
    START_PERIODICALLY("startperiodically", "start periodically", Boolean.class, false),
    START_PERIOD("startperiod", "start period", Long.class, 144000L) {
        @Override
        public Object makeValid(Object value) {
            if ((long) value < 100) {
                return 100;
            } else {
                return value;
            }
        }
    },
    VOTE_TO_START("votetostart", "vote to start", Boolean.class, false),
    REQUIRED_VOTES("requiredvotes", "required votes", Integer.class, 50) {
        @Override
        public Object makeValid(Object value) {
            if ((int) value < 1) {
                return 1;
            } else {
                return value;
            }
        }
    },
    //VOTIFIER("votifier", "votifier", Boolean.class, false),
    EMPTY_CHEST("emptychest", "empty chest", Boolean.class, true);

    private final String name;
    private final String displayName;
    private final Class<?> type;
    private final Object defaultValue;
    private final static List<String> partySettingNames;

    PartySetting(String name, String displayName, Class<?> type, Object defaultValue) {
        this.name = name;
        this.displayName = displayName;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the name that represents the party setting.
     *
     * @return The party setting's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the display name of the party setting.
     *
     * @return The party setting's display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the class of the party setting's value.
     *
     * @return The party setting's value's class.
     */
    public Class<?> getType() {
        return type;
    }

    public Object getDefault() {
        return defaultValue;
    }

    /**
     * Modifies a value to be a valid value for the party setting.
     *
     * @param value The value.
     * @return The modified value.
     */
    public Object makeValid(Object value) {
        return value;
    }

    /**
     * Gets a party setting from its name.
     *
     * @param name The name.
     * @return The party setting.
     */
    public static PartySetting fromName(String name) {
        for (PartySetting partySetting : PartySetting.values()) {
            if (partySetting.getName().equalsIgnoreCase(name)) {
                return partySetting;
            }
        }
        return null;
    }

    /**
     * Gets the list of party setting names.
     *
     * @return The list of party setting names.
     */
    public static List<String> getPartySettingNames() {
        return partySettingNames;
    }

    static {
        partySettingNames = new ArrayList<>();
        for (PartySetting partySetting : PartySetting.values()) {
            partySettingNames.add(partySetting.getName());
        }
    }

}
