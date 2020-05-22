import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * The main class that reads the input file and creates an
 * 2D array based on the edges and flow per edge
 * @author Manisha Kusuma
 *
 */
public class Algorithm {
    public static void main(String[] args) throws FileNotFoundException {
        String fileName = args[0];
        File input = new File(fileName);
        Scanner scan = new Scanner(input);
        String file = scan.next(); //Reads input file
        String[] numbers = file.split(",");
        int vertices = Integer.valueOf(numbers[0]);
        int[][] edges = new int[vertices][vertices];
        int count = 1;
        //Adds the flow to the corresponding entry in the 2D array of edges
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                edges[i][j] = Integer.valueOf(numbers[count]);
                count++;
            }
        }
        //Creates a calc flow object
        FlowCalc m = new FlowCalc();
        
        //Calls the ford fulkerson method in FlowCalc to get the total flow
        System.out.println("Total Flow: " + m.fordFulkerson(edges, 0, vertices
            - 1));
    }
}
