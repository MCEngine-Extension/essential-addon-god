package io.github.mcengine.extension.addon.essential.example.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handles the {@code /essentialaddonexample} command logic.
 */
public class EssentialAddOnCommand implements CommandExecutor {

    /**
     * Executes the {@code /essentialaddonexample} command.
     *
     * @param sender  The source of the command.
     * @param command The command that was executed.
     * @param label   The alias used.
     * @param args    The command arguments.
     * @return {@code true} if the command was handled successfully.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§aEssentialAddOn example command executed!");
        return true;
    }
}
