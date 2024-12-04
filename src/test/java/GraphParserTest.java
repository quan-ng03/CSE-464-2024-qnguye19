import main.java.GraphParser;
import main.java.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

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
        parser.addNode("A","B","A"); // Adding duplicate node should print a warning

        // Check if nodes were added correctly
        String expectedNodes = "[A, B]";
        assertEquals(expectedNodes, parser.getNodes().toString(), "Nodes should be A and B.");
    }

    // Test for Feature 3: Add edges to the graph
    @Test
    public void testAddEdge() {
        parser.addNode("A","B");
        parser.addEdge("A", "B");

        // Check if edges were added correctly
        String expectedEdges = "[A -> B]";
        assertEquals(expectedEdges, parser.getEdges().toString(), "Edge A -> B should be present.");
    }

    // Test for Feature 4: Output the graph to a PNG file
    @Test
    public void testOutputGraphics() throws IOException {
        // Create a small graph
        parser.addNode("A","B");
        parser.addEdge("A", "B");

        // Output the graph to a PNG file
        File outputPngFile = new File(tempDir, "output.png");
        parser.outputGraphics(outputPngFile.getAbsolutePath(), "png");

        // Verify that the PNG file was created
        assertTrue(outputPngFile.exists(), "PNG file should be created.");
    }

    // Test removeNode API
    @Test
    public void testRemoveNode() {
        parser.addNode("A","B");
        parser.removeNode("A");

        String expectedNodesAfterRemoval = "[B]";
        assertEquals(expectedNodesAfterRemoval, parser.getNodes().toString(), "Node A should be removed.");
    }

    // Test removeNodes API
    @Test
    public void testRemoveNodes() {
        parser.addNode("A","B","C");
        parser.removeNodes(new String[]{"A", "C"});

        String expectedNodesAfterRemoval = "[B]";
        assertEquals(expectedNodesAfterRemoval, parser.getNodes().toString(), "Nodes A and C should be removed.");
    }

    // Test removeEdge API
    @Test
    public void testRemoveEdge() {
        parser.addNode("A","B");
        parser.addEdge("A", "B");
        parser.removeEdge("A", "B");

        String expectedEdgesAfterRemoval = "[]";
        assertEquals(expectedEdgesAfterRemoval, parser.getEdges().toString(), "Edge A -> B should be removed.");
    }

    // Scenario 2: Test removing non-existent nodes
    @Test
    public void testRemoveNonExistentNode() {
        assertThrows(IllegalArgumentException.class, () -> parser.removeNode("X"),
                "Removing non-existent node should throw an exception.");
    }

    // Scenario 3: Test removing non-existent edges
    @Test
    public void testRemoveNonExistentEdge() {
        parser.addNode("A","B");
        assertThrows(IllegalArgumentException.class, () -> parser.removeEdge("A", "C"),
                "Removing an edge with a non-existent destination node should throw an exception.");
    }

    // Test for BFS Graph Search feature
    @Test
    public void testGraphSearchPathExists() {
        // Create a new GraphParser instance
        GraphParser parser = new GraphParser();

        // Add nodes and edges to the graph
        parser.addNode("A", "B", "C");
        parser.addEdge("A", "B");
        parser.addEdge("B", "C");

        // Test BFS implementation
        GraphParser.BFS bfs = new GraphParser.BFS(parser.getGraph());
        Path bfsPath = bfs.search("A", "C");
        assertNotNull(bfsPath, "BFS should find a path between A and C.");
        assertEquals("A -> B -> C", bfsPath.toString(), "BFS path should be A -> B -> C.");
    }

        @Test
    public void testGraphSearchNoPathExists() {
        parser.addNode("A","B","C");
        parser.addEdge("A", "B");
        GraphParser.BFS bfs = new GraphParser.BFS(parser.getGraph());
        Path bfsPath = bfs.search("A", "C");
        assertNull(bfsPath, "No path should exist between A and C.");
    }

    @Test
    public void testGraphSearchSingleNodePath() {
        parser.addNode("A");

        Path path = parser.graphSearch("A", "A");
        assertNotNull(path, "Path should exist for a node to itself.");
        assertEquals("A", path.toString(), "Path should be A.");
    }

    @Test
    public void testGraphSearchNonExistentNode() {
        parser.addNode("A","B");

        Path path = parser.graphSearch("A", "C");
        assertNull(path, "Path should be null if destination node doesn't exist.");
    }

    // Test for DFS search feature
    @Test
    public void testGraphSearchDFS_PathExists() {
        parser.addNode("A", "B", "C");
        parser.addEdge("A", "B");
        parser.addEdge("B", "C");

        GraphParser.DFS dfs = new GraphParser.DFS(parser.getGraph());
        Path dfsPath = dfs.search("A", "C");
        assertNotNull(dfsPath, "DFS should find a path between A and C.");
        assertEquals("A -> B -> C", dfsPath.toString(), "DFS path should also be A -> B -> C (depends on graph structure).");
    }

    @Test
    public void testGraphSearchDFS_NoPath() {
        parser.addNode("A","B","C");
        parser.addEdge("A", "B");

        GraphParser.DFS dfs = new GraphParser.DFS(parser.getGraph());
        Path dfsPath = dfs.search("A", "C");
        assertNull(dfsPath, "Path should not exist.");
    }

    // Test parsing an empty graph
    @Test
    public void testParseEmptyGraph() throws IOException {
        File inputDotFile = new File(tempDir, "empty.dot");
        try (FileWriter writer = new FileWriter(inputDotFile)) {
            writer.write("digraph G {}");
        }

        parser.parseGraph(inputDotFile.getAbsolutePath());

        String expectedOutput = "Number of Nodes: 0\n" +
                "Nodes: []\n" +
                "Number of Edges: 0\n";
    }

    @Test
    public void testParseCyclicGraph() throws IOException {
        File inputDotFile = new File(tempDir, "cyclic.dot");
        try (FileWriter writer = new FileWriter(inputDotFile)) {
            writer.write("digraph G { a -> b; b -> c; c -> a; }");
        }

        parser.parseGraph(inputDotFile.getAbsolutePath());

        String expectedOutput = "Number of Nodes: 3\n" +
                "Nodes: [a, b, c]\n" +
                "Number of Edges: 3\n" +
                "a -> b\nb -> c\nc -> a\n";
    }

    // Test BFS for a large graph
    @Test
    public void testGraphSearchLargeGraph() {
        for (int i = 0; i < 1000; i++) {
            parser.addNode("Node" + i);
            if (i > 0) {
                parser.addEdge("Node" + (i - 1), "Node" + i);
            }
        }

        Path path = parser.graphSearch("Node0", "Node999");
        assertNotNull(path, "Path should exist in a large graph.");
        assertTrue(path.toString().startsWith("Node0 ->"), "Path should start with Node0.");
    }

    // Test invalid input for graph operations
    @Test
    public void testAddEdgeWithNonExistentNodes() {
        GraphParser parser = new GraphParser();

        assertThrows(IllegalArgumentException.class, () -> parser.addEdge("A", "B"),
                "Adding edge with non-existent nodes should throw an exception.");
    }

}
