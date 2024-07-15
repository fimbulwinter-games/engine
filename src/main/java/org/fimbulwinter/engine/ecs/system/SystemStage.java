package org.fimbulwinter.engine.ecs.system;

public enum SystemStage {
    PRE_UPDATE,
    UPDATE,
    POST_UPDATE,
    PRE_RENDER,
    RENDER,
    POST_RENDER
}