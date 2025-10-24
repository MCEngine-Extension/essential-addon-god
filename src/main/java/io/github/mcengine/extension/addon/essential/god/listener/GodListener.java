package io.github.mcengine.extension.addon.essential.god.listener;

import io.github.mcengine.api.core.extension.logger.MCEngineExtensionLogger;
import io.github.mcengine.extension.addon.essential.god.command.GodCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

/**
 * Event listener for the God add-on module.
 * <p>
 * - Sends a welcome message on join.
 * - Logs when players quit.
 * - Prevents damage to players while they are in god mode.
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

    /**
     * Cancels damage for players who have god mode enabled.
     *
     * @param event The damage event.
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if (GodCommand.isGod(p)) {
                event.setCancelled(true);
            }
        }
    }
}
