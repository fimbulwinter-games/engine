package org.fimbulwinter.engine.ecs.scheduling.exception;

public class TaskInvocationRuntimeException extends RuntimeException {
    public TaskInvocationRuntimeException(Exception e) {
        super(e);
    }
}
