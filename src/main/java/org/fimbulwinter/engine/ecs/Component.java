package org.fimbulwinter.engine.ecs;

public interface Component {
    default String getComponentIdentifier() {
        return getClass().getName();
    }
}
