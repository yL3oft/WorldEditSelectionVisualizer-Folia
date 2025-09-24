package fr.mrmicky.worldeditselectionvisualizer.compat;

import fr.mrmicky.worldeditselectionvisualizer.WorldEditSelectionVisualizer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class FoliaUtils {

    private final WorldEditSelectionVisualizer plugin;

    public FoliaUtils(WorldEditSelectionVisualizer plugin) {
        this.plugin = plugin;
    }

    public void teleportPlayer(Player player, Location location) {
        if(plugin.getCompatibilityHelper().folia) {
            try {
                Method teleportAsyncMethod = Player.class.getMethod("teleportAsync", Location.class);
                teleportAsyncMethod.invoke(player, location);
                return;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Unable to teleport player to home", e);
            }
        }

        player.teleport(location);
    }

}
