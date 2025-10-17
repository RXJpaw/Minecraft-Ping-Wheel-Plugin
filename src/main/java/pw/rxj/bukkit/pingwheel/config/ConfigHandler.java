package pw.rxj.bukkit.pingwheel.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pw.rxj.bukkit.pingwheel.Main;
import pw.rxj.bukkit.pingwheel.api.RateLimiter;

public class ConfigHandler {
    private final FileConfiguration pluginConfig;
    private final JavaPlugin plugin;
    private final Config config;
    private final Gson gson;

    private ConfigHandler(JavaPlugin plugin, Gson gson) {
        this.pluginConfig = plugin.getConfig();
        this.plugin = plugin;
        this.config = Config.empty();
        this.gson = gson;
    }
    public static ConfigHandler of(JavaPlugin plugin, Gson gson) {
        return new ConfigHandler(plugin, gson);
    }
    public static ConfigHandler bake(JavaPlugin plugin) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return of(plugin, gson);
    }
    public ConfigHandler copy() {
        return ConfigHandler.of(this.plugin, this.gson);
    }

    protected FileConfiguration getPluginConfig() {
        return this.pluginConfig;
    }

    protected Config getConfig() {
        return this.config;
    }

    // ***
    public void setDefaultChannel(Config.DefaultChannel mode) {
        this.config.SERVER.defaultChannel = mode;
    }
    public Config.DefaultChannel getDefaultChannel() {
        return this.config.SERVER.defaultChannel;
    }

    public void setPlayerTrackingAllowed(boolean allowed) {
        this.config.SERVER.allowPlayerTracking = allowed;
    }
    public boolean isPlayerTrackingAllowed() {
        return this.config.SERVER.allowPlayerTracking;
    }

    public void setRateLimit(int amount) {
        this.config.SERVER.rateLimit = amount;
    }
    public int getRateLimit() {
        return this.config.SERVER.rateLimit;
    }

    public void setRateLimitRegen(int amount) {
        this.config.SERVER.rateLimitRegen = amount;
    }
    public int getRateLimitRegen() {
        return this.config.SERVER.rateLimitRegen;
    }
    // ***

    public void read() {
        this.config.SERVER.defaultChannel = Config.DefaultChannel.from(this.pluginConfig.getString("default_channel", this.config.SERVER.defaultChannel.toString()));
        this.config.SERVER.allowPlayerTracking = this.pluginConfig.getBoolean("player_tracking", this.config.SERVER.allowPlayerTracking);
        this.config.SERVER.rateLimit = this.pluginConfig.getInt("rate_limit", this.config.SERVER.rateLimit);
        this.config.SERVER.rateLimitRegen = this.pluginConfig.getInt("regen_time", this.config.SERVER.rateLimitRegen);

        Main.LOGGER.info("Server config read from disk.");

        this.write();
    }
    public void write() {
        this.pluginConfig.set("default_channel", this.config.SERVER.defaultChannel.toString());
        this.pluginConfig.set("player_tracking", this.config.SERVER.allowPlayerTracking);
        this.pluginConfig.set("rate_limit", this.config.SERVER.rateLimit);
        this.pluginConfig.set("regen_time", this.config.SERVER.rateLimitRegen);

        RateLimiter.setRates(this.getRateLimitRegen(), this.getRateLimit());

        this.plugin.saveConfig();

        Main.LOGGER.info("Server config written to disk.");
    }
}
