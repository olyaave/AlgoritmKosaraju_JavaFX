package sample;

import com.Algorithm.AlgorithmKosarayu;
import com.Algorithm.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Graph {
    public Graph() {
        Vertex.vertexCount = 1;
    }

    public void addVertex(Vertex vertex, GraphicsContext gc) {
        vertexes.add(vertex);
        drawAll(gc);
    }

    public void removeVertex(Vertex vert, GraphicsContext gc) {
        for (Vertex nextVert: vertexes) {
            nextVert.getAdj().remove(vert);
        }
        vertexes.remove(vert);
        drawAll(gc);
    }

    public void addEdge(Vertex vert1, Vertex vert2, GraphicsContext gc) {
        if (vert1.equals(vert2)) vert1.setHasLoop(true);
        else vert1.addAdj(vert2);
        drawAll(gc);
    }

    public void removeEdge(Vertex vert1, Vertex vert2, GraphicsContext gc) {
        vert1.getAdj().remove(vert2);
        vert2.getAdj().remove(vert1);
        drawAll(gc);
    }

    public void drawAll(GraphicsContext gc) {
        clearCanvas(gc);
        for (Vertex vert: vertexes) {
            vert.draw(gc);
            if (vert.getRinged()) drawRing(gc, vert.getX(), vert.getY());
            for (Vertex nextVert: vert.getAdj()) {
                if (vert != nextVert) {
                    if (vert.getColor().equals(nextVert.getColor()))
                        drawArrow(gc, vert.getX(), vert.getY(), nextVert.getX(), nextVert.getY(), vert.getColor());
                    else
                        drawArrow(gc, vert.getX(), vert.getY(), nextVert.getX(), nextVert.getY(), Color.BLACK);
                }
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
        gc.setLineWidth(2);
        gc.strokeLine(node1X, node1Y, node2X, node2Y);

        double x1 = Math.cos(angle + arrowAngle) * arrowLength + node2X;
        double y1 = Math.sin(angle + arrowAngle) * arrowLength + node2Y;

        double x2 = Math.cos(angle - arrowAngle) * arrowLength + node2X;
        double y2 = Math.sin(angle - arrowAngle) * arrowLength + node2Y;
        gc.strokeLine(node2X, node2Y, x1, y1);
        gc.strokeLine(node2X, node2Y, x2, y2);
    }

    private static void drawRing(GraphicsContext gc, double x, double y) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokeOval(x - Vertex.RADIUS, y-Vertex.RADIUS, Vertex.RADIUS*2, Vertex.RADIUS*2);
    }

    public Vertex checkCollision(double dotX, double dotY, double diff) {
        for (Vertex vert: vertexes) {
            if (vert.isDotInRadius(dotX, dotY, diff)) {
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
        events = algorithm.start(adjList);
        eventIndex = 0;
    }

    public void visualiseStep(GraphicsContext gc, TextArea textArea, HBox labelBox) {
        Event event = events.get(eventIndex++);
        textArea.setText(event.getTextHints());
        switch (event.getNAME_EVENT()) {
            case 1:
                VisualSteps.event1(vertexes, event.getNameVertex());
                break;
            case 2:
                labelBox.getChildren().add(new Label(" " + event.getNameVertex()));
                VisualSteps.event2(vertexes, event.getNameVertex());
                break;
            case 3:
                VisualSteps.event3(vertexes, event.getTransition().getKey(), event.getTransition().getValue());
                break;
            case 4:
                labelBox.getChildren().add(new Label(" " + event.getTransition().getKey()));
                VisualSteps.event4(vertexes, event.getTransition().getKey(), event.getTransition().getValue());
                break;
            case 5:
                VisualSteps.event5(vertexes);
                for (Pair<Integer, Integer> pair: event.getInventedListEdge()) {
                    if (pair.getValue() != -1)
                        addEdge(findVertex(pair.getKey()), findVertex(pair.getValue()), gc);
                }
                break;
            case 6:
                VisualSteps.event6(vertexes, event.getTransition().getKey(), event.getTransition().getValue());
                break;
            case 7:
                VisualSteps.event7(vertexes, event.getNameVertex());
                break;
            case 8:
                VisualSteps.event8(vertexes, event.getDataDFS2()[0],event.getDataDFS2()[1],event.getDataDFS2()[2]);
                break;
            case 9:
                VisualSteps.event9(vertexes, event.getTransition().getKey(), event.getTransition().getValue());
                break;
            case 10:
                if (orderIndex != 0)
                    ((Label)(labelBox.getChildren().
                            get(labelBox.getChildren().size() - orderIndex))).setTextFill(Color.BLACK);
                ((Label)(labelBox.getChildren().
                        get(labelBox.getChildren().size() - orderIndex++ - 1))).setTextFill(Color.RED);
                break;
        }
        drawAll(gc);

    }

    public void standardView(GraphicsContext gc, HBox labelBox) {
        for (Vertex vert: vertexes) {
            vert.setColor(Color.BLACK);
            vert.setRinged(false);
        }
        drawAll(gc);
        orderIndex = 0;
        labelBox.getChildren().clear();
    }

    public void inputFileGraph(GraphicsContext gc, TextArea mainTextArea) throws IOException {
        ImportManager graph = new ImportManager(ModalWindow.fileName, mainTextArea);
        ArrayList<Pair<Integer, Integer>> list = graph.getGraph();
        if (list == null) { return; }
        mainTextArea.setText("Граф импортирован.");

        double fromX = 40;
        double toX = gc.getCanvas().getWidth() - 30;
//        System.out.println("Ширина " + toX);
        double fromY = 60;
        double toY = gc.getCanvas().getHeight() - 30;
//        System.out.println("Высота " + toY);
        double X;
        double Y;
        double LIMITMAX = 160;  // изменить на нестатические
        double LIMITMIN = 50;
        double diffVertexes = 50;   // расстояние между вершинами
        Random randomX = new Random();
        Random randomY = new Random();
        X = fromX + randomX.nextInt((int)(toX - fromX));
        Y = fromY + randomY.nextInt((int)(toY - fromY));

        for (Pair<Integer, Integer> rib : list) {
            Vertex vertexFrom = findVertex(rib.getKey());

            if (vertexFrom == null) {  // если данной вершины не существует

                while (checkCollision(X, Y, diffVertexes) != null && X < toX && Y < toY) {
                    X = fromX + randomX.nextInt((int) (toX - fromX));
                    Y = fromY + randomY.nextInt((int) (toY - fromY));
                }
                System.out.println("Вершина готова к добавлению: "+ rib.getKey() + " [" + X + ", " + Y + "]");
                vertexFrom = new Vertex(X, Y, rib.getKey());
                addVertex(vertexFrom, gc);
            }
            if(rib.getValue() == -1) continue;
            if(rib.getValue() == -1) continue;


            Vertex vertexTo = findVertex(rib.getValue());
            if (vertexTo == null) {  // если данной вершины не существует
                while (checkCollision(X, Y, diffVertexes) != null && X < toX && Y < toY) {
                    X = fromX + randomX.nextInt((int) (toX - fromX));
                    while (Math.abs(vertexFrom.getX() - X) > LIMITMAX ||
                            Math.abs(vertexFrom.getX() - X) < LIMITMIN)
                        X = fromX + randomX.nextInt((int) (toX - fromX));

                    Y = fromY + randomY.nextInt((int) (toY - fromY));
                    while (Math.abs(vertexFrom.getY() - Y) > LIMITMAX ||
                            Math.abs(vertexFrom.getY() - Y) < LIMITMIN)
                        Y = fromY + randomY.nextInt((int) (toY - fromY));
                }
                System.out.println("Вершина готова к добавлению: "+ rib.getValue() + " [" + X + ", " + Y + "]");
                vertexTo = new Vertex(X, Y, rib.getValue());
                addVertex(vertexTo, gc);
            }

            addEdge(vertexFrom, vertexTo, gc);  // добавление ребра
            System.out.println("Ребро добавлено: "+ rib.getKey() + " " + rib.getValue());
        }

    }

    public Vertex findVertex(int number) {
        for(Vertex vertex : vertexes) {
            if(vertex.getNumber() == number)
                return vertex;
        }
        return null;
    }

    public void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0,0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    public boolean isLastEvent() {
        return events.size()-1 == eventIndex;
    }

    public void endAlgorithm(GraphicsContext gc, TextArea textArea, HBox label) {
        for (int i = eventIndex; i < events.size(); i++) {
            visualiseStep(gc, textArea, label);
        }
        textArea.setText("Результат работы алгоритма изображен на экране.\n");
        drawAll(gc);
    }

    private final ArrayList<Vertex> vertexes = new ArrayList<>();

    private ArrayList<Event> events = new ArrayList<>();

    private int orderIndex = 0;

    private int eventIndex = 0;
}
