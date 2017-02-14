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

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

/**
 * Drop party utilities.
 */
public final class DPUtils {

    private static final Random RANDOM = new Random();

    private DPUtils() {
    }

    /**
     * Converts a location into a string for easy storage in a config.
     *
     * @param location The location.
     * @return The string representation of the location.
     */
    public static String locationToString(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    /**
     * Converts a string representation of a location into a location.
     *
     * @param string The string representation of the location.
     * @return The location.
     */
    public static Location stringToLocation(String string) {
        try {
            String[] parts = string.split(",");
            if (parts.length == 6) {
                World world = Bukkit.getWorld(parts[0]);
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double z = Double.parseDouble(parts[3]);
                float yaw = Float.parseFloat(parts[4]);
                float pitch = Float.parseFloat(parts[5]);
                if (world != null) {
                    return new Location(world, x, y, z, yaw, pitch);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Clamps a value between a minimum and maximum value.
     *
     * @param value The value.
     * @param min   The minimum value.
     * @param max   The maximum value.
     * @return The clamped value.
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Gets a random enum value from an enum.
     *
     * @param clazz The enum's class.
     * @return The random enum value.
     */
    /**
     * Gets a random enum value from an enum.
     *
     * @param clazz The enum's class.
     * @param <T>   The enum's type.
     * @return The random enum value.
     */
    public static <T extends Enum> T randomEnum(Class<T> clazz) {
        return clazz.getEnumConstants()[RANDOM.nextInt(clazz.getEnumConstants().length)];
    }

    /**
     * Gets a random color.
     *
     * @return The random color.
     */
    public static Color randomColor() {
        switch (RANDOM.nextInt(17)) {
            case 0:
                return Color.AQUA;
            case 1:
                return Color.BLACK;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.FUCHSIA;
            case 4:
                return Color.GRAY;
            case 5:
                return Color.GREEN;
            case 6:
                return Color.LIME;
            case 7:
                return Color.MAROON;
            case 8:
                return Color.NAVY;
            case 9:
                return Color.OLIVE;
            case 10:
                return Color.ORANGE;
            case 11:
                return Color.PURPLE;
            case 12:
                return Color.RED;
            case 13:
                return Color.SILVER;
            case 14:
                return Color.TEAL;
            case 15:
                return Color.WHITE;
            case 16:
                return Color.YELLOW;
            default:
                return Color.WHITE;
        }
    }

}
