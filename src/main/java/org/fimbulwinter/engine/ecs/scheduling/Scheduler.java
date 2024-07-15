package org.fimbulwinter.engine.ecs.scheduling;

import org.fimbulwinter.engine.ecs.system.SystemStage;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Scheduler {
    private final Map<SystemStage, Set<SystemTask>> workerThreadTasks = new HashMap<>();
    private final Map<SystemStage, Set<SystemTask>> mainThreadTasks = new HashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Scheduler() {
        for (SystemStage stage : SystemStage.values()) {
            workerThreadTasks.put(stage, new HashSet<>());
            mainThreadTasks.put(stage, new HashSet<>());
        }
    }

    public void clear() {
        workerThreadTasks.values().forEach(Set::clear);
    }

    public void addTasks(Collection<? extends SystemTask> tasks) {
        for (SystemTask task : tasks) {
            switch (task.getTargetThread()) {
                case WORKER:
                    workerThreadTasks.get(task.getSystemStage()).add(task);
                    break;
                case MAIN:
                    mainThreadTasks.get(task.getSystemStage()).add(task);
                    break;
            }
        }
    }

    private void tick(SystemStage stage) {
        final Set<Future<?>> futures = new HashSet<>();

        workerThreadTasks.get(stage).forEach(task -> futures.add(executor.submit(task)));

        mainThreadTasks.get(stage).forEach(SystemTask::run);

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
            tick(stage);
        }
    }
}
