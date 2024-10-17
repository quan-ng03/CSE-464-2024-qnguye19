package test.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphParserTest {

    private GraphParser parser;

    @TempDir
    File tempDir;  // Temporary directory for input/output files

    @BeforeEach
    public void setUp() {
        parser = new GraphParser();  // Initialize GraphParser before each test
    }

    // Test for Feature 1: Parse a DOT graph file and print graph details
    @Test
    public void testParseGraph() throws IOException {
        // Create a temporary DOT file
        File inputDotFile = new File(tempDir, "input.dot");
        try (FileWriter writer = new FileWriter(inputDotFile)) {
            writer.write("digraph G { a -> b; b -> c; c -> d; }");
        }

        // Parse the graph
        parser.parseGraph(inputDotFile.getAbsolutePath());

        // Check if the graph was parsed correctly
        String expectedOutput = "Number of Nodes: 4\n" +
                "Nodes: [a, b, c, d]\n" +
                "Number of Edges: 3\n" +
                "a -> b\nb -> c\nc -> d\n";
        // Capture System.out for assertions (you can use a custom output stream if needed)
        // Assert that the output matches the expected result.
    }

    // Test for Feature 2: Add nodes to the graph
    @Test
    public void testAddNode() {
        parser.addNode("A");
        parser.addNode("B");
        parser.addNode("A");  // Adding duplicate node should print a warning

        // Check if nodes were added correctly
        String expectedNodes = "[A, B]";
        assertEquals(expectedNodes, parser.getNodes().toString(), "Nodes should be A and B.");
    }

    // Test for Feature 3: Add edges to the graph
    @Test
    public void testAddEdge() {
        parser.addNode("A");
        parser.addNode("B");
        parser.addEdge("A", "B");

        // Check if edges were added correctly
        String expectedEdges = "[A -> B]";
        assertEquals(expectedEdges, parser.getEdges().toString(), "Edge A -> B should be present.");
    }

    // Test for Feature 4: Output the graph to a DOT file
    @Test
    public void testOutputDOTGraph() throws IOException {
        // Create a small graph
        parser.addNode("A");
        parser.addNode("B");
        parser.addEdge("A", "B");

        // Output the graph to a DOT file
        File outputDotFile = new File(tempDir, "output.dot");
        parser.outputDOTGraph(outputDotFile.getAbsolutePath());

        // Read the output DOT file and compare with expected content
        String expectedDot = "digraph G {\n" +
                "  A -> B;\n" +
                "}";
        String outputDot = Files.readString(Paths.get(outputDotFile.getAbsolutePath()));
        assertEquals(expectedDot.trim(), outputDot.trim(), "DOT output should match expected graph structure.");
    }

    // Test for Feature 4: Output the graph to a PNG file
    @Test
    public void testOutputGraphics() throws IOException {
        // Create a small graph
        parser.addNode("A");
        parser.addNode("B");
        parser.addEdge("A", "B");

        // Output the graph to a PNG file
        File outputPngFile = new File(tempDir, "output.png");
        parser.outputGraphics(outputPngFile.getAbsolutePath(), "png");

        // Verify that the PNG file was created
        assertTrue(outputPngFile.exists(), "PNG file should be created.");
    }
}
