/*
 *  TP01
 * 
 *  Pontifícia Universidade Católica de Minas Gerais  
 *  Curso: Ciência da Computação 
 *  Disciplina: Teoria dos Grafos e Computabilidade 
 *  Professor : Zenilton Kleber Gonçalves do Patrocínio Júnior
 * 
 *  Aluno: Pedro Madeira, Thiago Teixeira e Fabio
 * 
 * Creditos: 
 */

import java.util.*;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.FileReader;

public class Grafo {
    private int V; // número de vértices
    private LinkedList<Integer>[] adj; // lista de adjacência
    private int tempo;

    @SuppressWarnings("unchecked")
    public Grafo(int v) {
        V = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; ++i) {
            adj[i] = new LinkedList<>();
        }
        tempo = 0;
    }

    public int getNumVertices() {
        return V;
    }

    public Iterable<Integer> getAdjList(int vertex) {
        return adj[vertex];
    }

    public void addAresta(int v, int w) {
        adj[v].add(w);
        adj[w].add(v); // Grafo não direcionado
    }

    // Método para verificar a existência de dois caminhos internamente disjuntos
    private boolean hasTwoDisjointPaths(int u, int v) {
    // System.out.println("prim " + u);
    // System.out.println("sec " + v);
    boolean[] visitedFirst = new boolean[V];
    boolean[] visitedSecond = new boolean[V];

    dfs(u, visitedFirst, v);
    return visitedFirst[v] && dfs(u, visitedSecond, -1, visitedFirst);
    }

    private void dfs(int u, boolean[] visited, int avoid) {
        visited[u] = true;
        for (int w : adj[u]) {
            if (!visited[w] && w != avoid) {
                dfs(w, visited, avoid);
            }
        }
    }

    private boolean dfs(int u, boolean[] visited, int avoid, boolean[] visitedFirst) {
        if (u == avoid) {
            return true;
        }

        visited[u] = true;

        for (int w : adj[u]) {
            if (!visited[w] && w != avoid && !visitedFirst[w]) {
                if (dfs(w, visited, avoid, visitedFirst)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void encontrarCaminhosDisjuntos(int origem, int destino) {
        List<List<Integer>> todosCaminhos = new ArrayList<>();
        List<Integer> caminhoAtual = new ArrayList<>();
        boolean[] visitado = new boolean[V];

        encontrarCaminhosDisjuntosUtil(origem, destino, visitado, caminhoAtual, todosCaminhos);

        // Exibir a quantidade de caminhos encontrados
        System.out.println("Quantidade de caminhos disjuntos: " + todosCaminhos.size());

        // Listar os caminhos encontrados
        for (List<Integer> caminho : todosCaminhos) {
            System.out.println(caminho);
        }
    }

    private void encontrarCaminhosDisjuntosUtil(int u, int destino, boolean[] visitado, List<Integer> caminhoAtual, List<List<Integer>> todosCaminhos) {
        visitado[u] = true;
        caminhoAtual.add(u);

        if (u == destino) {
            todosCaminhos.add(new ArrayList<>(caminhoAtual));
        } else {
            for (int vizinho : adj[u]) {
                if (!visitado[vizinho]) {
                    encontrarCaminhosDisjuntosUtil(vizinho, destino, visitado, caminhoAtual, todosCaminhos);
                }
            }
        }

        // Remover o vértice atual do caminho e marcar como não visitado para explorar outros caminhos
        caminhoAtual.remove(caminhoAtual.size() - 1);
        visitado[u] = false;
    }

    // Método para identificar articulações
    public List<Integer> findArticulacoes() {
        long starTime = System.currentTimeMillis(); //tempo

        List<Integer> articulacoes = new ArrayList<>();
        boolean[] visitado = new boolean[V];
        int[] disc = new int[V];
        int[] low = new int[V];
        int[] parent = new int[V];
        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);
        Arrays.fill(parent, -1);

        for (int i = 0; i < V; i++) {
            if (!visitado[i]) {
                findArticulacoesDfs(i, visitado, disc, low, parent, articulacoes);
            }
        }

        System.out.println("O Método para identificar articulações foi executado em: " + (System.currentTimeMillis()-starTime) + "ms\n");
        return articulacoes;
    }

    private void findArticulacoesDfs(int u, boolean[] visitado, int[] disc, int[] low, int[] parent, List<Integer> articulacoes) {
        int children = 0;
        visitado[u] = true;
        disc[u] = low[u] = ++tempo;

        for (int w : adj[u]) {
            if (!visitado[w]) {
                children++;
                parent[w] = u;
                findArticulacoesDfs(w, visitado, disc, low, parent, articulacoes);
                low[u] = Math.min(low[u], low[w]);

                // Se u é raiz da DFS e tem dois ou mais filhos, u é uma articulação
                if ((parent[u] == -1) && (children > 1))
                    articulacoes.add(u);

                // Se u não é raiz da DFS e o menor índice alcançável de um de seus filhos é
                // maior que o tempo de descoberta de u
                if ((parent[u] != -1) && (low[w] >= disc[u]))
                    articulacoes.add(u);
            } else if (w != parent[u]) // Atualiza o menor valor se w não for pai de u
                low[u] = Math.min(low[u], disc[w]);
        }
    }

    // Método proposto por Tarjan para encontrar componentes biconexos
    public List<List<Integer>> findBiconnectedComponents() {
        long starTime = System.currentTimeMillis(); //tempo

        List<List<Integer>> bcc = new ArrayList<>();
        int[] disc = new int[V];
        int[] low = new int[V];
        boolean[] stackMember = new boolean[V];
        Stack<Integer> st = new Stack<>();
        int time = 0;

        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);

        for (int i = 0; i < V; i++) {
            if (disc[i] == -1) {
                bccDfs(i, disc, low, st, stackMember, time, bcc);
            }
        }

        System.out.println("O Método proposto por Tarjan para encontrar componentes biconexos foi executado em: " + (System.currentTimeMillis()-starTime) + "ms\n");
        return bcc;
    }

    private void bccDfs(int u, int[] disc, int[] low, Stack<Integer> st, boolean[] stackMember, int time,
            List<List<Integer>> bcc) {
        disc[u] = low[u] = ++time;
        st.push(u);
        stackMember[u] = true;

        for (int v : adj[u]) {
            if (disc[v] == -1) {
                bccDfs(v, disc, low, st, stackMember, time, bcc);
                low[u] = Math.min(low[u], low[v]);

                if (low[v] >= disc[u]) {
                    List<Integer> comp = new ArrayList<>();
                    int w;
                    do {
                        w = st.pop();
                        stackMember[w] = false;
                        comp.add(w);
                    } while (w != v);
                    comp.add(u);
                    bcc.add(comp);
                }
            } else if (stackMember[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Scanner scanner = new Scanner(System.in);
        // System.out.print("Digite o número de vértices: ");
        // int numVertices;
        // try {
        //     numVertices = Integer.parseInt(scanner.nextLine()); // Usa nextLine e depois converte para inteiro
        // } catch (NumberFormatException e) {
        //     System.out.println("Número inválido. Usando 10 como padrão.");
        //     numVertices = 10; // Valor padrão caso haja erro na entrada
        // }
        // scanner.close();

        // GFGRandomGraph randomGraph = new GFGRandomGraph(numVertices);
        List<Integer> v = new ArrayList<Integer>();
        List<Integer> w = new ArrayList<Integer>();
        Boolean hasCycles = false;

        // JFrame frame = new JFrame("Visualização do Grafo");
        // frame.setSize(new Dimension(1680, 1050));
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Grafo g = new Grafo(numVertices); // Ajuste o número de vértices conforme necessário.
        // GraphPanel graphPanel = new GraphPanel(g);
        // frame.add(graphPanel); // Ajuste para o seu grafo
        // frame.pack();
        // frame.setLocationRelativeTo(null);
        // frame.setVisible(true);
        // graphPanel.calculateVertexPositions(); // Chamada após o painel ser exibido

        /*
         * Conseguimos propor para testes 3 tipos de grafos com tamanhos diferentes:
         * - graph11.txt >> grafo igual ao mostrado no exemplo do PDF do TP
         * - graph100.txt >> grafo aleatório de 100 vertices
         * - graph100.txt >> grafo aleatório de 1000 vertices
         * 
         *  Deixamos tambem exemplos de codigos (FUNCIONAIS) de grafos gerados aleatoriamente
         *  as funções estao apenas comentadas
         */

        String fileName = "graph11.txt"; //colocar aqui um dos grafos contidos na pasta
        
        BufferedReader br = new BufferedReader(new FileReader(fileName)); //lendo arquivo de dados
        String linha;
        String splitLine[] = new String[2];
        int amountOfGraphs, origem, destino;

        linha = br.readLine();
        splitLine = linha.split(" ");

        Grafo g = new Grafo(Integer.parseInt(splitLine[0])+1);
        amountOfGraphs = Integer.parseInt(splitLine[1]);

        for(int i = 0; i < amountOfGraphs; i++) {
            linha = br.readLine();
            splitLine = linha.split(" ");

            g.addAresta(Integer.parseInt(splitLine[0]), Integer.parseInt(splitLine[1]));
            v.add(Integer.parseInt(splitLine[0]));
            w.add(Integer.parseInt(splitLine[1]));
        }
        br.close();
 

        long starTime = System.currentTimeMillis(); //Tempo de execução dos tres metodos

        System.out.println("\n_______________________________________________________________________________________________\n");
        for(int i = 0; i < v.size(); i++) {
            if (g.hasTwoDisjointPaths(v.get(i), w.get(i))) {
                System.out.println("Existe cum ciclo entre (" +v.get(i)+ " e " + w.get(i)+ ")");
                hasCycles = true;
            }
            if (i == (v.size()-1) && !hasCycles) {
                System.out.println("Nào há ciclos neste grafo!");
            }
        }
        //g.encontrarCaminhosDisjuntos(origem, destino); outra função feita para encontrar caminhos disjuntos
        System.out.println("\n_______________________________________________________________________________________________");

        System.out.print("***      ***      ***      ***      ***      ***      ***      ***      ***      ***      ***");

        System.out.println("\n_______________________________________________________________________________________________\n");
        System.out.println("Articulações: " + g.findArticulacoes());
        System.out.println("\n_______________________________________________________________________________________________");

        System.out.print("***      ***      ***      ***      ***      ***      ***      ***      ***      ***      ***");

        System.out.println("\n_______________________________________________________________________________________________\n");
        System.out.println("Componentes biconexos: " + g.findBiconnectedComponents());
        System.out.println("\n_______________________________________________________________________________________________\n");


        System.out.println("\n>>> O tempo de execução de todos os tres metodos com um [Grafo de ("+ g.V +") Vertices] foi de: " + (System.currentTimeMillis()-starTime)/1000 + "s\n\n");
    }
}