package fr.mrmicky.worldeditselectionvisualizer.event;

import com.sk89q.worldedit.regions.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Called when the WorldEdit selection of a player changed.
 */
@NullMarked
public class SelectionChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final @Nullable Region region;

    /**
     * Creates a new SelectionChangeEvent for the given player with the specified region.
     *
     * @param player the player whose selection changed
     * @param region the new region in the selection, or null if the selection is empty
     */
    public SelectionChangeEvent(Player player, @Nullable Region region) {
        this.player = Objects.requireNonNull(player, "player");
        this.region = region;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Returns the player whose selection changed.
     *
     * @return the player whose selection changed
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the region in the selection of the player.
     *
     * @return the region in the selection, or null if the selection is empty
     */
    public @Nullable Region getRegion() {
        return this.region;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
