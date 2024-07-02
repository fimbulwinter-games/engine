package org.fimbulwinter.engine;

import org.fimbulwinter.engine.ecs.Engine;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.component.Transform;
import org.fimbulwinter.engine.ecs.system.RegisterSystem;

public class TestMain {
    public static void main(String[] args) {
        final var game = new Engine();

        game.registerSystems(TestSystems.class);

        game.instantiate(new Transform());
        game.instantiate(new Transform());
        game.tick();
    }


    public static class TestSystems {
        @RegisterSystem
        public static void print(Entity entity) {
            System.out.println(entity.getId());
        }
    }
}
