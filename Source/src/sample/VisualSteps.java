package sample;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

// класс для пошаговой визуализации
// события получаются в виде списка при завершении работы алгоритма
public class VisualSteps {
    // событие начала очередного dfs1
    public static void event1(ArrayList<Vertex> vertexes, int number) {
        for (Vertex vert: vertexes) {
            vert.setNextSelectedEdge(0);
            if (vert.getNumber() == number) {
                vert.setRinged(true);
            }
        }
    }

    // событие окончания очередного dfs1
    public static void event2(ArrayList<Vertex> vertexes, int number, HBox labelBox) {
        labelBox.getChildren().add(new Label(" " + number));
        for (Vertex vert: vertexes) {
            vert.setNextSelectedEdge(0);
            if (vert.getNumber() == number) {
                vert.setRinged(false);
            }
        }
    }

    // событие перехода по ребру в dfs1
    public static void event3(ArrayList<Vertex> vertexes, int fromNumber, int toNumber) {
        for (Vertex vert: vertexes) {
            vert.setNextSelectedEdge(0);
            if (vert.getNumber() == fromNumber) {
                vert.setRinged(false);
                vert.setNextSelectedEdge(toNumber);
                vert.addSelectedEdge(toNumber, Color.DARKRED);
            }
            if (vert.getNumber() == toNumber) vert.setRinged(true);
        }
    }

    // событие возврата в dfs1
    public static void event4(ArrayList<Vertex> vertexes, int fromNumber, int toNumber, HBox labelBox) {
        labelBox.getChildren().add(new Label(" " + fromNumber));
        for (Vertex vert: vertexes) {
            vert.setNextSelectedEdge(0);
            if (vert.getNumber() == fromNumber) vert.setRinged(false);
            if (vert.getNumber() == toNumber) vert.setRinged(true);
        }
    }

    // событие инвертирования всех рёбер графа
    public static void event5(ArrayList<Vertex> vertexes) {
        for (Vertex vert: vertexes) {
            vert.setRinged(false);
            vert.getSelectedEdges().clear();
            vert.setNextSelectedEdge(0);
            vert.getAdj().clear();
        }
    }

    // событие начала очередного dfs2
    public static void event6(ArrayList<Vertex> vertexes, int number, int colorNumber) {
        for (Vertex vert: vertexes) {
            vert.setNextSelectedEdge(0);
            if (vert.getNumber() == number) {
                vert.setRinged(true);
                vert.setColor(Vertex.computeColor(colorNumber));
            }
        }
    }

    // событие окончания очередного dfs2
    public static void event7(ArrayList<Vertex> vertexes, int number) {
        for (Vertex vert: vertexes) {
            vert.setNextSelectedEdge(0);
            if (vert.getNumber() == number) {
                vert.setRinged(false);
            }
        }
    }

    // событие перехода по ребру в dfs2
    public static void event8(ArrayList<Vertex> vertexes, int fromNumber, int toNumber, int colorNumber) {
        for (Vertex vert: vertexes) {
            vert.setNextSelectedEdge(0);
            if (vert.getNumber() == fromNumber) {
                vert.setRinged(false);
                vert.setNextSelectedEdge(toNumber);
            }
            if (vert.getNumber() == toNumber) {
                vert.setRinged(true);
                vert.setColor(Vertex.computeColor(colorNumber));
            }
        }
    }

    // событие возврата в dfs2
    public static void event9(ArrayList<Vertex> vertexes, int fromNumber, int toNumber) {
        for (Vertex vert: vertexes) {
            vert.setNextSelectedEdge(0);
            if (vert.getNumber() == fromNumber) vert.setRinged(false);
            if (vert.getNumber() == toNumber) vert.setRinged(true);
        }
    }

    // событие просмотра нового кандидата для начала dfs2
    public static void event10(ArrayList<Vertex> vertexes, int number) {
        for (Vertex vert: vertexes) {
            vert.setRinged(false);
            if (vert.getNumber() == number) {
                vert.setRinged(true);
            }
        }
    }
}
