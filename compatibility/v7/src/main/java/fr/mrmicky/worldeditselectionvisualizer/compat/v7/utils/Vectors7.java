package fr.mrmicky.worldeditselectionvisualizer.compat.v7.utils;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import fr.mrmicky.worldeditselectionvisualizer.math.Vector3d;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Vectors7 {

    private Vectors7() {
        throw new UnsupportedOperationException();
    }

    public static Vector3d toVector3d(Vector3 vector) {
        return new Vector3d(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector3d toVector3d(BlockVector3 vector) {
        return new Vector3d(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector3 toVector3(Vector3d vector) {
        return Vector3.at(vector.getX(), vector.getY(), vector.getZ());
    }

    public static BlockVector3 toBlockVector3(Vector3d vector) {
        return BlockVector3.at(vector.getX(), vector.getY(), vector.getZ());
    }
}
