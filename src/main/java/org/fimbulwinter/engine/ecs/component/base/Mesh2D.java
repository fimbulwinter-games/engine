package org.fimbulwinter.engine.ecs.component.base;

import org.joml.Vector2f;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Mesh2D {
    float[] vertexPos;

    public Mesh2D(float[] vertexPos) {
        setVertexPos(vertexPos);
    }

    public Mesh2D(List<Vector2f> vertexPos) {
        Objects.requireNonNull(vertexPos);
        this.vertexPos = new float[vertexPos.size() * 2];
        for (int i = 0; i < vertexPos.size(); ++i) {
            this.vertexPos[i * 2] = vertexPos.get(i).x;
            this.vertexPos[i * 2 + 1] = vertexPos.get(i).y;
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mesh2D mesh2D)) return false;

        return Arrays.equals(vertexPos, mesh2D.vertexPos);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vertexPos);
    }

    public float[] getVertexPos() {
        return vertexPos;
    }

    public void setVertexPos(float[] vertexPos) {
        Objects.requireNonNull(vertexPos);
        this.vertexPos = vertexPos;
    }
}
