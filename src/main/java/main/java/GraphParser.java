package main.java;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTImporter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class GraphParser {
    private DefaultDirectedGraph<String, DefaultEdge> graph;

    public GraphParser() {
        // Initialize the graph as a directed graph with default edges
        graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    }

    // Feature 1: Parse a DOT graph file and print graph details
    public void parseGraph(String filepath) throws IOException {
        DOTImporter<String, DefaultEdge> importer = new DOTImporter<>();
        importer.setVertexFactory(String::new);

        // Use FileReader to read the DOT file
        try (FileReader fileReader = new FileReader(filepath)) {
            importer.importGraph(graph, fileReader);
        }

        // Print vertices and edges
        System.out.println("Number of Nodes: " + graph.vertexSet().size());
        System.out.println("Nodes: " + graph.vertexSet());
        System.out.println("Number of Edges: " + graph.edgeSet().size());
        graph.edgeSet().forEach(edge -> {
            System.out.println(graph.getEdgeSource(edge) + " -> " + graph.getEdgeTarget(edge));
        });
    }

    public void addNode(String label) {
        if (!graph.containsVertex(label)) {
            graph.addVertex(label);
            System.out.println("Node added: " + label);
        } else {
            System.out.println("Node " + label + " already exists!");
        }
    }

    public void addNodes(String[] labels) {
        for (String label : labels) {
            addNode(label);
        }
    }


}
