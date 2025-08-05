package fr.mrmicky.worldeditselectionvisualizer.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * Called when a player toggles their WorldEdit selection visualization.
 */
@NullMarked
public class VisualizationToggleEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final boolean enabled;

    /**
     * Creates a new VisualizationToggleEvent for the given player with the specified enabled state.
     *
     * @param player  the player whose visualization was toggled
     * @param enabled true if the visualization is enabled, false if it is disabled
     */
    public VisualizationToggleEvent(Player player, boolean enabled) {
        this.player = Objects.requireNonNull(player, "player");
        this.enabled = enabled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Returns the player whose visualization was toggled.
     *
     * @return the player whose visualization was toggled
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns whether the visualization is enabled or disabled.
     *
     * @return true if the visualization is enabled, false if it is disabled
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
