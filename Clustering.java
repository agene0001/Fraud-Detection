import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;

import java.util.ArrayList;

public class Clustering {
    private CC clusters;
    private int k;

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        this.k = k;
        EdgeWeightedGraph graph = new EdgeWeightedGraph(locations.length);
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations.length; j++) {
                if (j != i) {
                    graph.addEdge(new Edge(i, j, locations[i].distanceTo(locations[j])));
                }
            }
        }
        KruskalMST mst = new KruskalMST(graph);
        EdgeWeightedGraph clusterss = new EdgeWeightedGraph(locations.length);
        ArrayList<Edge> edgess = new ArrayList<>();
        for (Edge e : mst.edges()) {
            edgess.add(e);
        }
        edgess.sort(null);
        for (int i = 0; i < edgess.size() - k + 1; i++) {
            clusterss.addEdge(edgess.get(i));

        }
        clusters = new CC(clusterss);
        // number of connected components


    }

    // return the cluster of the ith point
    public int clusterOf(int i) {
        return clusters.id(i);
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        int[] dimensions = new int[k];
        for (int i = 0; i < input.length; i++) {
            dimensions[clusterOf(i)] += input[i];
        }
        return dimensions;
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In("princeton_locations.txt");
        int locationslen = Integer.parseInt(in.readLine());
        Point2D[] locations = new Point2D[locationslen];
        int ctr = 0;
        while (in.hasNextLine()) {
            String temp = in.readLine();
            Point2D location = new Point2D(Double.parseDouble(temp.split(" ")[0]),
                                           Double.parseDouble(temp.split(" ")[1]));
            locations[ctr++] = location;
        }
        Clustering clustering = new Clustering(locations, 5);
    }
}