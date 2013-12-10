package me.ampayne2.dropparty.config;

public enum ConfigType {
    MESSAGE("Messages.yml"),
    PARTY("Parties.yml");

    private final String path;

    private ConfigType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
