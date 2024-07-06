package org.fimbulwinter.engine.ecs.system;

import org.fimbulwinter.engine.ecs.component.ComponentSet;
import org.fimbulwinter.engine.ecs.entity.Entity;
import org.fimbulwinter.engine.ecs.resource.Resource;
import org.fimbulwinter.engine.ecs.scheduling.SystemTask;

import java.util.*;

public class SystemStorage {
    private final Set<RegisteredSystem> registeredSystems = new HashSet<>();

    public SystemStorage() {
    }

    private void registerSystem(RegisteredSystem system) {
        Objects.requireNonNull(system);
        registeredSystems.add(system);
    }

    public void registerSystem(Class<?> systemContainer) {
        Objects.requireNonNull(systemContainer);
        Arrays.stream(systemContainer.getDeclaredMethods())
                .filter(x -> x.isAnnotationPresent(RegisterSystem.class))
                .map(RegisteredSystem::new)
                .forEach(this::registerSystem);
    }

    public Set<SystemTask> generateSystemTasks(Map<Entity, ComponentSet> entities,
                                               Collection<? extends Resource> resources) {
        Objects.requireNonNull(entities);
        Objects.requireNonNull(resources);

        final Set<SystemTask> tasks = new HashSet<>();

        for (var system : registeredSystems) {
            tasks.addAll(system.generateSystemTasks(entities, resources));
        }

        return tasks;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemStorage that)) return false;

        return registeredSystems.equals(that.registeredSystems);
    }

    @Override
    public int hashCode() {
        return registeredSystems.hashCode();
    }
}
