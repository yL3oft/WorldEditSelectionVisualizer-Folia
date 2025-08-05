package fr.mrmicky.worldeditselectionvisualizer.config;

import fr.mrmicky.worldeditselectionvisualizer.display.DisplayType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GlobalSelectionConfig {

    private final int fadeDelay;
    private final int maxSelectionSize;

    private final SelectionConfig primary;
    private final SelectionConfig secondary;
    private final SelectionConfig origin;

    public GlobalSelectionConfig(int fadeDelay, int maxSelectionSize,
                                 SelectionConfig primary,
                                 SelectionConfig secondary,
                                 SelectionConfig origin) {
        this.fadeDelay = fadeDelay;
        this.maxSelectionSize = maxSelectionSize;
        this.primary = primary;
        this.secondary = secondary;
        this.origin = origin;
    }

    public int getFadeDelay() {
        return this.fadeDelay;
    }

    public int getMaxSelectionSize() {
        return this.maxSelectionSize;
    }

    public SelectionConfig primary() {
        return this.primary;
    }

    public SelectionConfig secondary() {
        return this.secondary;
    }

    public SelectionConfig origin() {
        return this.origin;
    }

    public SelectionConfig byType(DisplayType type) {
        switch (type) {
            case PRIMARY:
                return this.primary;
            case SECONDARY:
                return this.secondary;
            case ORIGIN:
                return this.origin;
        }

        throw new IllegalArgumentException("Invalid display type: " + type);
    }
}
