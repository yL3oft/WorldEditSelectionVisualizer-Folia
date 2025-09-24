package fr.mrmicky.worldeditselectionvisualizer.compat;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;

/**
 * A class for running tasks on both Bukkit and Folia. Without this class, you would need to check if the server is using Folia or not, and then run the task accordingly.
 * The Folia scheduled task is stored as an Object and accessed via reflection to avoid compile-time dependency.
 */
public class FoliaRunnable extends BukkitRunnable {

    private Object foliaTask;
    private BukkitTask bukkitTask;

    /**
     * Cancels the task if it is running.
     */
    @Override
    public synchronized void cancel() throws IllegalStateException {
        if (this.foliaTask != null) {
            try {
                Method cancel = this.foliaTask.getClass().getDeclaredMethod("cancel");
                cancel.setAccessible(true);
                cancel.invoke(this.foliaTask);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.foliaTask = null;
            }
        } else if (this.bukkitTask != null) {
            // Legacy Bukkit task
            this.bukkitTask.cancel();
            this.bukkitTask = null;
        }
    }

    /**
     * This method is not used. Instead, use the run() method in the subclass.
     */
    @Override
    public void run() {
    }

    /**
     * Sets the scheduled task for Folia.
     * @param task The scheduled task object (Folia's ScheduledTask), stored as Object.
     */
    public void setScheduledTask(Object task) {
        this.foliaTask = task;
    }

    /**
     * Sets the Bukkit task for legacy Bukkit.
     * @param task The {@link BukkitTask}.
     */
    public void setBukkitTask(BukkitTask task) {
        this.bukkitTask = task;
    }
}