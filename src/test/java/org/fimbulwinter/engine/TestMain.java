package org.fimbulwinter.engine;

import org.fimbulwinter.engine.ecs.Engine;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.components.Transform;
import org.fimbulwinter.engine.ecs.system.AutoInject;
import org.fimbulwinter.engine.ecs.system.SystemContainer;

import java.util.Set;

public class TestMain {
    public static void main(String[] args) {
        final var game = new Engine();

        game.instantiate(Set.of(new Transform()));
        game.registerSystems(new TestSystems());
        game.tick();
        game.registerSystem((_, components) -> {
            components.stream()
                    .filter(x -> x instanceof Transform)
                    .map(x -> (Transform) x)
                    .forEach(System.out::println);
        });
        game.tick();
    }


    public static class TestSystems implements SystemContainer {
        @AutoInject
        public void move(Transform transform) {
            transform.setPosition(transform.getPosition().add(0, 1, 0));
        }

        @AutoInject
        public void scale(Transform transform) {
            transform.setScale(transform.getScale().add(0, 1, 0));
        }

        public void rotate(Entity entity) {
        }
    }
}
