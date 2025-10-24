package io.github.mcengine.extension.addon.essential.example;

import io.github.mcengine.api.core.MCEngineCoreApi;
import io.github.mcengine.api.core.extension.logger.MCEngineExtensionLogger;
import io.github.mcengine.api.essential.extension.addon.IMCEngineEssentialAddOn;

import io.github.mcengine.extension.addon.essential.example.command.EssentialAddOnCommand;
import io.github.mcengine.extension.addon.essential.example.listener.EssentialAddOnListener;
import io.github.mcengine.extension.addon.essential.example.tabcompleter.EssentialAddOnTabCompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Main class for the Essential AddOn example module.
 * <p>
 * Registers the {@code /essentialaddonexample} command and related event listeners.
 */
public class ExampleEssentialAddOn implements IMCEngineEssentialAddOn {

    /**
     * Custom extension logger for this module, with contextual labeling.
     */
    private MCEngineExtensionLogger logger;

    /**
     * Initializes the Essential AddOn example module.
     * Called automatically by the MCEngine core plugin.
     *
     * @param plugin The Bukkit plugin instance.
     */
    @Override
    public void onLoad(Plugin plugin) {
        // Initialize contextual logger once and keep it for later use.
        this.logger = new MCEngineExtensionLogger(plugin, "AddOn", "EssentialExampleAddOn");

        try {
            // Register event listener
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new EssentialAddOnListener(plugin, this.logger), plugin);

            // Reflectively access Bukkit's CommandMap
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            // Define the /essentialaddonexample command
            Command essentialAddOnExampleCommand = new Command("essentialaddonexample") {

                /**
                 * Handles command execution for /essentialaddonexample.
                 */
                private final EssentialAddOnCommand handler = new EssentialAddOnCommand();

                /**
                 * Handles tab-completion for /essentialaddonexample.
                 */
                private final EssentialAddOnTabCompleter completer = new EssentialAddOnTabCompleter();

                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return handler.onCommand(sender, this, label, args);
                }

                @Override
                public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                    return completer.onTabComplete(sender, this, alias, args);
                }
            };

            essentialAddOnExampleCommand.setDescription("Essential AddOn example command.");
            essentialAddOnExampleCommand.setUsage("/essentialaddonexample");

            // Dynamically register the /essentialaddonexample command
            commandMap.register(plugin.getName().toLowerCase(), essentialAddOnExampleCommand);

            this.logger.info("Enabled successfully.");
        } catch (Exception e) {
            this.logger.warning("Failed to initialize ExampleEssentialAddOn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Called when the Essential AddOn example module is disabled/unloaded.
     *
     * @param plugin The Bukkit plugin instance.
     */
    @Override
    public void onDisload(Plugin plugin) {
        if (this.logger != null) {
            this.logger.info("Disabled.");
        }
    }

    /**
     * Sets the unique ID for this module.
     *
     * @param id the assigned identifier (ignored; a fixed ID is used for consistency)
     */
    @Override
    public void setId(String id) {
        MCEngineCoreApi.setId("mcengine-essential-addon-example");
    }
}
