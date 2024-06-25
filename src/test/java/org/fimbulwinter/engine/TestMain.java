package org.fimbulwinter.engine;

import org.fimbulwinter.engine.ecs.Engine;
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
        game.tick();
        game.tick();
        game.tick();
        game.tick();
    }


    public static class TestSystems implements SystemContainer {
        @AutoInject
        public void move(Transform transform) {
            transform.setPosition(transform.getPosition().add(0, 1, 0));
        }

        @AutoInject
        public void print(Transform transform) {
            System.out.println(transform);
        }
    }
}
