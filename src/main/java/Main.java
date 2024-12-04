import main.java.GraphParser;
import main.java.RandomWalk;
import main.java.GraphParser.Algorithm;
import main.java.Path;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Initialize the parser and graph
        GraphParser parser = new GraphParser();
        parser.parseGraph("src/main/resources/randomWalkInput.dot");
        // Perform random walk searches and display the results
        System.out.println("Performing Random Walk Searches:");
        RandomWalk randomWalk = new RandomWalk(parser.getGraph());
        String src = "a";
        String dst = "h";

        for (int i = 0; i < 3; i++) { // Run multiple random walks to test randomness
            System.out.println("Random Walk #" + (i + 1));
            Path path = randomWalk.search(src, dst);

            if (path != null) {
                System.out.println("Path found: " + path.getNodes());
            } else {
                System.out.println("No path found.");
            }

            System.out.println();
        }
    }
}