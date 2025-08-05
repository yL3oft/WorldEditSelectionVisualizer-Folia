package fr.mrmicky.worldeditselectionvisualizer.compat.v6.utils;

import com.sk89q.worldedit.Vector;
import fr.mrmicky.worldeditselectionvisualizer.math.Vector3d;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Vectors6 {

    private Vectors6() {
        throw new UnsupportedOperationException();
    }

    public static Vector3d toVector3d(Vector vector) {
        return new Vector3d(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector toVector(Vector3d vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }
}
