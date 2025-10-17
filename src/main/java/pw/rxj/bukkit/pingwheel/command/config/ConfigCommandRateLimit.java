package pw.rxj.bukkit.pingwheel.command.config;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pw.rxj.bukkit.pingwheel.Main;
import pw.rxj.bukkit.pingwheel.command.BakedCommand;

public interface ConfigCommandRateLimit {
    static boolean handle(CommandSender sender, Command command, String label, String[] args, Object object) {
        String lastArg = args[args.length - 1];
        final int outcome;

        try {
            outcome = Integer.parseInt(lastArg);
        } catch (NumberFormatException e) {
            BakedCommand.sendMessage(sender, "Current rate limit:", Integer.toString(Main.CONFIG.getRateLimit()));

            return true;
        }

        Main.CONFIG.setRateLimit(outcome);
        Main.CONFIG.write();

        BakedCommand.sendMessage(sender, "Set rate limit to:", Integer.toString(Main.CONFIG.getRateLimit()));

        return true;
    }
}
