import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
/**
 * Main file that contains the bulk of Kruskal's Algorithm
 * @author Manisha Kusuma
 *
 */
public class AlgorithmKruskal {

    public static void main(String[] args) {
        String fileName = args[0];
        File input = new File(fileName);
        try {
            //Reads the input file and creates a list of weights and edges
            Scanner scan = new Scanner(input);
            String file = scan.next();
            String[] numbers = file.split(",");
            int vertices = Integer.valueOf(numbers[0]);
            System.out.println(vertices + " vertices found.");
            List<Integer> values = new ArrayList<Integer>();
            List<String> edges = new ArrayList<String>();
            for (int i = 0; i < vertices * vertices; i = i + vertices) {
                for (int j = 0; j < vertices; j++) {
                    int value = Integer.valueOf(numbers[j + i + 1]);
                    if (value != 0) {
                        values.add(value);
                        edges.add(i / vertices + "-" + j);
                    }
                }
            }
            //Creates a tree object that contains a List of edge objects
            Tree tree = new Tree(values, edges);
            tree.createTree();
            //Sorts the list of edge objects by their weight 
            List<Edge> edge = tree.sort();
            int weight = 0;
            List<List<String>> subgraphs = new ArrayList<List<String>>(); //list of subgraps 
            int j = 1;
            List<String> disGraph = new ArrayList<String>();
            disGraph.add(edge.get(0).getVertex1());
            disGraph.add(edge.get(0).getVertex2());
            subgraphs.add(disGraph);
            System.out.println("Adding edge (" + edge.get(0).getVertex1() + ","
                + edge.get(0).getVertex2() + ") with weight " + edge.get(0)
                    .getWeight() + ".");
            weight += edge.get(0).getWeight();
            Edge eg = edge.get(j);
            
            //Creates a loop that searches for edges till all of the vertices have been selected
            while (subgraphs.get(0).size() < vertices) {
                eg = edge.get(j); //gets the next edge
                String v1 = eg.getVertex1();
                String v2 = eg.getVertex2();
                boolean cycle = false;
                //Adds the edge to the corresponding subgraph 
                for (int a = 0; a < subgraphs.size(); a++) {
                    //If both vertices are already contained in a subgraph this edge
                    //would form a cycle so it is not added to the subgraph
                    if (subgraphs.get(a).contains(v1) && subgraphs.get(a)
                        .contains(v2)) {
                        cycle = true;
                        break;
                    }
                    //Checks if the subgraph contains vertex 1 if so adds vertex 2 to the subgraph
                    else if (subgraphs.get(a).contains(v1)) {
                        subgraphs.get(a).add(v2);
                        System.out.println("Adding edge (" + v1 + "," + v2
                            + ") with weight " + edge.get(j).getWeight() + ".");
                        weight += edge.get(j).getWeight();
                        cycle = true;
                        break;
                    }
                  //Checks if the subgraph contains vertex 2 if so adds vertex 1 to the subgraph
                    else if (subgraphs.get(a).contains(v2)) {
                        subgraphs.get(a).add(v1);
                        System.out.println("Adding edge (" + v1 + "," + v2
                            + ") with weight " + edge.get(j).getWeight() + ".");
                        weight += edge.get(j).getWeight();
                        cycle = true;
                        break;
                    }
                }

                //Creates a new subgraph if needed and adds the edge
                if (!cycle) {
                    List<String> disgraph = new ArrayList<String>();
                    disgraph.add(v1);
                    disgraph.add(v2);
                    subgraphs.add(disgraph);
                    System.out.println("Adding edge (" + v1 + "," + v2
                        + ") with weight " + edge.get(j).getWeight() + ".");
                    weight += edge.get(j).getWeight();
                }
                
                //Checks if any of the subgraphs need to be merged to form a connected graph
                if (subgraphs.size() > 0) {
                    tree.tryMerge(subgraphs);
                }
                j++;
            }

            System.out.println("Total weight of spanning tree: " + weight);
            scan.close();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
