package io.github.mcengine.extension.addon.essential.god.listener;

import io.github.mcengine.api.core.extension.logger.MCEngineExtensionLogger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

/**
 * Example event listener for the God add-on module.
 * <p>
 * Sends a welcome message on join and logs on quit using the add-on's logger.
 */
public class GodListener implements Listener {

    /**
     * The plugin instance used by this listener.
     */
    private final Plugin plugin;

    /**
     * Custom extension logger for this listener, with contextual labeling.
     */
    private final MCEngineExtensionLogger logger;

    /**
     * Creates a new {@link GodListener}.
     *
     * @param plugin The plugin instance.
     * @param logger The custom extension logger instance.
     */
    public GodListener(Plugin plugin, MCEngineExtensionLogger logger) {
        this.plugin = plugin;
        this.logger = logger;
    }
}
