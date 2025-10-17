package pw.rxj.bukkit.pingwheel.command.config;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pw.rxj.bukkit.pingwheel.Main;
import pw.rxj.bukkit.pingwheel.command.BakedCommand;

public interface ConfigCommandPlayerTracking {
    static boolean handle(CommandSender sender, Command command, String label, String[] args, Object object) {
        String lastArg = args[args.length - 1];
        final boolean outcome;

        if(lastArg.equalsIgnoreCase("true")) {
            outcome = true;
        } else if(lastArg.equalsIgnoreCase("false")) {
            outcome = false;
        } else {
            BakedCommand.sendMessage(sender, "Player tracking is currently:", Boolean.toString(Main.CONFIG.isPlayerTrackingAllowed()));

            return true;
        }

        Main.CONFIG.setPlayerTrackingAllowed(outcome);
        Main.CONFIG.write();

        BakedCommand.sendMessage(sender, "Player tracking is now:", Boolean.toString(Main.CONFIG.isPlayerTrackingAllowed()));

        return true;
    }
}
