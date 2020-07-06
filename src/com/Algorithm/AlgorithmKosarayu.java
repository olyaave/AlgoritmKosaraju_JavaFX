package com.Algorithm;
import java.util.ArrayList;
import javafx.util.Pair;

public class AlgorithmKosarayu {

    private ArrayList<Vertex> graph = new ArrayList<Vertex>();  //граф

    private ArrayList<Pair<Integer, Integer>> timeOut;          //время выхода
    private ArrayList<Pair<Integer, Integer>> result;           //список из пар - (вершина, № компоненты связности)

    private int dfsTimer = 1;                                   //таймер для определения времени
    private int component = 1;

    public AlgorithmKosarayu() {
        timeOut = new ArrayList<Pair<Integer, Integer>>();
        result = new ArrayList<Pair<Integer, Integer>>();
    }

    public ArrayList<Pair<Integer, Integer>> start(ArrayList<Pair<Integer, Integer>> listEdge) {
        //вызов методов, в последовательности задаваемой алгоритмом
        initGraph(listEdge);                             //инициализация графа, заданного списком ребер

        for (int i = 0; i < graph.size(); i++) {                //запуск поиска в глубину с фиксированием времени выхода
            if (graph.get(i).getColor() == -1) dfs1(i);
        }

        listEdge = inverting(listEdge);                         //инвертированный список ребер
        graph.clear();
        initGraph(listEdge);                             //инвертированный граф

        for (int i = timeOut.size() - 1; i >= 0; i--) {         //идем по уюыванию времени выхода
            int index = find(timeOut.get(i).getKey());          //индекс вершины в графе
            if (graph.get(index).getColor() == -1) {            //если не посещена раннее
                graph.get(index).setComponent(component++);     //фиксируем принадлежность к компоненте
                dfs2(index);
            }
        }

        for (int i = 0; i < graph.size(); i++) {                //записываем результат
            result.add(new Pair(graph.get(i).getNAME(), graph.get(i).getComponent()));
        }
        return result;
    }

    private void initGraph(ArrayList<Pair<Integer, Integer>> listEdge) {
        //принимает список ребер и преобразовывает в списки смежности вершин

        for(int i = 0; i < listEdge.size(); i++) {

            Vertex temp1 = new Vertex(listEdge.get(i).getKey());    //добавляемая вершина
            Vertex temp2 = new Vertex(listEdge.get(i).getValue());  //добавляемая вершина

            int index1 = 0;                                         //индекс вхождения в граф
            int index2 = 0;                                         //индекс вхождения в граф

            if (graph.isEmpty()) {                                  //в графе нет ни одной вершины
                graph.add(temp1);
                if (temp1.getNAME() != temp2.getNAME() && temp2.getNAME() != -1) {//если не петля или изолированная вершина
                    graph.add(temp2);
                }
                if(temp2.getNAME() != -1) graph.get(0).setAdjacentVertex(temp2);  //если не изолированная вершина

            } else {                                                //если граф не пуст
                for (int j = 0; j < graph.size(); j++) {
                    index1 = find(temp1.getNAME());
                    if (index1 == -1) {                             //вершина еще не добавлена в граф
                        graph.add(temp1);                           //добавляем
                        index1 = graph.size() - 1;                  //запоминаем индекс
                    }
                    index2 = find(temp2.getNAME());
                    if (index2 == -1) {                             //вершина еще не добавлена в граф
                        if (temp1.getNAME() != temp2.getNAME() && temp2.getNAME() != -1) {
                            graph.add(temp2);                       //добавляем
                        }
                        index2 = graph.size() - 1;                  //запоминаем индекс
                    }
                    if (temp2.getNAME() != -1) graph.get(index1).setAdjacentVertex(graph.get(index2)); //добавляем смежную вершину
                    break;
                }
            }
        }
    }

    /*Color:
     * -1: вершина не обработана
     * 0: вершина обрабатывается
     * 1: вершина обработана  */

    private void dfs1(int index) {

        graph.get(index).setTimeIn(dfsTimer++);                                     //время входа
        graph.get(index).setColor(0);                                               //обрабатывается

        for(int i = 0; i < graph.get(index).getAdjacentVertex().size(); i++) {
            if(graph.get(index).getAdjacentVertex().get(i).getColor() == -1) {      //если есть смежная непосещенная
                dfs1(find(graph.get(index).getAdjacentVertex().get(i).getNAME()));  //запускаем поиск от нее
            }
        }
        graph.get(index).setColor(1);                                               //обрабатана
        graph.get(index).setTimeOut(dfsTimer++);                                    //время выхода
        timeOut.add(new Pair(graph.get(index).getNAME(), graph.get(index).getTimeOut()));
    }

    private ArrayList<Pair<Integer, Integer>> inverting(ArrayList<Pair<Integer, Integer>> listEdge) {

        ArrayList<Pair<Integer, Integer>> inventedListEdge = new ArrayList<Pair<Integer, Integer>>();

        for(int i = 0; i < listEdge.size(); i++) {
            if(listEdge.get(i).getValue() == -1)                                    //изолированная вершина
                inventedListEdge.add(new Pair(listEdge.get(i).getKey(), listEdge.get(i).getValue()));
            else
                inventedListEdge.add(new Pair(listEdge.get(i).getValue(), listEdge.get(i).getKey()));
        }
        return inventedListEdge;                                                    //возвращаем инвертированный список ребер
    }

    private void dfs2(int index) {

        graph.get(index).setColor(0);                                               //обрабатывается
        for(int i = 0; i < graph.get(index).getAdjacentVertex().size(); i++) {
            if(graph.get(index).getAdjacentVertex().get(i).getColor() == -1) {      //если есть смежная непосещенная
                graph.get(index).getAdjacentVertex().get(i).setComponent(graph.get(index).getComponent()); //принадлежность к компоненте
                dfs2(find(graph.get(index).getAdjacentVertex().get(i).getNAME()));  //запускаем поиск от нее
            }
        }
        graph.get(index).setColor(1);                                               //обрабатана
    }

    private int find(Integer name) {                                                //возвращает индекс вхождения вершины в граф
        for(int i = 0; i < graph.size(); i++){                                      //или -1
            if (graph.get(i).getNAME() == name) {
                return i;
            }
        }
        return -1;
    }

    private void outputGraph() { //печать графа в виде списка смежности
        System.out.println("Список смежности:");
        for(int i = 0; i < graph.size(); i++) {
            System.out.print(graph.get(i).getNAME() + ": ");
            for(int j = 0; j < graph.get(i).getAdjacentVertex().size(); j++) {
                System.out.print(graph.get(i).getAdjacentVertex().get(j).getNAME() + " ");
            }
            System.out.print("\n");
        }
    }
}
