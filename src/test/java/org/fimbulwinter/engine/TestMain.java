package org.fimbulwinter.engine;

import org.fimbulwinter.engine.ecs.Engine;
import org.fimbulwinter.engine.ecs.component.base.Transform;
import org.fimbulwinter.engine.ecs.entity.Entity;
import org.fimbulwinter.engine.ecs.resource.base.Window;
import org.fimbulwinter.engine.ecs.system.RegisterSystem;
import org.fimbulwinter.engine.ecs.system.SystemStage;
import org.joml.Vector3f;

public class TestMain {
    public static void main(String[] args) {
        final var game = new Engine();

        game.registerSystems(TestSystems.class);
        game.registerResource(new Window());
        game.tick();
        game.instantiate(new Transform(new Vector3f(0, 0, 0), new Vector3f(), new Vector3f()));
        game.tick();
        game.instantiate(new Transform(new Vector3f(1, 0, 0), new Vector3f(), new Vector3f()));
        game.tick();
        game.instantiate(new Transform(new Vector3f(1, 0, 0), new Vector3f(), new Vector3f()));
        game.tick();
    }

    public static class TestSystems {
        @RegisterSystem
        public static void print(Entity entity, Transform t) {
            System.out.println("Entity: " + entity.getId() + " | " + t);
        }

        @RegisterSystem(systemStage = SystemStage.RENDER)
        public static void renderPass(Entity entity, Transform t) {
            System.out.println("Renderpass: " + entity.getId() + " | " + t);
        }
    }
}
