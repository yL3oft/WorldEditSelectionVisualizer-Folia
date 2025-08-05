package fr.mrmicky.worldeditselectionvisualizer.selection;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

@NullMarked
public enum SelectionType {

    SELECTION, CLIPBOARD;

    private static final SelectionType[] VALUES = values();

    private final String name;

    SelectionType() {
        this.name = name().toLowerCase(Locale.ROOT);
    }

    public static @Nullable SelectionType from(String type) {
        try {
            return valueOf(type.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    static SelectionType[] getValues() {
        return VALUES;
    }

    public String getName() {
        return this.name;
    }
}
