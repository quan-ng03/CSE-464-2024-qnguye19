Welcome to G-View! This Java application, built with Maven, displays the visual representation of different graphs, including DFS, BFS, and Binary Trees!

After cloning, if you have trouble with libraries from JGraphT, please go into External Libraries -> Find Maven: org.jgrapht:jgrapht-io:1.5.2 -> org.jgrapht -> nio -> dot -> Open DOTImporter and DOTExporter and Download Resources.

Hey! Here's my take on refractoring the code:

Part I:

Refractor 1: https://github.com/quan-ng03/CSE-464-2024-qnguye19/commit/ed0044a28172c28898f1262c8629f42643e2642d 
Last time in running GitHub Actions none of my tests were used during the build due to it having duplicate dependency in pom.xml. Thus, I went ahead and refractored this so now whenever I push changes to remote branches or request to merge to main or refractor branch, it will run the workflow including the test cases. This type is called removing duplication.

Refractor 2: https://github.com/quan-ng03/CSE-464-2024-qnguye19/commit/0b1687982a0f022d8b3e627dbf957ecfc209df96
Within the function outputDOTGraph, my initial implementation can cause conflicts with specific edge cases due to it not being closed properly within the process, causing the test runs to fail to delete the tempFile due to the process still accessing it. This Type is called simplifying methods.

Refractor 3: https://github.com/quan-ng03/CSE-464-2024-qnguye19/commit/06ad77fec3756cbcca3a84bfbdf3357d95ec121d
This type of refratoring is simplifying methods. I noticed the FileWrite and FileRead were being redundant in GraphParser, thus I created specific utility methods for them.

Refractor 4: https://github.com/quan-ng03/CSE-464-2024-qnguye19/commit/66158f507838d3adceaf6410f95dafd46b97894b
This type of refractoring is method simplification. When checking the existing methods within GraphParser and test cases in GraphParserTest, I noticed the redundant codes in adding singular node vs multiple nodes. Thus I researched and came up with using a loop within the function to go through each param in the call.

Refractor 5: https://github.com/quan-ng03/CSE-464-2024-qnguye19/commit/0b41c9a81b0bf0dff6b4c2ceccbc0fcbcab5ef6e
This type of refracting is test refractoring. Upon looking at the existing tests along with future changes, I noticed that there are missing test cases that will reveal the faults within the code.

Part II: Refractored BFS and DFS
Link: https://github.com/quan-ng03/CSE-464-2024-qnguye19/commit/556d4675ecd9196d50f4e635fa782a55c55bbba3
For this part of assignment, my approach was to following step-by-step with the guide. Here's what I did:
  1. Created an abstract function named GraphSearchTemplate containing all the redundant variables and methods.
  2. Created BFS class extends GraphSearchTemplate with override methods to use Queue implementation for searching nodes as it resembles the methodology of BFS.
  3. The same thing with DFS but the method is Stack implementation (LIFO)
  4. Adjust test cases method names and renaming for better visibility
![image](https://github.com/user-attachments/assets/2e8e9b64-0a87-4df6-8601-4578589c738f)

Part III: Refractored Graph Search ENUM
Link: https://github.com/quan-ng03/CSE-464-2024-qnguye19/commit/525875b1f46070e00c897a43d733f8bdb25f2a76
For this, I simply created a public enum named Algorithm declaring the BFS and DFS and increased the params of graphSearch function to specify which type of search I want to use. Finally, I added switch statement to check for the case with a default case as throwing an Exception into logs.
An example of test case using the new graphSearch: ![image](https://github.com/user-attachments/assets/08240b57-5a49-4360-bbe3-d9fbae8d12d3)
![image](https://github.com/user-attachments/assets/bc76402a-b9dc-41df-b15c-0a6be802901b)

Part IV: Added Random Walk Algorithm
Link: https://github.com/quan-ng03/CSE-464-2024-qnguye19/commit/32151a0edefcf3f23dc784068b664a77b634961e
For this, I added an enum named RANDOM_WALK into the Algorithm list and a case for it, following the previous implementation. In the main implementation of the function, I created a new Class named RandomWalk which extends from GraphSearchTemplate. The important thing was that I had to override all the functions within GraphSearchTemplate or else it will cause error and force the function to be abstract, which is not what I wanted. In the search method, I still initialized the variables the same but I had to include a limit for steps as I don't want a chance of it falling into infinite loop, thus I set it at 100. Then, I start the loop that first collect all neighbors of the current node and then break if it came back empty. If not, I'll let the program pick a random neighbor using Random. At the end, I also included an if statement to prevent it from revisiting the nodes it already visited.

This is the run for provided input.dot:
![image](https://github.com/user-attachments/assets/2d7ce617-03fc-44b6-8e2a-c60891a3d581)

These are the outputs:

![image](https://github.com/user-attachments/assets/e82e8ab3-dd18-4533-b502-26989158d4f4)

![image](https://github.com/user-attachments/assets/cdc3d3f0-c9b7-434f-b977-495d0209a292)

![image](https://github.com/user-attachments/assets/d8c7f77c-00d9-46ae-82d9-67c2b393940b)
