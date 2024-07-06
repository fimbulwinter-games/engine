package org.fimbulwinter.engine.ecs;

import org.fimbulwinter.engine.ecs.resource.Resource;
import org.fimbulwinter.engine.ecs.resource.ResourceStorage;
import org.fimbulwinter.engine.ecs.scheduling.Scheduler;
import org.fimbulwinter.engine.ecs.scheduling.SystemTask;

import java.util.List;
import java.util.Set;

public class Engine {
    private final EntityStorage entityStorage = new EntityStorage();
    private final SystemStorage systemStorage = new SystemStorage();
    private final Scheduler scheduler = new Scheduler();
    private final ResourceStorage resourceStorage = new ResourceStorage();

    public void run() {
    }

    public void tick() {
        scheduler.tick();
    }

    public Entity instantiate(Component... components) {
        final var entity = entityStorage.instantiate(List.of(components));
        regenerateScheduler();
        return entity;
    }

    public void registerSystems(Class<?> systemContainer) {
        systemStorage.registerSystem(systemContainer);
        regenerateScheduler();
    }

    public void registerResource(Resource resource) {
        this.resourceStorage.insertResource(resource);
        regenerateScheduler();
    }

    private void regenerateScheduler() {
        final Set<SystemTask> tasks = systemStorage.generateSystemTasks(entityStorage.getEntities(), resourceStorage.getResources());
        scheduler.clear();
        scheduler.addTasks(tasks);
    }
}
