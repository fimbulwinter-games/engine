package org.fimbulwinter.engine.ecs.resource.base;

import org.fimbulwinter.engine.ecs.resource.Resource;
import org.fimbulwinter.engine.ecs.scheduling.TargetThread;
import org.fimbulwinter.engine.ecs.system.RegisterSystem;
import org.fimbulwinter.engine.ecs.system.SystemStage;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Resource {
    private final String vertexShaderSource = """
            #version 330 core
            layout (location = 0) in vec3 position;
            void main()
            {
            gl_Position = vec4(position.x, position.y, position.z, 1.0);
            }\0""";
    private final String fragmentShaderSource = """
            #version 330 core
            out vec4 color;
            void main()
            {
            color = vec4(1.0f, 0.5f, 0.2f, 1.0f);
            }
            \0""";
    private int shaderProgram;
    private long handle;
    private int VAO, VBO;

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

        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        final int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);

        final int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);


        float[] vertices = {
                // First triangle
                -0.9f, -0.5f, 0.0f,  // Left
                -0.0f, -0.5f, 0.0f,  // Right
                -0.45f, 0.5f, 0.0f,  // Top
                // Second triangle
                0.0f, -0.5f, 0.0f,  // Left
                0.9f, -0.5f, 0.0f,  // Right
                0.45f, 0.5f, 0.0f   // Top
        };

        VAO = glGenVertexArrays();
        VBO = glGenBuffers();

        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

    }

    @RegisterSystem(systemStage = SystemStage.POST_RENDER, targetThread = TargetThread.MAIN)
    public static void refreshWindow(Window window) {
        glfwPollEvents();
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        glUseProgram(window.shaderProgram);
        glBindVertexArray(window.VAO);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
        glfwSwapBuffers(window.handle);
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
