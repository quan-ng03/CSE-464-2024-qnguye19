package main.java;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class RandomWalk extends GraphParser.GraphSearchTemplate {
    private final Random random = new Random();
    private String startNode;

    public RandomWalk(Graph<String, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    public void initializeSearch(String src) {
        // Validate the starting node
        if (!graph.containsVertex(src)) {
            throw new IllegalArgumentException("Source node '" + src + "' does not exist in the graph.");
        }
        this.startNode = src; // Set the starting node for the search
        System.out.println("Search initialized with starting node: " + src);
    }

    @Override
    protected List<String> fetchNextNode() {
        return List.of();
    }

    @Override
    protected void addNeighbor(List<String> neighborPath) {

    }

    @Override
    public Path search(String src, String dst) {
        initializeSearch(src);

        List<String> visitedNodes = new ArrayList<>();
        String current = startNode;
        visitedNodes.add(current);

        int maxSteps = 100; // Maximum number of steps to prevent infinite loops
        int stepCount = 0;

        while (!current.equals(dst) && stepCount < maxSteps) {
            System.out.println("Visiting " + visitedNodes);
            stepCount++;

            // Get neighbors of the current node
            Set<DefaultEdge> edges = graph.edgesOf(current);
            List<String> neighbors = new ArrayList<>();
            for (DefaultEdge edge : edges) {
                String neighbor = graph.getEdgeTarget(edge);
                if (!neighbor.equals(current)) {
                    neighbors.add(neighbor);
                } else {
                    neighbors.add(graph.getEdgeSource(edge));
                }
            }

            if (neighbors.isEmpty()) {
                break; // Dead-end
            }

            // Pick a random neighbor
            current = neighbors.get(random.nextInt(neighbors.size()));

            // Avoid revisiting nodes
            if (!visitedNodes.contains(current)) {
                visitedNodes.add(current);
            }
        }

        // Check if the destination was reached
        if (current.equals(dst)) {
            System.out.println("Path found: " + visitedNodes);
            return new Path(visitedNodes);
        } else {
            System.out.println("No path found.");
            return null;
        }
    }

}
