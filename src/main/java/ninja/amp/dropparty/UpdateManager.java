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

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manages updating drop party. Checks for an update on startup in a separate thread so the main thread isn't delayed.
 */
public class UpdateManager {
    private final DropParty dropParty;
    private final File file;
    private AtomicBoolean updateAvailable = new AtomicBoolean(false);
    private String name = "";
    private Updater.ReleaseType type = null;
    private String version = "";
    private String link = "";

    /**
     * Creates a new UpdateManager.
     *
     * @param dropParty The DropParty instance.
     * @param file      The DropParty plugin file.
     */
    public UpdateManager(final DropParty dropParty, final File file) {
        this.dropParty = dropParty;
        this.file = file;
        if (dropParty.getConfig().getBoolean("update-notice", true)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Updater updater = new Updater(dropParty, 56611, file, Updater.UpdateType.NO_DOWNLOAD, false) {
                        @Override
                        public boolean shouldUpdate(String localString, String fullRemoteString) {
                            if (fullRemoteString.contains("-BETA")) {
                                return false; // Don't update to beta versions
                            }
                            String remoteString = fullRemoteString.split("-")[0];
                            String[] localVersion = localString.split("\\.");
                            String[] remoteVersion = remoteString.split("\\.");
                            for (int i = 0; i < Math.max(localVersion.length, remoteVersion.length); i++) {
                                int l = 0;
                                int r = 0;
                                if (localVersion.length > i) {
                                    l = Integer.parseInt(localVersion[i]);
                                }
                                if (remoteVersion.length > i) {
                                    r = Integer.parseInt(remoteVersion[i]);
                                }
                                if (r > l) {
                                    return true; // Remote version is newer, update available
                                } else if (r < l) {
                                    return false; // Local version is newer, update not available
                                }
                            }
                            return false; // Both versions are the same, update not available
                        }
                    };
                    if (updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE) {
                        updateAvailable.set(true);
                        name = updater.getLatestName();
                        type = updater.getLatestType();
                        version = updater.getLatestGameVersion();
                        link = updater.getLatestFileLink();
                    }
                }
            }).start();
        }
    }

    /**
     * Checks if an update is available.
     *
     * @return True if an update is available, else false.
     */
    public boolean updateAvailable() {
        return updateAvailable.get();
    }

    /**
     * Gets the update notice string.
     *
     * @return The update notice string.
     */
    public String getNotice() {
        return "An update is available: " + name + ", a " + type + " for " + version + " available at " + link;
    }

    /**
     * Updates drop party.
     */
    public void update() {
        Updater updater = new Updater(dropParty, 56611, file, Updater.UpdateType.NO_VERSION_CHECK, true);
    }
}
