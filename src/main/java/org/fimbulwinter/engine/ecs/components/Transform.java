package org.fimbulwinter.engine.ecs.components;

import org.fimbulwinter.engine.ecs.Component;
import org.joml.Vector3f;

import java.util.Objects;

public class Transform implements Component {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        setPosition(position);
        setRotation(rotation);
        setScale(scale);
    }

    public Transform() {
        this(new Vector3f(), new Vector3f(), new Vector3f());
    }

    @Override
    public String toString() {
        return "Transform{" +
                "P(" + position.x() + ", " + position.y() + ", " + position.z() + ")" +
                ", R(" + rotation.x() + ", " + rotation.y() + ", " + rotation.z() + ")" +
                ", S(" + scale.x() + ", " + scale.y() + ", " + scale.z() + ")" +
                '}';
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        Objects.requireNonNull(position);
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        Objects.requireNonNull(rotation);
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        Objects.requireNonNull(scale);
        this.scale = scale;
    }
}
