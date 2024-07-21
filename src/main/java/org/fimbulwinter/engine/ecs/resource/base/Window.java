package org.fimbulwinter.engine.ecs.resource.base;

import org.fimbulwinter.engine.ecs.resource.Resource;
import org.fimbulwinter.engine.ecs.scheduling.TargetThread;
import org.fimbulwinter.engine.ecs.system.RegisterSystem;
import org.fimbulwinter.engine.ecs.system.SystemStage;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Resource {

    private long handle;

    public Window() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);


        // Create the window
        handle = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
        if (handle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(handle, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(handle, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);

            IntBuffer windowWidthBuffer = stack.mallocInt(1);
            IntBuffer windowHeightBuffer = stack.mallocInt(1);

            glfwGetFramebufferSize(handle, windowWidthBuffer, windowHeightBuffer);
        }


        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);

        glfwShowWindow(handle);
    }

    @RegisterSystem(systemStage = SystemStage.POST_RENDER, targetThread = TargetThread.MAIN)
    public static void refreshWindow(Window window) {
        glfwSwapBuffers(window.handle);
    }

    @RegisterSystem(systemStage = SystemStage.PRE_UPDATE, targetThread = TargetThread.MAIN)
    public static void pollEvents(Window window) {
        glfwPollEvents();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Window)) return false;

        Window window = (Window) o;
        return handle == window.handle;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(handle);
    }
}
