package org.fimbulwinter.engine;

import org.fimbulwinter.engine.ecs.Engine;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.components.Transform;
import org.fimbulwinter.engine.ecs.system.RegisterSystem;
import org.fimbulwinter.engine.ecs.system.SystemContainer;

import java.util.Set;

public class TestMain {
    public static void main(String[] args) {
        final var game = new Engine();

        game.registerSystems(new TestSystems());

        game.tick();
        game.tick();
        game.instantiate(Set.of(new Transform()));
        game.instantiate(Set.of(new Transform()));
        game.instantiate(Set.of(new Transform()));

        game.tick();
        game.tick();
    }


    public static class TestSystems extends SystemContainer {
        @RegisterSystem
        public void move(Transform transform) {
            transform.setPosition(transform.getPosition().add(0, 1, 0));
        }

        @RegisterSystem
        public void print(Entity entity, Transform transform) {
            System.out.println(entity.getId() + " " + transform);
        }
    }
}
