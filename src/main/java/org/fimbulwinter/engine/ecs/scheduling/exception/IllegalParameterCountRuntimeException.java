package org.fimbulwinter.engine.ecs.scheduling.exception;

import java.lang.reflect.Method;

public class IllegalParameterCountRuntimeException extends RuntimeException {
    public IllegalParameterCountRuntimeException(Method system, int givenParameterCount) {
        super(system.getName() + " expects " + system.getParameterCount() + " arguments but " + givenParameterCount + " were given");
    }
}
