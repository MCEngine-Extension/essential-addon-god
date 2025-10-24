package io.github.mcengine.extension.addon.essential.god.tabcompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tab-completer for the {@code /god} command.
 * <p>
 * Suggestions:
 * <ul>
 *     <li>When no arguments: suggest online player names (for the first argument)</li>
 *     <li>Otherwise: no suggestions</li>
 * </ul>
 */
public class GodTabCompleter implements TabCompleter {

    /**
     * Provides tab-completion suggestions for {@code /god}.
     *
     * @param sender  The source of the command.
     * @param command The command object.
     * @param alias   The alias used to execute the command.
     * @param args    The current command arguments.
     * @return A list of tab-completion suggestions.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Suggest online player names for the first argument.
        if (args.length == 1) {
            List<String> names = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(p -> names.add(p.getName()));
            return names;
        }
        return Collections.emptyList();
    }
}
