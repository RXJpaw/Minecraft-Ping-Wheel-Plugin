package pw.rxj.bukkit.pingwheel.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class BakedCommand {
    private final String name;
    private final List<CommandOption> options;

    private BakedCommand(String name) {
        this.name = name;
        this.options = new ArrayList<>();
    }

    public static BakedCommand bake(String name) {
        return new BakedCommand(name);
    }

    public BakedCommand withOption(CommandOption option) {
        this.options.add(option);

        return this;
    }

    public String getName() {
        return this.name;
    }
    public List<CommandOption> getOptions() {
        return this.options;
    }

    public static void sendMessage(CommandSender sender, String p0, String p1) {
        sender.sendMessage(ChatColor.DARK_GRAY + "[Ping-Wheel Plugin]" + ChatColor.RESET + " " + ChatColor.RESET + p0 + ChatColor.RESET + " " + ChatColor.YELLOW + p1);
    }
}
