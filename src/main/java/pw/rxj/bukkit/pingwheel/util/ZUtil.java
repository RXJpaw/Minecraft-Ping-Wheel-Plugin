package pw.rxj.bukkit.pingwheel.util;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;

public class ZUtil {
    public static @Nullable Team getPlayerTeam(Player player) {
        return player.getScoreboard().getEntryTeam(player.getName());
    }

    public static boolean teamEquals(@Nullable Team t0, @Nullable Team t1) {
        return (t0 == null ? "" : t0.getName()).equals(t1 == null ? "" : t1.getName());
    }
}
