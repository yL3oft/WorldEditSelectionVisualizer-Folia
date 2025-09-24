package fr.mrmicky.worldeditselectionvisualizer.compat;

import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.item.ItemType;
import com.sk89q.worldedit.world.item.ItemTypes;
import fr.mrmicky.worldeditselectionvisualizer.WorldEditSelectionVisualizer;
import fr.mrmicky.worldeditselectionvisualizer.compat.v6.ClipboardAdapter6;
import fr.mrmicky.worldeditselectionvisualizer.compat.v6.RegionAdapter6;
import fr.mrmicky.worldeditselectionvisualizer.compat.v7.ClipboardAdapter7;
import fr.mrmicky.worldeditselectionvisualizer.compat.v7.RegionAdapter7;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Helper class to help support multiples Spigot and WorldEdit versions
 */
@NullMarked
public class CompatibilityHelper {

    private final WorldEditSelectionVisualizer plugin;
    private final SchedulerUtils scheduler;
    private final FoliaUtils foliaUtils;

    private final boolean supportOffHand = isOffhandSupported();
    private final boolean supportActionBar = isActionBarSupported();
    private final boolean worldEdit7 = isWorldEdit7();
    public final boolean folia = isFolia();

    private @Nullable Material selectionItem;

    public CompatibilityHelper(WorldEditSelectionVisualizer plugin) {
        this.plugin = plugin;
        this.scheduler = new SchedulerUtils(plugin);
        this.foliaUtils = new FoliaUtils(plugin);

        plugin.getLogger().info("Using WorldEdit " + getWorldEditVersion() + " api");

        init();
    }

    public void init() {
        try {
            Field field = LocalConfiguration.class.getField("wandItem");
            LocalConfiguration config = WorldEdit.getInstance().getConfiguration();

            if (field.getType() == int.class) { // WorldEdit 6
                String itemId = Integer.toString(field.getInt(config));
                this.selectionItem = Material.matchMaterial(itemId);

                return;
            }

            if (field.getType() == String.class) { // WorldEdit 7
                ItemType itemType = ItemTypes.get((String) field.get(config));
                this.selectionItem = itemType != null ? BukkitAdapter.adapt(itemType) : null;

                return;
            }

            this.plugin.getLogger().warning("Unsupported item type in WorldEdit config, try to update WorldEdit.");
        } catch (ReflectiveOperationException e) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to get WorldEdit wand item, try to update WorldEdit.", e);
        }
    }

    public RegionAdapter adaptRegion(Region region) {
        return this.worldEdit7 ? new RegionAdapter7(region) : new RegionAdapter6(region);
    }

    public ClipboardAdapter adaptClipboard(Clipboard clipboard) {
        return this.worldEdit7 ? new ClipboardAdapter7(clipboard) : new ClipboardAdapter6(clipboard);
    }

    public int getWorldEditVersion() {
        return this.worldEdit7 ? 7 : 6;
    }

    public SchedulerUtils getScheduler() {
        return this.scheduler;
    }

    public FoliaUtils getFoliaUtils() {
        return this.foliaUtils;
    }

    public boolean isHoldingSelectionItem(Player player) {
        return isSelectionItem(getItemInMainHand(player)) || isSelectionItem(getItemInOffHand(player));
    }

    public void sendActionBar(Player player, String message) {
        if (!this.supportActionBar) {
            player.sendMessage(message);
            return;
        }

        SpigotActionBarAdapter.sendActionBar(player, message);
    }

    public boolean isSelectionItem(@Nullable ItemStack item) {
        if (this.selectionItem == null) {
            return true;
        }

        return item != null && item.getType() == this.selectionItem;
    }

    @SuppressWarnings("deprecation") // 1.7.10/1.8 servers support
    private ItemStack getItemInMainHand(Player player) {
        return player.getItemInHand();
    }

    private @Nullable ItemStack getItemInOffHand(Player player) {
        if (!this.supportOffHand) {
            return null;
        }

        return player.getInventory().getItemInOffHand();
    }

    private boolean isActionBarSupported() {
        try {
            Class.forName("net.md_5.bungee.api.ChatMessageType");
            Player.class.getMethod("spigot");
            SpigotActionBarAdapter.checkSupported();

            return true;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return false;
        }
    }

    private boolean isOffhandSupported() {
        try {
            PlayerInventory.class.getMethod("getItemInOffHand");

            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private boolean isWorldEdit7() {
        try {
            Class.forName("com.sk89q.worldedit.math.Vector3");

            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
