package org.fimbulwinter.engine;

import org.fimbulwinter.engine.ecs.Engine;
import org.fimbulwinter.engine.ecs.Entity;
import org.fimbulwinter.engine.ecs.component.Transform;
import org.fimbulwinter.engine.ecs.resource.Resource;
import org.fimbulwinter.engine.ecs.system.RegisterSystem;
import org.fimbulwinter.engine.ecs.system.SystemStage;
import org.joml.Vector3f;

public class TestMain {
    public static void main(String[] args) {
        final var game = new Engine();

        game.registerSystems(TestSystems.class);

        game.instantiate(new Transform(new Vector3f(0, 0, 0), new Vector3f(), new Vector3f()));
        game.instantiate(new Transform(new Vector3f(1, 0, 0), new Vector3f(), new Vector3f()));

        game.tick();
        game.registerResource(new TestResource());
        game.tick();
        game.tick();
        game.tick();
    }

    public static class TestSystems {
        @RegisterSystem
        public static void print(Entity entity, Transform t) {
            System.out.println("Entity: " + entity.getId() + " | " + t);
        }

        @RegisterSystem(pass = SystemStage.RENDER)
        public static void printResource(TestResource testResource) {
            System.out.println("Resource: " + testResource.a);
        }

        @RegisterSystem
        public static void entityModifiesResource(Entity entity, TestResource testResource) {
            System.out.println("Entity " + entity.getId() + " modifies resource: " + testResource.a++);
        }
    }

    public static class TestResource implements Resource {
        public int a = 0;
    }
}
