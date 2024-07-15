package org.fimbulwinter.engine.ecs.scheduling;

import org.fimbulwinter.engine.ecs.system.SystemStage;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Scheduler {
    private final Map<SystemStage, Set<SystemTask>> systemTasks = new HashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Scheduler() {
        for (SystemStage stage : SystemStage.values()) {
            systemTasks.put(stage, new HashSet<>());
        }
    }

    public void clear() {
        systemTasks.values().forEach(Set::clear);
    }

    public void addTasks(Collection<? extends SystemTask> tasks) {
        for (SystemTask task : tasks) {
            systemTasks.get(task.getSystemStage()).add(task);
        }
    }

    private void tick(Collection<SystemTask> tasks) {
        final Set<Future<?>> futures = new HashSet<>();

        tasks.forEach(task -> futures.add(executor.submit(task)));

        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void tick() {
        for (var stage : SystemStage.values()) {
            tick(systemTasks.get(stage));
        }
    }
}
