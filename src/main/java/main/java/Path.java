package main.java;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private final List<String> nodes;

    public Path() {
        nodes = new ArrayList<>();
    }

    public Path(List<String> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }

    public void addNode(String node) {
        nodes.add(node);
    }

    @Override
    public String toString() {
        return String.join(" -> ", nodes);
    }

    public List<String> getNodes() {
        return nodes;
    }
}
