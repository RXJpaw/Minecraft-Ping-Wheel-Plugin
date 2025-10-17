package pw.rxj.bukkit.pingwheel.command.config;

import pw.rxj.bukkit.pingwheel.command.AbstractCommand;
import pw.rxj.bukkit.pingwheel.command.BakedCommand;
import pw.rxj.bukkit.pingwheel.command.CommandOption;

public class ConfigCommand extends AbstractCommand {
    public static BakedCommand INSTANCE = BakedCommand.bake("pw_config")
            .withOption(CommandOption.string("help", ConfigCommandHelp::handle))
            .withOption(CommandOption.string("default_channel", ConfigCommandDefaultChannel::handle,
                    CommandOption.string("auto", ConfigCommandDefaultChannel::handleArg),
                    CommandOption.string("disabled", ConfigCommandDefaultChannel::handleArg),
                    CommandOption.string("global", ConfigCommandDefaultChannel::handleArg),
                    CommandOption.string("team_only", ConfigCommandDefaultChannel::handleArg)
            ))
            .withOption(CommandOption.bool("player_tracking", ConfigCommandPlayerTracking::handle))
            .withOption(CommandOption.number("rate_limit", ConfigCommandRateLimit::handle))
            .withOption(CommandOption.number("regen_time", ConfigCommandRegenTime::handle));

    @Override
    public BakedCommand getInstance() {
        return INSTANCE;
    }
}
