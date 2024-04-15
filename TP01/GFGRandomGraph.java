/*
 *  TP01
 * 
 *  Pontifícia Universidade Católica de Minas Gerais  
 *  Curso: Ciência da Computação 
 *  Disciplina: Teoria dos Grafos e Computabilidade 
 *  Professor : Zenilton Kleber Gonçalves do Patrocínio Júnior
 * 
 *  Aluno: Pedro Madeira, Thiago Teixeira e Fabio Franco
 * 
 * Creditos: https://acervolima.com/como-criar-um-grafico-aleatorio-usando-a-geracao-de-borda-aleatoria-em-java/

 */

// Create a Random Graph Using
// Random Edge Generation in Java
import java.util.*;
import java.io.*;

public class GFGRandomGraph {

    public int vertices;
    public int edges;

    // Set a maximum limit to the vertices
    // final int MAX_LIMIT = 20;

    // A Random instance to generate random values
    Random random = new Random();
    // An adjacency list to represent a graph
    public List<List<Integer>> adjacencyList;

    // Creating the constructor
    public GFGRandomGraph(int MAX_LIMIT) {
        // Set a maximum limit for the number
        // of vertices say 20
        this.vertices = random.nextInt(MAX_LIMIT) + 1;

        // compute the maximum possible number of edges
        // and randomly choose the number of edges less than
        // or equal to the maximum number of possible edges
        this.edges = random.nextInt(computeMaxEdges(vertices)) + 1;

        // Creating an adjacency list representation
        // for the random graph
        adjacencyList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++)
            adjacencyList.add(new ArrayList<>());

        // A for loop to randomly generate edges
        for (int i = 0; i < edges; i++) {
            // randomly select two vertices to
            // create an edge between them
            int v = random.nextInt(vertices);
            int w = random.nextInt(vertices);

            // Check if there is already
            // an edge between v and w
            if (adjacencyList.get(v).contains(w)) {
                // Reduce the value of i
                // so that again v and w can be chosen
                // for the same edge count
                i = i - 1;
                continue;
            }

            // Add an edge between them if
            // not previously created
            addEdge(v, w);
        }
    }

    // Method to compute the maximum number of possible
    // edges for a given number of vertices
    int computeMaxEdges(int numOfVertices) {
        // As it is an undirected graph
        // So, for a given number of vertices V
        // there can be at-most V*(V-1)/2 number of edges
        return numOfVertices * ((numOfVertices - 1) / 2);
    }

    // Method to add edges between given vertices
    void addEdge(int v, int w) {
        // Note: it is an Undirected graph

        // Add w to v's adjacency list
        adjacencyList.get(v).add(w);

        // Add v to w's adjacency list
        // if v is not equal to w
        if (v != w)
            adjacencyList.get(w).add(v);
        // The above condition is important
        // If you don't apply the condition then
        // two self-loops will be created if
        // v and w are equal
    }

    public static void main(String[] args) {
        // Create a GFGRandomGraph object
        GFGRandomGraph randomGraph = new GFGRandomGraph(20);

        // Print the graph
        // System.out.println("The generated random graph :");
        System.out.println("\n");
        for (int i = 0; i < randomGraph.adjacencyList.size(); i++) {
            System.out.print(i + " -> { ");

            List<Integer> list = randomGraph.adjacencyList.get(i);

            if (list.isEmpty())
                System.out.print(" No adjacent vertices ");
            else {
                int size = list.size();
                for (int j = 0; j < size; j++) {

                    System.out.print(list.get(j));
                    if (j < size - 1)
                        System.out.print(" , ");
                }
            }
            System.out.println("}");
        }
        System.out.println("\n");
    }
}