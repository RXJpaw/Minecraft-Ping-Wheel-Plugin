package pw.rxj.bukkit.pingwheel.command.config;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pw.rxj.bukkit.pingwheel.Main;
import pw.rxj.bukkit.pingwheel.command.BakedCommand;
import pw.rxj.bukkit.pingwheel.config.Config;

public interface ConfigCommandDefaultChannel {
    static boolean handle(CommandSender sender, Command command, String label, String[] args, Object object) {
        BakedCommand.sendMessage(sender, "Current default channel mode:", Main.CONFIG.getDefaultChannel().toString());

        return true;
    }
    static boolean handleArg(CommandSender sender, Command command, String label, String[] args, Object object) {
        if(object instanceof String string) {
            Config.DefaultChannel mode = Config.DefaultChannel.from(string);

            Main.CONFIG.setDefaultChannel(mode);
            Main.CONFIG.write();

            BakedCommand.sendMessage(sender, "Set default channel mode to:", mode.toString());

            return true;
        }

        return false;
    }
}
