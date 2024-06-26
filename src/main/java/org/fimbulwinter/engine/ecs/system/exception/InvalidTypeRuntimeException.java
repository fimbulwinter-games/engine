package org.fimbulwinter.engine.ecs.system.exception;

import org.fimbulwinter.engine.ecs.Entity;

import java.lang.reflect.Method;

public class InvalidTypeRuntimeException extends AutoinjectionRuntimeException {
    public InvalidTypeRuntimeException(Method method, Class<?> containingClass, Class<?> expectedType) {
        super("One of the parameters of method '" + method.getName() + "' in class '" + containingClass.getName() + "' is not of type '" + expectedType.getName() + '"' + " or '" + Entity.class.getName() + "'.");
    }

}
