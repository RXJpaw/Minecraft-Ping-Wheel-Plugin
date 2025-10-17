package pw.rxj.bukkit.pingwheel.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pw.rxj.bukkit.pingwheel.util.QuintupleFunction;

import java.util.List;

public class CommandOption {
    private final QuintupleFunction<CommandSender, Command, String, String[], Object, Boolean> function;

    private final Type type;
    private final String name;
    private final List<CommandOption> options;

    private CommandOption(Type type, String name, QuintupleFunction<CommandSender, Command, String, String[], Object, Boolean> function, CommandOption... options) {
        this.type = type;
        this.name = name;
        this.function = function;
        this.options = List.of(options);
    }

    public static CommandOption string(String name, QuintupleFunction<CommandSender, Command, String, String[], Object, Boolean> function, CommandOption... options) {
        return new CommandOption(Type.STRING, name, function, options);
    }
    public static CommandOption bool(String name, QuintupleFunction<CommandSender, Command, String, String[], Object, Boolean> function) {
        return new CommandOption(Type.BOOLEAN, name, function, string("true", function), string("false", function));
    }
    public static CommandOption number(String name, QuintupleFunction<CommandSender, Command, String, String[], Object, Boolean> function) {
        return new CommandOption(Type.NUMBER, name, function);
    }

    public Type getType() {
        return type;
    }
    public String getName() {
        return this.name;
    }
    public List<CommandOption> getOptions() {
        return this.options;
    }

    public boolean apply(CommandSender sender, Command command, String label, String[] args) {
        try {
            return this.function.apply(sender, command, label, args, this.name);
        } catch (Exception e) {
            return false;
        }
    }

    public static List<String> toStringList(List<CommandOption> options) {
        return options.stream().map(CommandOption::getName).toList();
    }

    public enum Type {
        STRING,
        BOOLEAN,
        NUMBER
    }
}
