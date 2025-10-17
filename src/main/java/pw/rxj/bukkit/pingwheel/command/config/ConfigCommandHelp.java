package pw.rxj.bukkit.pingwheel.command.config;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface ConfigCommandHelp {
    static boolean handle(CommandSender sender, Command command, String label, String[] args, Object object) {
        sender.sendMessage(
                ChatColor.DARK_GRAY + "[Ping-Wheel Plugin]",
                "/pw_config default_channel",
                ChatColor.GRAY + "(get the current default channel mode)",
                "/pw_config default_channel <mode_name>",
                ChatColor.GRAY + "(set the default channel mode)",
                "/pw_config player_tracking",
                ChatColor.GRAY + "(check if player tracking is currently enabled)",
                "/pw_config player_tracking true|false",
                ChatColor.GRAY + "(enable or disable the tracking of players)",
                "/pw_config regen_time",
                ChatColor.GRAY + "(get the current time to regenerate ping usages)",
                "/pw_config regen_time <milliseconds>",
                ChatColor.GRAY + "(set the time to regenerate ping usages)",
                "/pw_config rate_limit",
                ChatColor.GRAY + "(get the current rate limit for ping usages)",
                "/pw_config rate_limit <limit>",
                ChatColor.GRAY + "(set the rate limit for ping usages)"
        );

        return true;
    }

}
