package com.Algorithm;
import javafx.util.Pair;
import java.util.ArrayList;

public class Vertex {

    private final Integer NAME;                 //имя
    private Integer whence = -1;                //из какой вершины попали
    private ArrayList<Vertex> adjacentVertex;   //список смежных вершин

    private int color = -1;                     //цвет, в который вершина окрашена (для dfs)
    private int component = -1;                 //компонента связности
    private int timeOut = -1;                   //время выхода (для dfs)


    public Vertex(Integer NAME) {
        this.NAME = NAME;
        adjacentVertex = new ArrayList<Vertex>();
    }
    //сеттеры
    public void setWhence(Integer whence) {
        this.whence = whence;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setComponent(int component) {
        this.component = component;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public void setAdjacentVertex(Vertex value) {
        adjacentVertex.add(value);
    }
    //геттеры
    public Integer getNAME() {
        return NAME;
    }

    public Integer getWhence() {
        return whence;
    }

    public int getColor() {
        return color;
    }

    public int getComponent() {
        return component;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public ArrayList<Vertex> getAdjacentVertex(){
        return adjacentVertex;
    }
}
