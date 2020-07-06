package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Vertex {
    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
        number = vertexCount++;
    }

    // конструктор для ввода вершин из файла
    public Vertex(double x, double y, int number) {
        this.x = x;
        this.y = y;
        this.number = number;   // могут быть проблемы с нумерацией из-за этого
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillOval(x-RADIUS, y-RADIUS, RADIUS*2, RADIUS*2);
        gc.setFill(Color.WHITE);
        if (number < 10)
            gc.fillText(String.valueOf(number), x-0.4*RADIUS, y+0.5*RADIUS);
        else
            gc.fillText(String.valueOf(number), x-0.8*RADIUS, y+0.5*RADIUS);
    }

    public void draw(GraphicsContext gc, int numb) {
        Color color = getColor(numb);
        gc.setFill(color);
        gc.fillOval(x-RADIUS, y-RADIUS, RADIUS*2, RADIUS*2);
        gc.setFill(Color.WHITE);
        if (number < 10)
            gc.fillText(String.valueOf(number), x-0.4*RADIUS, y+0.5*RADIUS);
        else
            gc.fillText(String.valueOf(number), x-0.8*RADIUS, y+0.5*RADIUS);
    }

    public boolean isDotInRadius(double dotX, double dotY) {
        return (dotX - x)*(dotX - x) + (dotY - y)*(dotY - y) <= RADIUS*RADIUS;
    }

    public void addAdj(Vertex vert) {
        adj.add(vert);
    }

    public int getNumber() {
        return number;
    }

    public ArrayList<Vertex> getAdj() {
        return adj;
    }

    public double getX() {return x;}
    public double getY() {return y;}

    private final ArrayList<Vertex> adj = new ArrayList<>();

    private final double x;
    private final double y;
    private final int number;

    public static int vertexCount = 1;
    public static int RADIUS = 10;

    public static Color getColor(int numb) {
        return colors[(numb-1) % colors.length];
    }

    public static Color[] colors = {Color.GREEN, Color.RED, Color.ORANGE,
            Color.PINK, Color.YELLOW, Color.AQUA, Color.BROWN, Color.GRAY};
}
