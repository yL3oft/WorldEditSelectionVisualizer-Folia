package fr.mrmicky.worldeditselectionvisualizer.display;

import fr.mrmicky.fastparticles.ParticleData;
import fr.mrmicky.fastparticles.ParticleType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public class Particle {

    public static final Particle FALLBACK = new Particle(ParticleType.of("FLAME"));

    private final ParticleType type;
    private final @Nullable ParticleData data;

    public Particle(ParticleType type) {
        this(type, null);
    }

    public Particle(ParticleType type, @Nullable ParticleData data) {
        this.type = Objects.requireNonNull(type, "type");
        this.data = data;
    }

    public ParticleType getType() {
        return this.type;
    }

    public @Nullable ParticleData getData() {
        return this.data;
    }
}
