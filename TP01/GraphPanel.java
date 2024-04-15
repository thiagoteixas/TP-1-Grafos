
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GraphPanel extends JPanel {
  private Grafo grafo;
  private Map<Integer, Point> vertexCoordinates; // Mapa para armazenar as coordenadas dos vértices

  public GraphPanel(Grafo grafo) {
    this.grafo = grafo;
    this.vertexCoordinates = new HashMap<>();
    this.setPreferredSize(new Dimension(800, 600));
    //calculateVertexPositions(); // Calcular e armazenar as posições dos vértices

    this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Garante que só calcula as posições se o painel tem dimensões positivas
                if (getWidth() > 0 && getHeight() > 0) {
                    calculateVertexPositions();
                    repaint();
                }
            }

            @Override
            public void componentShown(ComponentEvent e) {
                if (getWidth() > 0 && getHeight() > 0) {
                    calculateVertexPositions();
                }
            }
        });
    
  }

  public void calculateVertexPositions() {
    int vertexRadius = 20; // Ajustado para visualização melhor
    Random rand = new Random();
    System.out.println("Width: " + getWidth() + " Height: " + getHeight()); // Debug para verificar as dimensões
    if (getWidth() > 0 && getHeight() > 0) {
      vertexCoordinates.clear();
      for (int i = 0; i < grafo.getNumVertices(); i++) {
        int x = rand.nextInt(getWidth() - vertexRadius * 2) + vertexRadius;
        int y = rand.nextInt(getHeight() - vertexRadius * 2) + vertexRadius;
        vertexCoordinates.put(i, new Point(x, y));
        // System.out.println("Vertex " + i + ": (" + x + ", " + y + ")"); // Debug para verificar as coordenadas
      }
    }
  }

  

  @Override
 protected void paintComponent(Graphics g) {
  super.paintComponent(g);
  Graphics2D g2d = (Graphics2D) g;
  g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

  // Desenhe as arestas
  g2d.setColor(Color.GRAY);
  int vertexRadius = 10;
  for (int v = 0; v < grafo.getNumVertices(); v++) {
    Point p1 = vertexCoordinates.get(v);
    if (p1 != null) {
      for (int w : grafo.getAdjList(v)) {
        if (v < w) { // Só desenha aresta se v < w para evitar duplicatas
          Point p2 = vertexCoordinates.get(w);
          if (p2 != null) {
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
          }
        }
      }
    }
  }

  // Desenhe os vértices
  g2d.setColor(Color.BLUE);
  for (int i = 0; i < grafo.getNumVertices(); i++) {
    Point p = vertexCoordinates.get(i);
    if (p != null) {
      g2d.fillOval(p.x - vertexRadius, p.y - vertexRadius, vertexRadius * 2, vertexRadius * 2);
      g2d.drawString(String.valueOf(i), p.x - vertexRadius / 2, p.y - vertexRadius / 2);
    }
  }
}
}
