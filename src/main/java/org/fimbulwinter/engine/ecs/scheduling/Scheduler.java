package org.fimbulwinter.engine.ecs.scheduling;

import org.fimbulwinter.engine.ecs.system.SystemStage;

import java.util.*;

public class Scheduler {
    private final Map<SystemStage, Set<SystemTask>> systemTasks = new HashMap<>();

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

    public void tick() {
        systemTasks.get(SystemStage.PRE_UPDATE).forEach(SystemTask::run);
        systemTasks.get(SystemStage.UPDATE).forEach(SystemTask::run);
        systemTasks.get(SystemStage.POST_UPDATE).forEach(SystemTask::run);
        systemTasks.get(SystemStage.RENDER).forEach(SystemTask::run);
    }
}
