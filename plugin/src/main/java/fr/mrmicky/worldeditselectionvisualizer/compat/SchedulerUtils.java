package fr.mrmicky.worldeditselectionvisualizer.compat;

import fr.mrmicky.worldeditselectionvisualizer.WorldEditSelectionVisualizer;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * Utility class for scheduling tasks in a Paper/Folia server.
 * Folia / Paper threaded region classes are accessed via reflection to avoid
 * compile-time dependency on those classes.
 */
public class SchedulerUtils {

    private final WorldEditSelectionVisualizer plugin;

    public SchedulerUtils(WorldEditSelectionVisualizer plugin) {
        this.plugin = plugin;
    }

    private Method findMethodByNameAndParamCount(Object obj, String name, int paramCount) throws NoSuchMethodException {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == paramCount) {
                return m;
            }
        }
        throw new NoSuchMethodException("Method " + name + " with " + paramCount + " params not found on " + obj.getClass());
    }

    private Object invokeMethodByName(Object obj, String name, Object... args) throws Exception {
        Method m = findMethodByNameAndParamCount(obj, name, args.length);
        return m.invoke(obj, args);
    }

    private Object getRegionScheduler() throws Exception {
        Method getRegionScheduler = plugin.getServer().getClass().getMethod("getRegionScheduler");
        return getRegionScheduler.invoke(plugin.getServer());
    }

    private Object getGlobalRegionScheduler() throws Exception {
        Method getGlobalScheduler = plugin.getServer().getClass().getMethod("getGlobalRegionScheduler");
        return getGlobalScheduler.invoke(plugin.getServer());
    }

    /**
     * Schedules a task to run later on the main server thread.
     * @param loc The location where the task should run, or null for the main thread.
     * @param task   The task to run.
     * @param delay  The delay in ticks before the task runs.
     */
    public void runTaskLater(@Nullable Location loc, @NotNull Runnable task, long delay) {
        if (plugin.getCompatibilityHelper().folia) {
            try {
                if (loc != null) {
                    Object regionScheduler = getRegionScheduler();
                    // runDelayed(plugin, loc, Consumer<ScheduledTask>, delay)
                    Consumer<Object> consumer = (scheduledTask) -> task.run();
                    invokeMethodByName(regionScheduler, "runDelayed", plugin, loc, consumer, delay);
                } else {
                    Object globalScheduler = getGlobalRegionScheduler();
                    // runDelayed(plugin, Consumer<ScheduledTask>, delay)
                    Consumer<Object> consumer = (scheduledTask) -> task.run();
                    invokeMethodByName(globalScheduler, "runDelayed", plugin, consumer, delay);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        plugin.getServer().getScheduler().runTaskLater(plugin, task, delay);
    }

    /**
     * Schedules a task to run repeatedly on the main server thread.
     * @param loc The location where the task should run, or null for the main thread.
     * @param runnable   The FoliaRunnable to run.
     * @param delay  The delay in ticks before the task runs.
     * @param period The period in ticks between subsequent runs of the task.
     */
    public FoliaRunnable runTaskTimer(@Nullable Location loc, @NotNull FoliaRunnable runnable, long delay, long period) {
        if (plugin.getCompatibilityHelper().folia) {
            try {
                Object scheduledTaskObj;
                if (loc != null) {
                    Object regionScheduler = getRegionScheduler();
                    // runAtFixedRate(plugin, loc, Consumer<ScheduledTask>, delay, period)
                    Consumer<Object> consumer = (t) -> runnable.run();
                    scheduledTaskObj = invokeMethodByName(regionScheduler, "runAtFixedRate", plugin, loc, consumer, delay, period);
                } else {
                    Object globalScheduler = getGlobalRegionScheduler();
                    // runAtFixedRate(plugin, Consumer<ScheduledTask>, delay, period)
                    Consumer<Object> consumer = (t) -> runnable.run();
                    scheduledTaskObj = invokeMethodByName(globalScheduler, "runAtFixedRate", plugin, consumer, delay, period);
                }
                runnable.setScheduledTask(scheduledTaskObj);
                return runnable;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return runnable;
        }
        BukkitTask task = runnable.runTaskTimer(plugin, delay, period);
        runnable.setBukkitTask(task);
        return runnable;
    }

    /**
     * Schedules a task to run later asynchronously on the main server thread.
     * @param loc The location where the task should run, or null for the main thread.
     * @param task   The task to run.
     * @param delay  The delay in ticks before the task runs.
     */
    public void runTaskLaterAsynchronously(@Nullable Location loc, @NotNull Runnable task, long delay) {
        if (plugin.getCompatibilityHelper().folia) {
            try {
                if (loc != null) {
                    Object regionScheduler = getRegionScheduler();
                    Consumer<Object> consumer = (t) -> task.run();
                    invokeMethodByName(regionScheduler, "runDelayed", plugin, loc, consumer, delay);
                } else {
                    Object globalScheduler = getGlobalRegionScheduler();
                    Consumer<Object> consumer = (t) -> task.run();
                    invokeMethodByName(globalScheduler, "runDelayed", plugin, consumer, delay);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
    }

    /**
     * Schedules a task to run repeatedly on the main server thread asynchronously.
     * @param runnable The FoliaRunnable to run.
     * @param delay  The delay in ticks before the task runs.
     * @param period The period in ticks between subsequent runs of the task.
     */
    public void runTaskTimerAsynchronously(@NotNull FoliaRunnable runnable, long delay, long period) {
        if (plugin.getCompatibilityHelper().folia) {
            try {
                Object globalScheduler = getGlobalRegionScheduler();
                class AsyncRepeatingTask {
                    void start(long initialDelay) {
                        try {
                            Consumer<Object> consumer = (t) -> {
                                runnable.run();
                                start(period);
                            };
                            Object task = invokeMethodByName(globalScheduler, "runDelayed", plugin, consumer, initialDelay);
                            runnable.setScheduledTask(task);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                new AsyncRepeatingTask().start(delay);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        BukkitTask task = runnable.runTaskTimerAsynchronously(plugin, delay, period);
        runnable.setBukkitTask(task);
    }

    /**
     * Runs a task asynchronously on the main server thread.
     * @param task The task to run.
     */
    public void runTaskAsynchronously(@NotNull Runnable task) {
        if (plugin.getCompatibilityHelper().folia) {
            try {
                Object globalScheduler = getGlobalRegionScheduler();
                // execute(plugin, Runnable)
                invokeMethodByName(globalScheduler, "execute", plugin, (Runnable) task);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task);
    }

    /**
     * Runs a task on the main server thread at a specific location or globally.
     * @param loc The location where the task should run, or null for the main thread.
     * @param task The task to run.
     */
    public void runTask(@Nullable Location loc, @NotNull Runnable task) {
        if (plugin.getCompatibilityHelper().folia) {
            try {
                if (loc != null) {
                    Object regionScheduler = getRegionScheduler();
                    // execute(plugin, loc, Runnable)
                    invokeMethodByName(regionScheduler, "execute", plugin, loc, (Runnable) task);
                } else {
                    Object globalScheduler = getGlobalRegionScheduler();
                    // execute(plugin, Runnable)
                    invokeMethodByName(globalScheduler, "execute", plugin, (Runnable) task);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        plugin.getServer().getScheduler().runTask(plugin, task);
    }

    /**
     * Calls a method synchronously on the main server thread at a specific location or globally.
     * @param loc The location where the task should run, or null for the main thread.
     * @param task The task to run.
     * @return A CompletableFuture that will hold the result of the task.
     */
    public <T> CompletableFuture<T> callSyncMethod(@Nullable Location loc, @NotNull Callable<T> task) {
        if (plugin.getCompatibilityHelper().folia) {
            CompletableFuture<T> future = new CompletableFuture<>();
            try {
                if (loc != null) {
                    Object regionScheduler = getRegionScheduler();
                    // execute(plugin, loc, Runnable)
                    invokeMethodByName(regionScheduler, "execute", plugin, loc, (Runnable) () -> {
                        try {
                            future.complete(task.call());
                        } catch (Exception e) {
                            future.completeExceptionally(e);
                        }
                    });
                } else {
                    Object globalScheduler = getGlobalRegionScheduler();
                    invokeMethodByName(globalScheduler, "execute", plugin, (Runnable) () -> {
                        try {
                            future.complete(task.call());
                        } catch (Exception e) {
                            future.completeExceptionally(e);
                        }
                    });
                }
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
            return future;
        }
        CompletableFuture<T> cf = new CompletableFuture<>();
        try {
            Future<T> bukkitFuture = plugin.getServer().getScheduler().callSyncMethod(plugin, task);
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    T result = bukkitFuture.get();
                    cf.complete(result);
                } catch (Throwable ex) {
                    cf.completeExceptionally(ex);
                }
            });
        } catch (Throwable e) {
            cf.completeExceptionally(e);
        }
        return cf;
    }
}