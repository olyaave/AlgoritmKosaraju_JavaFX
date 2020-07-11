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

    public void addVertex(Vertex vertex) {
        vertexes.add(vertex);
    }

    public void removeVertex(Vertex vert) {
        for (Vertex nextVert: vertexes) {
            nextVert.getAdj().remove(vert);
        }
        vertexes.remove(vert);
    }

    public void addEdge(Vertex vert1, Vertex vert2) {
        if (vert1.equals(vert2)) vert1.setHasLoop(true); // добавление петли
        else vert1.addAdj(vert2);
    }

    public void removeEdge(Vertex vert1, Vertex vert2) {
        if (vert1.equals(vert2)) vert1.setHasLoop(false); // удаление петли
        vert1.getAdj().remove(vert2);
        vert2.getAdj().remove(vert1);
    }

    // отрисовка изображения графа
    public void drawAll(GraphicsContext gc) {
        clearCanvas(gc);
        Pair<Integer, Color> edge;
        for (Vertex vert: vertexes) {
            vert.draw(gc);
            if (vert.getRinged()) drawRing(gc, vert.getX(), vert.getY()); // отрисовка кольца вокруг вершины
            // отрисовка всех рёбер исходящих из вершины
            for (Vertex nextVert: vert.getAdj()) {
                if (vert != nextVert) {
                    if(nextVert.getNumber() == vert.getNextSelectedEdge())
                        drawArrow(gc, vert.getX(), vert.getY(), nextVert.getX(), nextVert.getY(),
                                                                                Color.RED, false);
                    else if(!vert.getSelectedEdges().isEmpty() &&
                            (edge = vert.findSelectedEdge(nextVert.getNumber())) != null)
                            drawArrow(gc, vert.getX(), vert.getY(), nextVert.getX(), nextVert.getY(),
                                                                                edge.getValue(), false);
                    else if (vert.getColor().equals(nextVert.getColor()))
                    {
                        if (!nextVert.getSelectedEdges().isEmpty() && nextVert.findSelectedEdge(vert.getNumber())
                                != null || vert.getNumber() == nextVert.getNextSelectedEdge())
                            drawArrow(gc, vert.getX(), vert.getY(), nextVert.getX(), nextVert.getY(),
                                                                                Color.BLACK, true);

                        else
                            drawArrow(gc, vert.getX(), vert.getY(), nextVert.getX(), nextVert.getY(),
                                                                                vert.getColor(), false);
                    }
                    else
                            drawArrow(gc, vert.getX(), vert.getY(), nextVert.getX(), nextVert.getY(),
                                                                                Color.BLACK, false);

                }
            }
        }
    }

    private void drawArrow(GraphicsContext gc, double node1X, double node1Y,
                           double node2X, double node2Y, Color color, boolean isOnlyArrow) {
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
        if(!isOnlyArrow)
            gc.strokeLine(node1X, node1Y, node2X, node2Y);

        double x1 = Math.cos(angle + arrowAngle) * arrowLength + node2X;
        double y1 = Math.sin(angle + arrowAngle) * arrowLength + node2Y;

        double x2 = Math.cos(angle - arrowAngle) * arrowLength + node2X;
        double y2 = Math.sin(angle - arrowAngle) * arrowLength + node2Y;
        // отрисовка наконечника стрелочки
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

    // получение списка рёбер и передача алгоритму
    public void runAlgorithm() {
        ArrayList<Pair<Integer, Integer>> adjList = new ArrayList<>();
        for (Vertex vert: vertexes) {
            for (Vertex curVert: vert.getAdj()) {
                adjList.add(new Pair(vert.getNumber(), curVert.getNumber()));
            }
        }
        // выделение изолированных вершин
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

    // визуализация одного шага алгоритма, согласно списку событий, полученному от алгоритма
    public void visualiseStep(TextArea textArea, HBox labelBox) {
        Event event = events.get(eventIndex++);
        textArea.appendText(event.getTextHints());
        switch (event.getNAME_EVENT()) {
            case 1:
                VisualSteps.event1(vertexes, event.getNameVertex());
                break;
            case 2:
                VisualSteps.event2(vertexes, event.getNameVertex(), labelBox);
                break;
            case 3:
                VisualSteps.event3(vertexes, event.getTransition().getKey(), event.getTransition().getValue());
                break;
            case 4:
                VisualSteps.event4(vertexes, event.getTransition().getKey(),
                        event.getTransition().getValue(), labelBox);
                break;
            case 5:
                VisualSteps.event5(vertexes);
                for (Pair<Integer, Integer> pair : event.getInventedListEdge()) {
                    if (pair.getValue() != -1)
                        addEdge(findVertex(pair.getKey()), findVertex(pair.getValue()));
                }
                break;
            case 6:
                VisualSteps.event6(vertexes, event.getTransition().getKey(), event.getTransition().getValue());
                break;
            case 7:
                VisualSteps.event7(vertexes, event.getNameVertex());
                break;
            case 8:
                VisualSteps.event8(vertexes, event.getDataDFS2()[0], event.getDataDFS2()[1], event.getDataDFS2()[2]);
                break;
            case 9:
                VisualSteps.event9(vertexes, event.getTransition().getKey(), event.getTransition().getValue());
                break;
            case 10:
                if (orderIndex != 0)
                    ((Label) (labelBox.getChildren().
                            get(labelBox.getChildren().size() - orderIndex))).setTextFill(Color.BLACK);
                ((Label) (labelBox.getChildren().
                        get(labelBox.getChildren().size() - orderIndex++ - 1))).setTextFill(Color.RED);
                VisualSteps.event10(vertexes, event.getNameVertex());
                break;
        }
    }

    // визуализация шага назад
    public void visualiseStepBack(TextArea textArea, HBox labelBox) {
        int saveEventIndex = eventIndex - 1;
        endAlgorithm(textArea, labelBox);
        standardView(labelBox);
        eventIndex = 0;
        textArea.setText("Алгоритм запущен.\n");
        labelBox.getChildren().add(new Label("Порядок вершин:"));
        for (int i = 0; i < saveEventIndex; i++) {
            visualiseStep(textArea, labelBox);
        }

    }

    // сброс изображения графа к стандартному виду
    public void standardView(HBox labelBox) {
        for (Vertex vert: vertexes) {
            vert.setColor(Color.BLACK);
            vert.setRinged(false);
            vert.getSelectedEdges().clear();
            vert.setNextSelectedEdge(0);
        }
        orderIndex = 0;
        labelBox.getChildren().clear();
    }

    // ввод графа из файла
    public void inputFileGraph(GraphicsContext gc, TextArea mainTextArea) throws IOException {
        ImportManager graph = new ImportManager(ModalWindow.fileName, mainTextArea);
        ArrayList<Pair<Integer, Integer>> list = graph.getGraph();
        if (list == null) { return; }
        mainTextArea.setText("Граф импортирован.");

        double fromX = 40;
        double toX = gc.getCanvas().getWidth() - 30;
        double fromY = 60;
        double toY = gc.getCanvas().getHeight() - 30;
        double X;
        double Y;
        double diffVertexes = 60;   // расстояние между вершинами
        Random randomX = new Random();
        Random randomY = new Random();
        X = fromX + randomX.nextInt((int) (toX - fromX));
        Y = fromY + randomY.nextInt((int) (toY - fromY));
        double repeat = 1;

        for (Pair<Integer, Integer> rib : list) {
            Vertex vertexFrom = findVertex(rib.getKey());

            if (vertexFrom == null) {  // если данной вершины не существует
                // выбор места, непересекающегося с другими вершинами
                while (checkCollision(X, Y, diffVertexes) != null && X < toX && Y < toY) {
                    X = fromX + randomX.nextInt((int) (toX - fromX));
                    Y = fromY + randomY.nextInt((int) (toY - fromY));
                    if(repeat++ > 150) {
                        mainTextArea.setText("Скорее всего, введенный граф слишком большой и не помещается на" +
                                " поле текущего размера. Разверните программу на полный экран и попробуйте снова.");
                        vertexes.clear();
                        clearCanvas(gc);
                        return;
                    }
                }
                vertexFrom = new Vertex(X, Y, rib.getKey());
                addVertex(vertexFrom);
            }
            if (rib.getValue() == -1) continue;

            Vertex vertexTo = findVertex(rib.getValue());
            repeat = 1;

            if (vertexTo == null) {  // если данной вершины не существует
                // выбор места, непересекающегося с другими вершинами
                while (checkCollision(X, Y, diffVertexes) != null && X < toX && Y < toY) {
                    X = fromX + randomX.nextInt((int) (toX - fromX));

                    Y = fromY + randomY.nextInt((int) (toY - fromY));
                    if(repeat++ > 150) {
                        mainTextArea.setText("Скорее всего, введенный граф слишком большой и не помещается на" +
                                " поле текущего размера. Разверните программу на полный экран и попробуйте снова.");
                        vertexes.clear();
                        clearCanvas(gc);
                        return;
                    }
                }
                vertexTo = new Vertex(X, Y, rib.getValue());
                addVertex(vertexTo);
            }
            addEdge(vertexFrom, vertexTo);  // добавление ребра
        }
    }

    public Vertex findVertex(int number) {
        for(Vertex vertex : vertexes) {
            if(vertex.getNumber() == number)
                return vertex;
        }
        return null;
    }

    private void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0,0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    public boolean isLastEvent() {
        return events.size()-1 == eventIndex;
    }
    public boolean isFirstEvent() {return eventIndex == 0;}
    public boolean isEmpty() {return vertexes.isEmpty();}

    public void endAlgorithm(TextArea textArea, HBox label) {
        for (int i = eventIndex; i < events.size(); i++) {
            visualiseStep(textArea, label);
        }
        textArea.setText("Результат работы алгоритма изображен на экране.\n");
    }

    private final ArrayList<Vertex> vertexes = new ArrayList<>();

    private ArrayList<Event> events = new ArrayList<>();

    private int orderIndex = 0;

    private int eventIndex = 0;
}
