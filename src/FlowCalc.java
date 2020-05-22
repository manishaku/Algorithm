import java.util.LinkedList;
import java.util.Stack;
/**
 * Calculated the maximum flow using the ford-fulkerson algorithm
 * @author Manisha Kusuma
 *
 */
public class FlowCalc {
    private int vert = 0;

    /**
     * Ford Fulkerson algorithm that finds each augmented 
     * path and the flow through each path
     * @param graph flows for each edge
     * @param start source
     * @param end sink
     * @return maximum flow
     */
    int fordFulkerson(int graph[][], int start, int end) {
        vert = end + 1;
        int i = 0;
        int j = 0;
        int newGraph[][] = new int[vert][vert]; // holds current flows for each edge

        for (i = 0; i < vert; i++)
            for (j = 0; j < vert; j++)
                newGraph[i][j] = graph[i][j];

        int parent[] = new int[vert];

        int max_flow = 0;

        //Checks to see if the sink has been visited yet 
        //or every vertex has been scanned
        while (breathFirst(newGraph, start, end, parent)) {
            int path_flow = Integer.MAX_VALUE;
            Stack<Integer> nums = new Stack<Integer>();
            j = end;
            while (j != start) {
                i = parent[j];
                //finds the minimum path flow possible given the conditions
                path_flow = Math.min(path_flow, newGraph[i][j]); 
                nums.push(j); //adds the flow value to the stack
                j = parent[j];
            }
            nums.push(start);
            j = end;
            
            //Backtracks and sets the amount of flow actually used at every vertex
            while (j != start) {
                i = parent[j];
                newGraph[i][j] -= path_flow;
                newGraph[j][i] += path_flow;
                j = parent[j];
            }

            max_flow = max_flow + path_flow; //Adds the paths plow to the maximum value
            System.out.print("Augmenting Path found: ");
            while (!nums.isEmpty()) {
                System.out.print(nums.pop() + " ");
            }
            System.out.println("with flow: " + path_flow);
        }

        return max_flow; //returns maximum flow from the source to the sink
    }

    /**
     * Does a breath first search of the vertices and determines
     * whether or not they have been visited yet 
     * @param graph weights at each edge
     * @param start source
     * @param end sink
     * @param parent list of previously visited vertices in the search
     * @return whether the sink has been visited yet
     */
    boolean breathFirst(int graph[][], int start, int end, int parent[]) {
        boolean visited[] = new boolean[vert];
        //Sets the vertices to originally not being visited
        for (int i = 0; i < vert; ++i) {
            visited[i] = false;
        }

        LinkedList<Integer> list = new LinkedList<Integer>();
        list.add(start);
        visited[start] = true;
        parent[start] = -1;

        while (list.size() != 0) {
            int i = list.poll();

            for (int j = 0; j < vert; j++) {
                //if the flow of a current vertex is above 0 it has been changed to visited
                if (visited[j] == false && graph[i][j] > 0) {
                    list.add(j);
                    parent[j] = i;
                    visited[j] = true;
                }
            }
        }
        return (visited[end] == true); //returns if the sink has been visited yet
    }
}
