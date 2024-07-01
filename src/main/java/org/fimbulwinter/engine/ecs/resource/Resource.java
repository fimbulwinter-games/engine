package org.fimbulwinter.engine.ecs.resource;

public interface Resource {
    default String getResourceIdentifier() {
        return this.getClass().getName();
    }
}
