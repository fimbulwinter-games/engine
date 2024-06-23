package org.fimbulwinter.engine.ecs;

import org.fimbulwinter.engine.ecs.system.System;
import org.fimbulwinter.engine.ecs.system.SystemContainer;

import java.util.HashSet;
import java.util.Set;

public class Engine {
    private final EntityStorage entityStorage = new EntityStorage();
    private final Set<System> systems = new HashSet<>();
    private final Set<SystemContainer> systemContainers = new HashSet<>();

    public void run() {

    }

    public void tick() {
        systems.forEach(entityStorage::runSystem);
        ;
    }

    public Entity instantiate(Set<? extends Component> componentSet) {
        return entityStorage.instantiate(componentSet);
    }

    public void registerSystem(System system) {
        systems.add(system);
    }

    public void registerSystems(SystemContainer systemContainer) {
        systemContainer.validateSystems();
        systemContainers.add(systemContainer);
    }

}
