package pw.rxj.bukkit.pingwheel.util;

import pw.rxj.bukkit.pingwheel.Main;

import java.util.logging.Logger;

public class PrefixedLogger {
    private final Logger logger;
    private final String prefix;

    public void info(String message){
        logger.info("[" + prefix + "] " + message);
    }
    public void warn(String message){
        logger.info("[" + prefix + "] " + message);
    }
    public void error(String message){
        logger.info("[" + prefix + "] " + message);
    }

    public PrefixedLogger(String prefix) {
        this.logger = Logger.getLogger(Main.PLUGIN_ID);
        this.prefix = prefix;
    }
}
