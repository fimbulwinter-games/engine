package org.fimbulwinter.engine.ecs;

import org.fimbulwinter.engine.ecs.system.System;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityStorage {
    private final Map<Entity, Map<String, ? extends Component>> entities = new HashMap<>();
    private int nextEntityId = 0;

    private Entity retrieveNewEntity() {
        final var nextEntity = new Entity(nextEntityId);
        nextEntityId += 1;
        return nextEntity;
    }

    public Entity instantiate(Set<? extends Component> componentSet) {
        Objects.requireNonNull(componentSet);

        final Map<String, Component> components = componentSet
                .stream()
                .collect(Collectors.toMap(Component::getComponentIdentifier, x -> x));

        final var newEntity = retrieveNewEntity();
        entities.put(newEntity, components);

        return newEntity;
    }

    private Set<? extends Component> createComponentSet(Map<String, ? extends Component> components) {
        return components.values().stream().collect(Collectors.toUnmodifiableSet());
    }

    public void runSystem(System system) {
        entities.forEach((entity, components) -> {
            final var componentSet = createComponentSet(components);
            system.run(entity, componentSet);
        });
    }
}
