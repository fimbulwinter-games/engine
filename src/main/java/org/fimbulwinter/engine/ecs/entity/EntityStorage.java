package org.fimbulwinter.engine.ecs.entity;

import org.fimbulwinter.engine.ecs.component.Component;
import org.fimbulwinter.engine.ecs.component.ComponentSet;

import java.util.*;

public class EntityStorage {
    private final Map<Entity, ComponentSet> entities = new HashMap<>();
    private int nextEntityId = 0;

    private Entity retrieveNewEntity() {
        final var nextEntity = new Entity(nextEntityId);
        nextEntityId += 1;
        return nextEntity;
    }

    public Entity instantiate(Collection<? extends Component> components) {
        Objects.requireNonNull(components);

        final var componentSet = new ComponentSet(components);

        final var newEntity = retrieveNewEntity();
        entities.put(newEntity, componentSet);

        return newEntity;
    }

    public Map<Entity, ComponentSet> getEntities() {
        return Collections.unmodifiableMap(entities);
    }

    private Set<? extends Component> createComponentSet(ComponentSet componentSet) {
        return new HashSet<Component>(componentSet.getComponents());
    }
}
