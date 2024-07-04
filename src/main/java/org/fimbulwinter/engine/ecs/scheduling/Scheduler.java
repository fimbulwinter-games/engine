package org.fimbulwinter.engine.ecs.scheduling;

import java.util.HashSet;
import java.util.Set;

public class Scheduler {
    private final Set<SystemTask> systemTasks = new HashSet<>();

    public Scheduler() {
    }
}
