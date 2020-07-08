package com.Algorithm;
import java.util.ArrayList;
import javafx.util.Pair;

public class Event {

    private final Integer EVENT_NUMB;                           //номер события
    private Integer nameVertex = -1;                            //для событий: 1, 2, 7
    private Integer[] dataDFS2;                                 //для события: 8

    private Pair <Integer, Integer > transition;                //для событий: 3, 4, 6, 9
    private ArrayList<Pair<Integer, Integer>> inventedListEdge; //для событий: 5

    private String textHints;                                   //текстовые пояснения


    public Event(Integer EVENT_NUMB) {
        this.EVENT_NUMB = EVENT_NUMB;
        inventedListEdge = new ArrayList<Pair<Integer, Integer>>();
        dataDFS2 = new Integer[3];
    }
    //сеттеры
    public void setNameVertex(Integer nameVertex) {
        this.nameVertex = nameVertex;
    }

    public void setTransition(Integer value1, Integer value2) {
        transition = new Pair <Integer, Integer >(value1, value2);
    }

    public void setInventedListEdge(ArrayList<Pair<Integer, Integer>> inventedListEdge) {
        this.inventedListEdge = inventedListEdge;
    }

    public void setDataDFS2(Integer value1, Integer value2, Integer value3) {
        dataDFS2[0] = value1;
        dataDFS2[1] = value2;
        dataDFS2[2] = value3;
    }

    public void setTextHints(String textHints){
            this.textHints = textHints;
    }

    //геттеры
    public Integer getNAME_EVENT() {
        return EVENT_NUMB;
    }

    public Integer getNameVertex() {
        return nameVertex;
    }

    public Pair <Integer, Integer > getTransition() {
        return transition;
    }

    public ArrayList<Pair<Integer, Integer>> getInventedListEdge() {
        return inventedListEdge;
    }

    public Integer[] getDataDFS2() {
        return dataDFS2;
    }

    public String getTextHints() {
        return textHints;
    }
}
