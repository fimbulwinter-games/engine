package org.fimbulwinter.engine.ecs;

public interface Resource {
    default String getResourceIdentifier() {
        return this.getClass().getName();
    }
}
