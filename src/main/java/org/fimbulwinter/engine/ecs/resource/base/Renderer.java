package org.fimbulwinter.engine.ecs.resource.base;

import org.fimbulwinter.engine.ecs.resource.Resource;
import org.fimbulwinter.engine.ecs.scheduling.TargetThread;
import org.fimbulwinter.engine.ecs.system.RegisterSystem;
import org.fimbulwinter.engine.ecs.system.SystemStage;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderer implements Resource {
    private static final String vertexShaderSource = """
            #version 330 core
            layout (location = 0) in vec3 position;
            void main()
            {
            gl_Position = vec4(position.x, position.y, position.z, 1.0);
            }\0""";
    private static final String fragmentShaderSource = """
            #version 330 core
            out vec4 color;
            void main()
            {
            color = vec4(1.0f, 0.5f, 0.2f, 1.0f);
            }
            \0""";
    private int shaderProgram;

    private int VAO;
    private int VBO;

    public Renderer() {
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

    @RegisterSystem(systemStage = SystemStage.RENDER, targetThread = TargetThread.MAIN)
    public static void render(Renderer renderer) {
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        glUseProgram(renderer.shaderProgram);
        glBindVertexArray(renderer.VAO);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
    }
}
