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

import org.mcstats.Metrics;

import java.io.IOException;

/**
 * A drop party wrapper for the Metrics instance.
 */
public class DPMetrics {
    private final DropParty dropParty;
    private Metrics metrics;

    /**
     * Creates a new DPMetrics.
     *
     * @param dropParty The DropParty instance.
     */
    public DPMetrics(final DropParty dropParty) {
        this.dropParty = dropParty;
        try {
            metrics = new Metrics(dropParty);
            metrics.start();
        } catch (IOException e) {
            dropParty.getMessenger().debug(e);
        }
    }

    /**
     * Creates the custom drop party graphs.
     */
    public void createGraphs() {
    }

    /**
     * Destroys the custom drop party graphs.
     */
    public void destroyGraphs() {
    }
}
