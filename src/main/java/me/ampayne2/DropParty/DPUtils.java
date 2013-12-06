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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class DPUtils {
    private DPUtils() {
    }

    public static String locationToString(Location location) {
        return new StringBuilder()
                .append(location.getWorld().getName()).append(",")
                .append(location.getX()).append(",")
                .append(location.getY()).append(",")
                .append(location.getZ()).append(",")
                .append(location.getYaw()).append(",")
                .append(location.getPitch()).append(",").toString();
    }

    public static Location stringToLocation(String string) {
        try {
            String[] parts = string.split(",", 6);
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
}
