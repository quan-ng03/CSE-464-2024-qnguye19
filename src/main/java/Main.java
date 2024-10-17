import main.java.GraphParser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GraphParser parser = new GraphParser();
        parser.parseGraph("src/main/resources/input.dot");

        parser.addNode("d");
        parser.addNodes(new String[]{"E", "F"});
    }
}