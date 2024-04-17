/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.daaproject;

import java.util.*;

public class DaaProject {

 public static void main(String[] args) {
    // Create graph using adjacency list
    Map<Integer, List<Integer>> adjacencyList = createGraph();

    // Array representing colors after graph coloring
    Map<Integer, Integer> colors = new HashMap<>();

    // Apply graph coloring algorithm
    graphColoring(adjacencyList, colors, 0, 3); // Assuming 3 colors are available

    // Print initial color map
    System.out.println("Initial Color Combination (After graph Coloring):");
    for (Map.Entry<Integer, Integer> entry : colors.entrySet()) {
        System.out.println("Node: " + entry.getKey() + ", Color: " + entry.getValue());
    }

    // Identify invalid edges
    List<ColorCorrection.Edge> invalidEdges = identifyInvalidEdges(adjacencyList, colors);

    // Print invalid edges before color correction
    System.out.println("\nInvalid Edges (After graph coloring):");
    for (ColorCorrection.Edge edge : invalidEdges) {
        System.out.println("Edge: " + edge.getStartNode() + " -> " + edge.getEndNode() + ", Color: " + edge.getColor());
    }

    // Altering color combination to make some edges invalid
    // Update color of node 3 to 0
    

    // Print color map after altering colors
    System.out.println("\nColor Combination (After Altering Colors):");
    for (Map.Entry<Integer, Integer> entry : colors.entrySet()) {
        System.out.println("Node: " + entry.getKey() + ", Color: " + entry.getValue());
    }

    // Recompute invalid edges after altering colors
    invalidEdges = identifyInvalidEdges(adjacencyList, colors);

    // Print invalid edges after altering colors
    System.out.println("\n Invalid Edges (Before Color Correction):");
    for (ColorCorrection.Edge edge : invalidEdges) {
        System.out.println("Edge: " + edge.getStartNode() + " -> " + edge.getEndNode() + ", Color: " + edge.getColor());
    }

    // Color correction for invalid edges
    ColorCorrection.colorCorrection(invalidEdges, colors);

    // Display results
    System.out.println("\nGraph after Color Correction:");
    for (ColorCorrection.Edge edge : invalidEdges) {
        System.out.println("Edge: " + edge.getStartNode() + " -> " + edge.getEndNode() + ", Color: " + edge.getColor());
    }
}

    static Map<Integer, List<Integer>> createGraph() {
        Map<Integer, List<Integer>> adjacencyList = new HashMap<>();

        adjacencyList.put(0, List.of(1, 2));
        adjacencyList.put(1, List.of(0, 2));
        adjacencyList.put(2, List.of(0, 1, 3));
        adjacencyList.put(3, List.of(2));

        return adjacencyList;
    }

    static List<ColorCorrection.Edge> identifyInvalidEdges(Map<Integer, List<Integer>> adjacencyList, Map<Integer, Integer> colors) {
        List<ColorCorrection.Edge> invalidEdges = new ArrayList<>();

        for (Map.Entry<Integer, List<Integer>> entry : adjacencyList.entrySet()) {
            int node = entry.getKey();
            List<Integer> neighbors = entry.getValue();
         ///System.out.println(neighbors);
            for (int neighbor : neighbors) {
                if (colors.containsKey(node) && colors.containsKey(neighbor) && colors.get(node).equals(colors.get(neighbor))) {
                    invalidEdges.add(new ColorCorrection.Edge(node, neighbor, colors.get(node)));
                }
            }
        }

        return invalidEdges;
    }

    static void graphColoring(Map<Integer, List<Integer>> adjacencyList, Map<Integer, Integer> colors, int node, int numColors) {
        if (node == adjacencyList.size()) {
            return; // All nodes are colored
        }

        for (int color = 1; color <= numColors; color++) {
            if (isColorValid(node, color, adjacencyList, colors)) {
                colors.put(node, color);
                graphColoring(adjacencyList, colors, node + 1, numColors);
            }
        }
    }

    static boolean isColorValid(int node, int color, Map<Integer, List<Integer>> adjacencyList, Map<Integer, Integer> colors) {
        List<Integer> neighbors = adjacencyList.getOrDefault(node, new ArrayList<>());

        for (int neighbor : neighbors) {
            if (colors.containsKey(neighbor) && colors.get(neighbor) == color) {
                return false; // The color is already used by a neighbor
            }
        }

        return true;
    }
}

class ColorCorrection {

  public static void colorCorrection(List<Edge> invalidEdges, Map<Integer, Integer> colors) {
    for (Edge edge : invalidEdges) {
        int node1 = edge.getStartNode();
        int node2 = edge.getEndNode();

        int color1 = colors.get(node1);

        // Attempt to find a color for node1
        boolean colorFoundForN1 = tryReuseColor(node1, colors, color1);

        if (!colorFoundForN1) {
            // If no color found for node1, attempt to find a color for node2
            boolean colorFoundForN2 = tryReuseColor(node2, colors, color1);

            if (!colorFoundForN2) {
                // If no color found for both node1 and node2, create a new color
                int newColor = findNewColor(colors);
                colors.put(node1, newColor);
                colors.put(node2, newColor);
            }
        }
    }
}

private static int findNewColor(Map<Integer, Integer> colors) {
    Set<Integer> usedColors = new HashSet<>(colors.values());
    int color = 0;

    while (usedColors.contains(color)) {
        color++;
    }

    return color;
}


    private static boolean tryReuseColor(int node, Map<Integer, Integer> colors, int currentColor) {
        for (int c : colors.values()) {
            if (c != currentColor) {
                if (canReuseColor(node, colors, c)) {
                    colors.put(node, c);
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean canReuseColor(int node, Map<Integer, Integer> colors, int colorToReuse) {
        List<Edge> edges = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : colors.entrySet()) {
            int neighbor = entry.getKey();
            int color = entry.getValue();
            edges.add(new Edge(node, neighbor, color));
        }

        for (Edge e1 : edges) {
            int neighbor = (e1.getStartNode() == node) ? e1.getEndNode() : e1.getStartNode();
            if (colors.containsKey(neighbor) && colors.get(neighbor) == colorToReuse) {
                return false;
            }
        }
        return true;
    }

   /* private static int findNewColor(Map<Integer, Integer> colors) {
        int maxColor = 0;

        for (int color : colors.values()) {
            if (color > maxColor) {
                maxColor = color;
            }
        }

        return maxColor + 1;
    }
*/
    
    public static class Edge {
        private int startNode;
        private int endNode;
        private int color;

        public Edge(int startNode, int endNode, int color) {
            this.startNode = startNode;
            this.endNode = endNode;
            this.color = color;
        }

        public int getStartNode() {
            return startNode;
        }

        public int getEndNode() {
            return endNode;
        }

        public int getColor() {
            return color;
        }
    }
}