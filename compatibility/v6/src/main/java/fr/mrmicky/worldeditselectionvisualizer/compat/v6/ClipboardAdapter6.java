package fr.mrmicky.worldeditselectionvisualizer.compat.v6;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionOperationException;
import fr.mrmicky.worldeditselectionvisualizer.compat.ClipboardAdapter;
import fr.mrmicky.worldeditselectionvisualizer.compat.v6.utils.Vectors6;
import fr.mrmicky.worldeditselectionvisualizer.math.Vector3d;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class ClipboardAdapter6 implements ClipboardAdapter {

    private final Clipboard clipboard;

    public ClipboardAdapter6(Clipboard clipboard) {
        this.clipboard = Objects.requireNonNull(clipboard, "clipboard");
    }

    @Override
    public Vector3d getOrigin() {
        return Vectors6.toVector3d(this.clipboard.getOrigin());
    }

    @Override
    public Clipboard getClipboard() {
        return this.clipboard;
    }

    @Override
    public Region shiftRegion(Vector3d change) throws RegionOperationException {
        Region region = this.clipboard.getRegion().clone();
        region.shift(Vectors6.toVector(change));
        return region;
    }
}
