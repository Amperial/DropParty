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
package ninja.amp.dropparty.modes;

/**
 * Types of player selection and removal modes.
 */
public enum PlayerMode {
    SETTING_CHESTS("chest selection"),
    SETTING_ITEM_POINTS("item point selection"),
    SETTING_FIREWORK_POINTS("firework point selection"),
    REMOVING_CHESTS("chest removal"),
    REMOVING_ITEM_POINTS("item point removal"),
    REMOVING_FIREWORK_POINTS(" firework point removal");

    private final String name;

    PlayerMode(String name) {
        this.name = name;
    }

    /**
     * Gets the name that represents the mode.
     *
     * @return The name of the mode.
     */
    public String getName() {
        return name;
    }

}
