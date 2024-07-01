package org.fimbulwinter.engine.ecs;

public interface Component extends AutoInjectable {
    default String getComponentIdentifier() {
        return getClass().getName();
    }
}
