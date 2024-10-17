import main.java.GraphParser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GraphParser parser = new GraphParser();
        parser.parseGraph("src/main/resources/input.dot");

        parser.addNode("d");
        parser.addNodes(new String[]{"E", "F"});

        parser.addEdge("a", "c");
        parser.addEdge("d", "E");

        parser.outputDOTGraph("src/main/resources/output.dot");

        // Export to PNG file
        parser.outputGraphics("src/main/resources/output.png", "png");
    }
}