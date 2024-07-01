package org.fimbulwinter.engine;

import org.fimbulwinter.engine.ecs.Engine;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.component.Transform;
import org.fimbulwinter.engine.ecs.system.RegisterSystem;

import java.util.Set;

public class TestMain {
    public static void main(String[] args) {
        final var game = new Engine();

        game.registerSystems(TestSystems.class);

        game.tick();
        game.tick();
        game.instantiate(Set.of(new Transform()));
        game.instantiate(Set.of(new Transform()));
        game.tick();
        game.tick();
    }


    public static class TestSystems {
        @RegisterSystem
        public static void move(Transform transform) {
            transform.setPosition(transform.getPosition().add(0, 1, 0));
        }

        @RegisterSystem
        public static void print(Entity entity, Transform transform) {
            System.out.println(entity.getId() + " " + transform);
        }
    }
}
