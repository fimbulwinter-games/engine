package org.fimbulwinter.engine.ecs.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceStorage {
    private final Map<String, Resource> resources = new HashMap<String, Resource>();

    public void insertResource(Resource resource) {
        resources.put(resource.getResourceIdentifier(), resource);
    }

    public List<? extends Resource> getResources() {
        return new ArrayList<>(resources.values());
    }
}
