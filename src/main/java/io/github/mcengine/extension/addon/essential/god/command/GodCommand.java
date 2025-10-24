package io.github.mcengine.extension.addon.essential.god.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handles the {@code /god} command logic.
 * <p>
 * Current example implementation simply sends a confirmation message to the sender.
 * Real god-mode toggling logic (permissions, target lookup, state storage) should be
 * implemented where appropriate.
 */
public class GodCommand implements CommandExecutor {

    /**
     * Message sent to the command sender when the command executes successfully.
     */
    private static final String SUCCESS_MESSAGE = "§aGod command executed!";

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
        sender.sendMessage(SUCCESS_MESSAGE);
        return true;
    }
}
