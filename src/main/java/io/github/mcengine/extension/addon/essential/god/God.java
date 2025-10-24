package io.github.mcengine.extension.addon.essential.god;

import io.github.mcengine.api.core.MCEngineCoreApi;
import io.github.mcengine.api.core.extension.logger.MCEngineExtensionLogger;
import io.github.mcengine.api.essential.extension.addon.IMCEngineEssentialAddOn;

import io.github.mcengine.extension.addon.essential.god.command.GodCommand;
import io.github.mcengine.extension.addon.essential.god.listener.GodListener;
import io.github.mcengine.extension.addon.essential.god.tabcompleter.GodTabCompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Main class for the Essential AddOn "God" module.
 * <p>
 * Registers the {@code /god} command and related event listeners.
 * This class is instantiated and invoked by the MCEngine core plugin.
 */
public class God implements IMCEngineEssentialAddOn {

    /**
     * Custom extension logger for this module, with contextual labeling.
     */
    private MCEngineExtensionLogger logger;

    /**
     * Initializes the God add-on module.
     * Called automatically by the MCEngine core plugin.
     *
     * @param plugin The Bukkit plugin instance.
     */
    @Override
    public void onLoad(Plugin plugin) {
        // Initialize contextual logger once and keep it for later use.
        this.logger = new MCEngineExtensionLogger(plugin, "AddOn", "MCEngineGod");

        try {
            // Register event listener
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new GodListener(plugin, this.logger), plugin);

            // Reflectively access Bukkit's CommandMap
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            // Define the /god command
            Command godCommand = new Command("god") {

                /**
                 * Command handler for the {@code /god} command.
                 */
                private final GodCommand handler = new GodCommand();

                /**
                 * Tab-completer for the {@code /god} command.
                 */
                private final GodTabCompleter completer = new GodTabCompleter();

                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return handler.onCommand(sender, this, label, args);
                }

                @Override
                public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                    return completer.onTabComplete(sender, this, alias, args);
                }
            };

            godCommand.setDescription("Toggle god mode for yourself or another player.");
            godCommand.setUsage("/god [player]");

            // Dynamically register the /god command
            commandMap.register(plugin.getName().toLowerCase(), godCommand);

            this.logger.info("Enabled successfully.");
        } catch (Exception e) {
            this.logger.warning("Failed to initialize GodAddOn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Called when the God add-on module is disabled/unloaded.
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
        MCEngineCoreApi.setId("mcengine-essential-addon-god");
    }
}
