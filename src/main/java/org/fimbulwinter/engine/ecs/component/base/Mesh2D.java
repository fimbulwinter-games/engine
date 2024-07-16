package org.fimbulwinter.engine.ecs.component.base;

import org.joml.Vector2f;

import java.util.List;
import java.util.Objects;

public class Mesh2D {
    List<Vector2f> vertexPos;
    List<Integer> indices;

    public Mesh2D(List<Vector2f> vertexPos, List<Integer> indices) {
        Objects.requireNonNull(vertexPos);
        Objects.requireNonNull(indices);
        this.vertexPos = vertexPos;
        this.indices = indices;
    }

    public static Mesh2D createSquare() {
        final var vertexPos = List.of(
                new Vector2f(-1.0f, -1.0f), // The bottom left corner
                new Vector2f(-1.0f, 1.0f), // The top left corner
                new Vector2f(1.0f, 1.0f), // The top right corner
                new Vector2f(1.0f, -1.0f)); // The bottom right corner

        final var indices = List.of(
                0, 1, 2,
                3, 0, 1);

        return new Mesh2D(vertexPos, indices);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mesh2D mesh2D)) return false;

        return vertexPos.equals(mesh2D.vertexPos) && indices.equals(mesh2D.indices);
    }

    @Override
    public int hashCode() {
        int result = vertexPos.hashCode();
        result = 31 * result + indices.hashCode();
        return result;
    }

    public List<Vector2f> getVertexPos() {
        return vertexPos;
    }

    public void setVertexPos(List<Vector2f> vertexPos) {
        this.vertexPos = vertexPos;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }
}
