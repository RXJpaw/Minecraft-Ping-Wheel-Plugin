package pw.rxj.bukkit.pingwheel.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {
    public abstract BakedCommand getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<CommandOption> options = this.getInstance().getOptions();
        CommandOption selectedOption = null;

        for (String arg : args) {
            if(options.isEmpty()) break;
            if(arg.isEmpty()) break;

            Optional<CommandOption> foundOption = options.stream().filter(option -> option.getName().equalsIgnoreCase(arg)).findFirst();
            if(foundOption.isEmpty()) break;

            selectedOption = foundOption.get();
            options = selectedOption.getOptions();
        }

        if(selectedOption == null) return false;

        return selectedOption.apply(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<CommandOption> options = this.getInstance().getOptions();

        boolean emptyArgExists = false;

        for (String arg : args) {
            if(options.isEmpty()) return List.of("");

            if(arg.isEmpty()) {
                if(!emptyArgExists) {
                    emptyArgExists = true;
                    continue;
                }

                return List.of("");
            }

            Optional<CommandOption> foundOption = options.stream().filter(option -> option.getName().equals(arg)).findFirst();
            if(foundOption.isEmpty() || args[args.length - 1].equals(arg)) break;

            options = foundOption.get().getOptions();
        }

        return CommandOption.toStringList(options);
    }
}
