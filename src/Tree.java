import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * Tree class that maintains all the edges in the tree
 * @author Manisha Kusuma
 *
 */
public class Tree {
    private List<Integer> values;
    private List<String> vertices;
    private List<Edge> treeEdges = new ArrayList<Edge>();

    /**
     * Constructor for Tree Object
     * @param val weights
     * @param vertex vertices of the edges
     */
    public Tree(List<Integer> val, List<String> vertex) {
        values = val;
        vertices = vertex;
    }

    /**
     * Creates a tree with edges and weights
     */
    public void createTree() {
        for (int i = 0; i < values.size(); i++) {
            String[] vert = vertices.get(i).split("-");
            Edge edg = new Edge(vert[0], vert[1], values.get(i));
            treeEdges.add(edg);
        }
    }

    /**
     * Sorts the list of edges by weight
     * @return list of edges
     */
    public List<Edge> sort() {
        Collections.sort(treeEdges, new Comparator<Edge>() {
            @Override
            public int compare(Edge e1, Edge e2) {
                if (e1.getWeight() == e2.getWeight()) {
                    return 0;
                }
                else if (e1.getWeight() > e2.getWeight()) {
                    return 1;
                }
                return -1;
            }
        });
        return treeEdges;
    }
    
    /**
     * Merges 2 subgraphs in the tree if needed 
     * @param graphs
     * @return Merged subgraphs
     */
    public List<List<String>> tryMerge(List<List<String>> graphs) {
        for(int i = 0; i < graphs.size(); i++)
        {
            String w = graphs.get(i).get(graphs.get(i).size() - 1);
            for(int j = i + 1; j < graphs.size(); j++) {
                if(graphs.get(j).contains(w)) {
                    int s =0;
                    while(s < graphs.get(j).size()) {
                        if(!graphs.get(j).get(s).equals(w)) {
                            graphs.get(i).add(graphs.get(j).get(s));
                        }
                        s++;
                    }
                    graphs.remove(j);
                    return graphs;
                }
            }
        }       
        return graphs;
    }

}
