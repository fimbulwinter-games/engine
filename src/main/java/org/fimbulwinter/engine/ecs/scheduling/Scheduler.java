package org.fimbulwinter.engine.ecs.scheduling;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Scheduler {
    private final Set<SystemTask> systemTasks = new HashSet<>();

    public Scheduler() {
    }

    public void clear() {
        systemTasks.clear();
    }

    public void addTasks(Collection<? extends SystemTask> tasks) {
        systemTasks.addAll(tasks);
    }

    public void tick() {
        systemTasks.forEach(SystemTask::run);
    }
}
