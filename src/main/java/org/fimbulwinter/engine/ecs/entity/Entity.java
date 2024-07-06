package org.fimbulwinter.engine.ecs.entity;

import org.fimbulwinter.engine.ecs.system.AutoInjectable;

import java.util.Objects;

public class Entity implements AutoInjectable {
    private final int id;

    public Entity(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }
}
