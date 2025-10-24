package io.github.mcengine.extension.addon.essential.god.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles the {@code /god} command logic.
 * <p>
 * Supports:
 * <ul>
 *     <li>{@code /god} - toggle sender's god mode (requires {@code mcengine.essential.god.use})</li>
 *     <li>{@code /god &lt;player&gt;} - toggle another player's god mode (requires {@code mcengine.essential.god.toggle.players})</li>
 * </ul>
 *
 * This class also exposes static helper methods so other classes (such as listeners) may
 * query god-mode state.
 */
public class GodCommand implements CommandExecutor {

    /**
     * Permission required to toggle your own god mode via {@code /god}.
     */
    private static final String PERM_SELF = "mcengine.essential.god.use";

    /**
     * Permission required to toggle another player's god mode via {@code /god &lt;player&gt;}.
     */
    private static final String PERM_OTHERS = "mcengine.essential.god.toggle.players";

    /**
     * Message shown when a player attempts a command they don't have permission for.
     */
    private static final String NO_PERMISSION_MESSAGE = ChatColor.RED + "You do not have permission to use that command.";

    /**
     * Message template for toggling god mode on a player (self-target).
     */
    private static final String GOD_TOGGLED_SELF = ChatColor.GREEN + "God mode %s for you.";

    /**
     * Message template for toggling god mode on another player (sender feedback).
     */
    private static final String GOD_TOGGLED_OTHER = ChatColor.GREEN + "God mode %s for player %s.";

    /**
     * Message shown when a target player cannot be found.
     */
    private static final String PLAYER_NOT_FOUND = ChatColor.RED + "Player not found or not online.";

    /**
     * Thread-safe set that contains UUIDs of players currently in god mode.
     * <p>
     * Managed via public helper methods {@code isGod}, {@code setGod}, and {@code toggleGod}.
     */
    private static final Set<UUID> godModePlayers = ConcurrentHashMap.newKeySet();

    /**
     * Executes the {@code /god} command.
     *
     * @param sender  The source of the command.
     * @param command The command that was executed.
     * @param label   The alias used.
     * @param args    The command arguments.
     * @return {@code true} if the command was handled successfully.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /god
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players may toggle their own god mode. Use /god <player> to toggle others.");
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission(PERM_SELF)) {
                player.sendMessage(NO_PERMISSION_MESSAGE);
                return true;
            }

            boolean newState = toggleGod(player);
            player.sendMessage(String.format(GOD_TOGGLED_SELF, newState ? "enabled" : "disabled"));
            return true;
        }

        // /god <player>
        if (args.length == 1) {
            if (!sender.hasPermission(PERM_OTHERS)) {
                sender.sendMessage(NO_PERMISSION_MESSAGE);
                return true;
            }

            String targetName = args[0];
            Player target = Bukkit.getPlayerExact(targetName);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(PLAYER_NOT_FOUND);
                return true;
            }

            boolean newState = toggleGod(target);

            // Feedback to sender
            String senderMessage = String.format(GOD_TOGGLED_OTHER, newState ? "enabled" : "disabled", target.getName());
            sender.sendMessage(senderMessage);

            // Inform the target (unless sender toggled themself above)
            if (!(sender instanceof Player && ((Player) sender).getUniqueId().equals(target.getUniqueId()))) {
                target.sendMessage(String.format(GOD_TOGGLED_SELF, newState ? "enabled" : "disabled"));
            }
            return true;
        }

        // Too many args -> usage
        sender.sendMessage(ChatColor.YELLOW + "Usage: /god [player]");
        return true;
    }

    /**
     * Returns true if the given player is currently in god mode.
     *
     * @param player the player to check
     * @return true when player has god mode enabled
     */
    public static boolean isGod(Player player) {
        return godModePlayers.contains(player.getUniqueId());
    }

    /**
     * Sets the god mode state for a player.
     *
     * @param player the player to modify
     * @param state  true to enable god mode, false to disable
     */
    public static void setGod(Player player, boolean state) {
        if (state) {
            godModePlayers.add(player.getUniqueId());
        } else {
            godModePlayers.remove(player.getUniqueId());
        }
    }

    /**
     * Toggles the god mode state for a player.
     *
     * @param player the player to toggle
     * @return the new state (true if enabled, false if disabled)
     */
    public static boolean toggleGod(Player player) {
        UUID uid = player.getUniqueId();
        if (godModePlayers.contains(uid)) {
            godModePlayers.remove(uid);
            return false;
        } else {
            godModePlayers.add(uid);
            return true;
        }
    }
}
