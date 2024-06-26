package org.fimbulwinter.engine.ecs;

import java.util.Set;

public class Engine {
    private final EntityStorage entityStorage = new EntityStorage();
    private final Scheduler scheduler = new Scheduler();

    public void run() {

    }

    public void tick() {
        entityStorage.runSystem(scheduler::tick);
    }

    public Entity instantiate(Set<? extends Component> componentSet) {
        return entityStorage.instantiate(componentSet);
    }

    public void registerSystems(Class<?> systemContainer) {
        scheduler.addSystems(systemContainer);
    }

}
