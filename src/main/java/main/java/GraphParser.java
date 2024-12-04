package main.java;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTImporter;
import org.jgrapht.nio.dot.DOTExporter;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;

import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class GraphParser {
    private final DefaultDirectedGraph<String, DefaultEdge> graph;

    public Graph<String, DefaultEdge> getGraph() {
        return graph;
    }

    // Utility Method for File Reading
    private FileReader getFileReader(String filePath) throws IOException {
        return new FileReader(filePath);
    }

    // Utility Method for File Writing
    private FileWriter getFileWriter(String filePath) throws IOException {
        return new FileWriter(filePath);
    }

    public GraphParser() {
        // Initialize the graph as a directed graph with default edges
        graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    }

    // Parse a DOT graph file and print graph details
    public void parseGraph(String filepath) throws IOException {
        DOTImporter<String, DefaultEdge> importer = new DOTImporter<>();
        importer.setVertexFactory(String::new);

        // Use FileReader to read the DOT file
        try (FileReader fileReader = getFileReader(filepath)) {
            importer.importGraph(graph, fileReader);
        }

        // Print vertices and edges
        System.out.println("Number of Nodes: " + graph.vertexSet().size());
        System.out.println("Nodes: " + graph.vertexSet());
        System.out.println("Number of Edges: " + graph.edgeSet().size());
        graph.edgeSet().forEach(edge -> System.out.println(graph.getEdgeSource(edge) + " -> " + graph.getEdgeTarget(edge)));
    }

    // Add nodes to the graph
    public void addNode(String... labels) {
        for (String label : labels) {
            if (!graph.containsVertex(label)) {
                graph.addVertex(label);
                System.out.println("Node added: " + label);
            } else {
                System.out.println("Node " + label + " already exists!");
            }
        }
    }

    // Add edges to the graph
    public void addEdge(String srcLabel, String dstLabel) {
        if (!graph.containsVertex(srcLabel) || !graph.containsVertex(dstLabel)) {
            throw new IllegalArgumentException("One or both nodes do not exist: " + srcLabel + ", " + dstLabel);
        } else {
            if (graph.addEdge(srcLabel, dstLabel) != null) {
                System.out.println("Edge added: " + srcLabel + " -> " + dstLabel);
            } else {
                System.out.println("Edge already exists!");
            }
        }
    }

    // Output the graph to a DOT file and a PNG image
    public void outputDOTGraph(String path) throws IOException {
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>();
        try (FileWriter writer = getFileWriter(path)) {
            exporter.exportGraph(graph, writer);
            System.out.println("Graph exported to DOT file: " + path);
        }
    }


    public void outputGraphics(String path, String format) throws IOException {
        MutableGraph g = Factory.mutGraph("Graph").setDirected(true);

        // Add all vertices (nodes)
        for (String vertex : graph.vertexSet()) {
            g.add(Factory.mutNode(vertex));
        }

        // Add all edges
        for (DefaultEdge edge : graph.edgeSet()) {
            String src = graph.getEdgeSource(edge);
            String tgt = graph.getEdgeTarget(edge);
            g.add(Factory.mutNode(src).addLink(Factory.to(Factory.mutNode(tgt))));
        }

        // Render the graph in the specified format
        Graphviz viz = Graphviz.fromGraph(g);
        if (format.equalsIgnoreCase("png")) {
            viz.render(Format.PNG).toFile(new File(path));
        } else {
            System.out.println("Unsupported format: " + format);
            return;
        }
        System.out.println("Graph exported to graphics file: " + path);
    }

    // Get the nodes of the graph
    public Set<String> getNodes() {
        return graph.vertexSet();
    }

    // Get the edges of the graph
    public Set<String> getEdges() {
        return graph.edgeSet().stream()
                .map(edge -> graph.getEdgeSource(edge) + " -> " + graph.getEdgeTarget(edge))
                .collect(Collectors.toSet());
    }

    // Remove a node from the graph
    public void removeNode(String label) {
        if (graph.containsVertex(label)) {
            graph.removeVertex(label);
            System.out.println("Node removed: " + label);
        } else {
            throw new IllegalArgumentException("Node " + label + " does not exist!");
        }
    }

    // Remove multiple nodes from the graph
    public void removeNodes(String[] labels) {
        for (String label : labels) {
            removeNode(label);  // Reuse the removeNode method
        }
    }

    // Remove an edge from the graph
    public void removeEdge(String srcLabel, String dstLabel) {
        DefaultEdge edge = graph.getEdge(srcLabel, dstLabel);
        if (edge != null) {
            graph.removeEdge(edge);
            System.out.println("Edge removed: " + srcLabel + " -> " + dstLabel);
        } else {
            throw new IllegalArgumentException("Edge " + srcLabel + " -> " + dstLabel + " does not exist!");
        }
    }


    // Base class for the Template Method Pattern
    public abstract static class GraphSearchTemplate {
        protected Set<String> visited; // Tracks visited nodes
        protected Deque<List<String>> nodes; // Abstracted data structure for BFS/DFS
        protected Graph<String, DefaultEdge> graph; // Graph object

        public GraphSearchTemplate(Graph<String, DefaultEdge> graph) {
            this.graph = graph;
            this.visited = new HashSet<>();
        }

        // Template method: Defines the skeleton of the algorithm
        public Path search(String src, String dst) {
            if (!graph.containsVertex(src) || !graph.containsVertex(dst)) {
                System.out.println("One or both nodes not present in the graph.");
                return null;
            }

            initializeSearch(src);

            while (!nodes.isEmpty()) {
                List<String> path = fetchNextNode();
                String lastNode = path.get(path.size() - 1);

                if (lastNode.equals(dst)) {
                    return new Path(path); // Path found
                }

                for (DefaultEdge edge : graph.outgoingEdgesOf(lastNode)) {
                    String neighbor = graph.getEdgeTarget(edge);
                    if (!visited.contains(neighbor)) {
                        List<String> newPath = new ArrayList<>(path);
                        newPath.add(neighbor);
                        addNeighbor(newPath);
                        visited.add(neighbor);
                    }
                }
            }

            return null; // No path found
        }

        // Steps to be implemented by subclasses
        protected abstract void initializeSearch(String startNode);
        protected abstract List<String> fetchNextNode();
        protected abstract void addNeighbor(List<String> neighborPath);
    }

    // BFS Implementation
    public static class BFS extends GraphSearchTemplate {
        public BFS(Graph<String, DefaultEdge> graph) {
            super(graph);
        }

        @Override
        protected void initializeSearch(String startNode) {
            nodes = new ArrayDeque<>(); // Queue
            nodes.add(Collections.singletonList(startNode));
            visited.add(startNode);
        }

        @Override
        protected List<String> fetchNextNode() {
            return nodes.poll(); // FIFO
        }

        @Override
        protected void addNeighbor(List<String> neighborPath) {
            nodes.add(neighborPath);
        }
    }

    // DFS Implementation
    public static class DFS extends GraphSearchTemplate {
        public DFS(Graph<String, DefaultEdge> graph) {
            super(graph);
        }

        @Override
        protected void initializeSearch(String startNode) {
            nodes = new ArrayDeque<>(); // Stack
            nodes.push(Collections.singletonList(startNode));
            visited.add(startNode);
        }

        @Override
        protected List<String> fetchNextNode() {
            return nodes.pop(); // LIFO
        }

        @Override
        protected void addNeighbor(List<String> neighborPath) {
            nodes.push(neighborPath);
        }
    }

    public enum Algorithm {
        BFS,
        DFS
    }

    public Path graphSearch(String src, String dst, Algorithm algo) {
        // Validate input nodes
        if (!getGraph().containsVertex(src)) {
            throw new IllegalArgumentException("Source node '" + src + "' does not exist in the graph.");
        }
        if (!getGraph().containsVertex(dst)) {
            throw new IllegalArgumentException("Destination node '" + dst + "' does not exist in the graph.");
        }

        // Choose the search strategy
        GraphSearchTemplate strategy = switch (algo) {
            case BFS -> new BFS(getGraph());
            case DFS -> new DFS(getGraph());
            default -> throw new IllegalArgumentException("Unsupported algorithm: " + algo);
        };

        // Perform the search using the selected strategy
        return strategy.search(src, dst);
    }

}
