package fr.mrmicky.worldeditselectionvisualizer.config;

import fr.mrmicky.worldeditselectionvisualizer.display.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public class SelectionConfig {

    private final double pointsDistance;
    private final double linesGap;
    private final int updateInterval;
    private final int viewDistance;
    private final Particle particle;

    public SelectionConfig(ConfigurationSection config, Function<ConfigurationSection, Particle> map) {
        this.pointsDistance = config.getDouble("points-distance");
        this.linesGap = config.getDouble("lines-gap");
        this.updateInterval = config.getInt("update-interval");
        this.viewDistance = config.getInt("view-distance");
        this.particle = map.apply(config.getConfigurationSection("particles"));
    }

    public double getPointsDistance() {
        return this.pointsDistance;
    }

    public double getLinesGap() {
        return this.linesGap;
    }

    public int getUpdateInterval() {
        return this.updateInterval;
    }

    public int getViewDistance() {
        return this.viewDistance;
    }

    public Particle getParticle() {
        return this.particle;
    }
}
