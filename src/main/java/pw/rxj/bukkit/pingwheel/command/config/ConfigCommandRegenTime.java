package pw.rxj.bukkit.pingwheel.command.config;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pw.rxj.bukkit.pingwheel.Main;
import pw.rxj.bukkit.pingwheel.command.BakedCommand;

public interface ConfigCommandRegenTime {
    static boolean handle(CommandSender sender, Command command, String label, String[] args, Object object) {
        String lastArg = args[args.length - 1];
        final int outcome;

        try {
            outcome = Integer.parseInt(lastArg);
        } catch (NumberFormatException e) {
            BakedCommand.sendMessage(sender, "Current time to regenerate:", Integer.toString(Main.CONFIG.getRateLimitRegen()));

            return true;
        }

        Main.CONFIG.setRateLimitRegen(outcome);
        Main.CONFIG.write();

        BakedCommand.sendMessage(sender, "Set time to regenerate to:", Integer.toString(Main.CONFIG.getRateLimitRegen()));

        return true;
    }
}
