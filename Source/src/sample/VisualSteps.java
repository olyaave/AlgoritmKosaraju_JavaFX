package sample;

import javafx.util.Pair;

import java.util.ArrayList;

public class VisualSteps {
    public static void event1(ArrayList<Vertex> vertexes, int number) {
        for (Vertex vert: vertexes) {
            if (vert.getNumber() == number) {
                vert.setRinged(true);
            }
        }
    }

    public static void event2(ArrayList<Vertex> vertexes, int number) {
        for (Vertex vert: vertexes) {
            if (vert.getNumber() == number) {
                vert.setRinged(false);
            }
        }
    }

    public static void event3(ArrayList<Vertex> vertexes, int fromNumber, int toNumber) {
        for (Vertex vert: vertexes) {
            if (vert.getNumber() == fromNumber) vert.setRinged(false);
            if (vert.getNumber() == toNumber) vert.setRinged(true);
        }
    }

    public static void event4(ArrayList<Vertex> vertexes, int fromNumber, int toNumber) {
        for (Vertex vert: vertexes) {
            if (vert.getNumber() == fromNumber) vert.setRinged(false);
            if (vert.getNumber() == toNumber) vert.setRinged(true);
        }
    }

    public static void event5(ArrayList<Vertex> vertexes) {
        for (Vertex vert: vertexes) {
            vert.getAdj().clear();
        }
    }

    public static void event6(ArrayList<Vertex> vertexes, int number, int colorNumber) {
        for (Vertex vert: vertexes) {
            if (vert.getNumber() == number) {
                vert.setRinged(true);
                vert.setColor(Vertex.computeColor(colorNumber));
            }
        }
    }

    public static void event7(ArrayList<Vertex> vertexes, int number) {
        for (Vertex vert: vertexes) {
            if (vert.getNumber() == number) {
                vert.setRinged(false);
            }
        }
    }

    public static void event8(ArrayList<Vertex> vertexes, int fromNumber, int toNumber, int colorNumber) {
        for (Vertex vert: vertexes) {
            if (vert.getNumber() == fromNumber) vert.setRinged(false);
            if (vert.getNumber() == toNumber) {
                vert.setRinged(true);
                vert.setColor(Vertex.computeColor(colorNumber));
            }
        }
    }

    public static void event9(ArrayList<Vertex> vertexes, int fromNumber, int toNumber) {
        for (Vertex vert: vertexes) {
            if (vert.getNumber() == fromNumber) vert.setRinged(false);
            if (vert.getNumber() == toNumber) vert.setRinged(true);
        }
    }

    public static void event10(ArrayList<Vertex> vertexes, int number) {

    }
}
