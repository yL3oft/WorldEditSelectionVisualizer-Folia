package fr.mrmicky.worldeditselectionvisualizer.selection;

import fr.mrmicky.worldeditselectionvisualizer.math.Vector3d;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

@NullMarked
public class PlayerSelection {

    private final SelectionType selectionType;

    private Vector3d origin = Vector3d.ZERO;
    private boolean lastSelectionTooLarge = false;
    private @Nullable SelectionPoints selectionPoints;
    private @Nullable Instant expireTime;
    private @Nullable RegionInfo lastSelectedRegion;

    public PlayerSelection(SelectionType selectionType) {
        this.selectionType = Objects.requireNonNull(selectionType, "selectionType");
    }

    public @Nullable SelectionPoints getSelectionPoints() {
        return this.selectionPoints;
    }

    public Vector3d getOrigin() {
        return this.origin;
    }

    public @Nullable Instant getExpireTime() {
        return this.expireTime;
    }

    public @Nullable RegionInfo getLastSelectedRegion() {
        return this.lastSelectedRegion;
    }

    public boolean isLastSelectionTooLarge() {
        return this.lastSelectionTooLarge;
    }

    public void setLastSelectionTooLarge(boolean lastSelectionTooLarge) {
        this.lastSelectionTooLarge = lastSelectionTooLarge;
    }

    public SelectionType getSelectionType() {
        return this.selectionType;
    }

    public long getSelectedVolume() {
        return this.lastSelectedRegion != null ? this.lastSelectedRegion.getVolume() : 0;
    }

    public PlayerSelection verifyExpireTime() {
        if (this.expireTime != null && this.expireTime.isBefore(Instant.now())) {
            this.expireTime = null;
            this.selectionPoints = null;
        }
        return this;
    }

    public void updateSelection(@Nullable SelectionPoints selectionPoints,
                                @Nullable RegionInfo lastSelectedRegion,
                                Vector3d origin,
                                int expireSeconds) {
        this.selectionPoints = selectionPoints;
        this.lastSelectedRegion = lastSelectedRegion;
        this.origin = origin;
        this.lastSelectionTooLarge = false;
        this.expireTime = expireSeconds > 0 ? Instant.now().plusSeconds(expireSeconds) : null;
    }

    public void resetSelection() {
        resetSelection(null);
    }

    public void resetSelection(@Nullable RegionInfo lastSelectedRegion) {
        this.lastSelectedRegion = lastSelectedRegion;
        this.selectionPoints = null;
        this.origin = Vector3d.ZERO;
        this.expireTime = null;
        this.lastSelectionTooLarge = false;
    }
}
