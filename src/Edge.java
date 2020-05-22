/**
 * Creates an Edge object that stores vertices and weights
 * @author Manisha Kusuma
 *
 */
public class Edge {
    private String vertex1;
    private String vertex2;
    private int weight;
    /**
     * Each vertex associated with an edge and value of the edge
     * @param vertexOne
     * @param vertexTwo
     * @param value
     */
    public Edge(String vertexOne, String vertexTwo, int value) {
        vertex1 = vertexOne;
        vertex2 = vertexTwo;
        weight = value;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public String getVertex1() {
        return vertex1;
    }
    
    public String getVertex2() {
        return vertex2;
    }
}
