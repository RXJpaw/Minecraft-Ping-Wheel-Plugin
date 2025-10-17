package pw.rxj.bukkit.pingwheel.config;

import lombok.EqualsAndHashCode;

import java.util.Objects;

public class Config {
    public final Config.Server SERVER = new Config.Server();

    private Config() { }
    protected static Config empty() {
        return new Config();
    }

    protected void override(Config newConfig) {
        this.overrideServer(newConfig.SERVER);
    }
    protected void overrideServer(Config.Server newServerConfig) {
        this.SERVER.defaultChannel = newServerConfig.defaultChannel;
        this.SERVER.allowPlayerTracking = newServerConfig.allowPlayerTracking;
        this.SERVER.rateLimit = newServerConfig.rateLimit;
        this.SERVER.rateLimitRegen = newServerConfig.rateLimitRegen;
    }

    @Override
    public int hashCode() {
        return Objects.hash(SERVER);
    }

    @EqualsAndHashCode
    public static class Server {
        private Server() {}

        public DefaultChannel defaultChannel = DefaultChannel.AUTO;
        public boolean allowPlayerTracking = true;
        public int rateLimit = 5;
        public int rateLimitRegen = 1000;
    }

    public enum DefaultChannel {
        AUTO("auto"),
        DISABLED("disabled"),
        GLOBAL("global"),
        TEAM_ONLY("team_only");

        private final String mode;

        private DefaultChannel(String mode) {
            this.mode = mode;
        }

        public static DefaultChannel from(String name){
            for (DefaultChannel defaultChannel : DefaultChannel.values()) {
                if(defaultChannel.mode.equalsIgnoreCase(name)){
                    return defaultChannel;
                }
            }

            return AUTO;
        }
    }
}
