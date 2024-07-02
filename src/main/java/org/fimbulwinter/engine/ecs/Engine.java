package org.fimbulwinter.engine.ecs;

import org.fimbulwinter.engine.ecs.resource.ResourceStorage;
import org.fimbulwinter.engine.ecs.scheduling.Scheduler;

import java.util.List;

public class Engine {
    private final EntityStorage entityStorage = new EntityStorage();
    private final Scheduler scheduler = new Scheduler();
    private final ResourceStorage resourceStorage = new ResourceStorage();

    public void run() {
    }

    public void tick() {
        entityStorage.runSystem(scheduler::tick);
    }

    public Entity instantiate(Component... components) {
        return entityStorage.instantiate(List.of(components));
    }

    public void registerSystems(Class<?> systemContainer) {
        scheduler.addSystems(systemContainer);
    }
}
