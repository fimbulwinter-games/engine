package org.fimbulwinter.engine;

import org.fimbulwinter.engine.ecs.Engine;
import org.fimbulwinter.engine.ecs.component.base.Transform;
import org.fimbulwinter.engine.ecs.resource.base.Window;
import org.joml.Vector3f;

public class TestMain {
    public static void main(String[] args) {
        final var game = new Engine();

        game.registerSystems(TestSystems.class);
        game.registerSystems(Window.class);
        game.instantiate(new Window());
        game.instantiate(new Transform(new Vector3f(0, 0, 0)));
        game.run();
    }

    public static class TestSystems {
//        @RegisterSystem
//        public static void print(Entity entity, Transform t) {
//            System.out.println("Entity: " + entity.getId() + " | " + t);
//        }
//
//        @RegisterSystem(systemStage = SystemStage.RENDER)
//        public static void renderPass(Entity entity, Transform t) {
//            System.out.println("Renderpass: " + entity.getId() + " | " + t);
//        }
    }
}
