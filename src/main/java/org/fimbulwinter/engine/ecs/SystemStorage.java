package org.fimbulwinter.engine.ecs;

import org.fimbulwinter.engine.ecs.system.RegisterSystem;
import org.fimbulwinter.engine.ecs.system.RegisteredSystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
                .forEach(registeredSystems::add);
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
