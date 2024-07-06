package org.fimbulwinter.engine.ecs.component;

import java.util.*;

public class ComponentSet implements Iterable<Component> {

    private final List<Component> components = new ArrayList<>();
    private final Set<String> typeSet = new HashSet<>();


    public ComponentSet(Collection<? extends Component> components) {
        for (final var component : components) {
            this.add(component);
        }
    }

    public ComponentSet() {
    }

    public void add(Component component) {
        if (typeSet.contains(component.getComponentIdentifier())) {
            throw new IllegalArgumentException(component.getComponentIdentifier() + "' is already present");
        } else {
            components.add(component);
            typeSet.add(component.getComponentIdentifier());
        }
    }

    public List<? extends Component> getComponents() {
        return Collections.unmodifiableList(components);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComponentSet that = (ComponentSet) o;
        return typeSet.equals(that.typeSet);
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }

    @Override
    public int hashCode() {
        return typeSet.hashCode();
    }
}
