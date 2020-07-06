package sample;

import com.Algorithm.AlgorithmKosarayu;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Pair;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Graph {
    public void addVertex(Vertex vertex, GraphicsContext gc) {
        vertexes.add(vertex);
        vertex.draw(gc);
    }

    public void addEdge(Vertex vert1, Vertex vert2, GraphicsContext gc) {
        vert1.addAdj(vert2);
        drawArrow(gc, vert1.getX(), vert1.getY(), vert2.getX(), vert2.getY(), Color.BLACK);
    }

    public void drawAll(GraphicsContext gc) {
        clearCanvas(gc);
        for (Vertex vert: vertexes) {
            vert.draw(gc);
            for (Vertex nextVert: vert.getAdj()) {
                drawArrow(gc, vert.getX(), vert.getY(), nextVert.getX(), nextVert.getY(), Color.BLACK);
            }
        }
    }

    private void drawArrow(GraphicsContext gc, double node1X, double node1Y,
                           double node2X, double node2Y, Color color) {
        double arrowAngle = Math.toRadians(30.0);
        double arrowLength = Vertex.RADIUS;
        double dx = node1X - node2X;
        double dy = node1Y - node2Y;
        double angle = Math.atan2(dy, dx);

        node1X = node1X - Math.cos(angle) * Vertex.RADIUS;
        node1Y = node1Y - Math.sin(angle) * Vertex.RADIUS;
        node2X = node2X + Math.cos(angle) * Vertex.RADIUS;
        node2Y = node2Y + Math.sin(angle) * Vertex.RADIUS;
        gc.setStroke(color);
        gc.strokeLine(node1X, node1Y, node2X, node2Y);

        double x1 = Math.cos(angle + arrowAngle) * arrowLength + node2X;
        double y1 = Math.sin(angle + arrowAngle) * arrowLength + node2Y;

        double x2 = Math.cos(angle - arrowAngle) * arrowLength + node2X;
        double y2 = Math.sin(angle - arrowAngle) * arrowLength + node2Y;
        gc.strokeLine(node2X, node2Y, x1, y1);
        gc.strokeLine(node2X, node2Y, x2, y2);
    }

    public Vertex checkCollision(double dotX, double dotY) {
        for (Vertex vert: vertexes) {
            if (vert.isDotInRadius(dotX, dotY)) {
                return vert;
            }
        }
        return null;
    }

    public void runAlgorithm(GraphicsContext gc) {
        ArrayList<Pair<Integer, Integer>> adjList = new ArrayList<>();
        for (Vertex vert: vertexes) {
            for (Vertex curVert: vert.getAdj()) {
                adjList.add(new Pair(vert.getNumber(), curVert.getNumber()));
            }
        }
        HashSet<Integer> markedVertexes = new HashSet<>();
        for (Pair<Integer, Integer> edge: adjList) {
            markedVertexes.add(edge.getKey());
            markedVertexes.add(edge.getValue());
        }
        for (Vertex vert: vertexes) {
            if (!markedVertexes.contains(vert.getNumber())) {
                adjList.add(new Pair(vert.getNumber(), -1));
            }
        }
        AlgorithmKosarayu algorithm = new AlgorithmKosarayu();
        ArrayList<Pair<Integer, Integer>> components = algorithm.start(adjList);
        HashMap<Integer, Integer> map = new HashMap<>();
        for (Pair<Integer, Integer> p: components) {
            map.put(p.getKey(), p.getValue());
        }

        clearCanvas(gc);
        for (Vertex vert: vertexes) {
            vert.draw(gc, map.get(vert.getNumber()));
            for (Vertex curVert: vert.getAdj()) {
                if (map.get(vert.getNumber()) == map.get(curVert.getNumber())) {
                    drawArrow(gc, vert.getX(), vert.getY(), curVert.getX(),
                            curVert.getY(), Vertex.getColor(map.get(vert.getNumber())));
                }
                else {
                    drawArrow(gc, vert.getX(), vert.getY(), curVert.getX(),
                            curVert.getY(), Color.BLACK);
                }
            }
        }
    }

    public void inputFileGraph(GraphicsContext gc) throws IOException {
        importManager graph = new importManager("tests/test_2.txt");
        ArrayList<Pair<Integer, Integer>> list = graph.getGraph();
        if (list == null) { System.out.println("Файл не найден."); return; }  // сделать эксепшены
        System.out.println("Граф импортирован.");

        double fromX = 20;
        double toX = gc.getCanvas().getWidth() - 30;
        double fromY = 20;
        double toY = gc.getCanvas().getHeight() - 30;
        double X;
        double Y;
        Random randomX = new Random();
        Random randomY = new Random();

        for (Pair<Integer, Integer> rib : list) {
            Vertex vertexFrom = findVertex(rib.getKey());

            if (vertexFrom == null) {  // если данной вершины не существует

                X = fromX + randomX.nextInt((int)(toX - fromX));
                Y = fromY + randomY.nextInt((int)(toY - fromY));

                vertexFrom = new Vertex(X, Y, rib.getKey());
                addVertex(vertexFrom, gc);
            }

            Vertex vertexTo = findVertex(rib.getValue());
            if (vertexTo == null) {  // если данной вершины не существует

                X = fromX + randomX.nextInt((int)(toX - fromX));
                Y = fromY + randomY.nextInt((int)(toY - fromY));

                vertexTo = new Vertex(X, Y, rib.getValue());
                addVertex(vertexTo, gc);
            }

            addEdge(vertexFrom, vertexTo, gc);  // добавление ребра
        }

    }

    public Vertex findVertex(int number){
        for(Vertex vertex : vertexes) {
            if(vertex.getNumber() == number)
                return vertex;
        }
        return null;

    }

    public void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0,0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    private final ArrayList<Vertex> vertexes = new ArrayList<>();
}
