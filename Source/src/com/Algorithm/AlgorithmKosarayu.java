package com.Algorithm;
import java.util.ArrayList;
import javafx.util.Pair;

public class AlgorithmKosarayu{

    private ArrayList<Vertex> graph = new ArrayList<Vertex>();               //граф
    private ArrayList<Pair<Integer, Integer>> timeOut;                  //время выхода
    private ArrayList<Event> events;                                    //список событий для пошаговой визуализации
    private ArrayList<Pair<Integer, Integer>> listEdge;                 //список ребер

    private int dfsTimer = 1;                                           //таймер для определения времени
    private int component = 1;

    public AlgorithmKosarayu() {
        listEdge = new ArrayList<Pair<Integer, Integer>>();
        timeOut = new ArrayList<Pair<Integer, Integer>>();
        events = new ArrayList<Event>();
    }

    public ArrayList<Event> start(ArrayList<Pair<Integer, Integer>> listEdge) {

        //вызов методов, в последовательности задаваемой алгоритмом
        this.listEdge = listEdge;

        initGraph();                                            //инициализация графа, заданного списком ребер

        for (int i = 0; i < graph.size(); i++) {                        //запуск поиска в глубину с фиксированием времени выхода

            if (graph.get(i).getColor() == -1)  {

                events.add(new Event(1));
                events.get(events.size() - 1).setNameVertex(graph.get(i).getNAME());
                events.get(events.size() - 1).setTextHints("Запуск поиска в глубину на исходном графе из вершины "
                        + graph.get(i).getNAME() + ".\n");
                //событие: начало дфс - с какой вершины

                dfs1(i);
            }
        }

        this.listEdge = inverting();                                 //инвертирование инвертированного списка рёбер

        events.add(new Event(5));
        events.get(events.size()-1).setInventedListEdge(this.listEdge);
        events.get(events.size() - 1).setTextHints("Изменение направлений ребер на противоположные. " +
                "Инвертированный граф изображен на экране.\n");
        //событие: инвертирование графа

        graph.clear();
        initGraph();                                            //инвертированный граф

        for (int i = timeOut.size() - 1; i >= 0; i--) {                 //идем по убыванию времени выхода
            int index = find(timeOut.get(i).getKey());                  //индекс вершины в графе
            events.add(new Event(10));
            events.get(events.size()-1).setNameVertex(graph.get(index).getNAME());
            events.get(events.size()-1).setTextHints("Следующая вершина по порядку времени выхода " +
                    graph.get(index).getNAME() + ".\n");
            if (graph.get(index).getColor() == -1) {                    //если не посещена раннее
                graph.get(index).setComponent(component++);             //фиксируем принадлежность к компоненте

                events.add(new Event(6));
                events.get(events.size() - 1).setTransition(graph.get(index).getNAME(), graph.get(index).getComponent());
                events.get(events.size() - 1).setTextHints("Запуск поиска в глубину из вершины " + graph.get(index).getNAME() +
                        " в обратном порядке, текущая компонента связности " + graph.get(index).getComponent() + ".\n");
                //событие: начало dfs - вершина, компонента

                dfs2(index);
            }
        }

        this.listEdge = inverting();                                 //инвертированный список ребер

        events.add(new Event(5));
        events.get(events.size()-1).setInventedListEdge(this.listEdge);
        events.get(events.size() - 1).setTextHints("Изменение направлений ребер на противоположные. " +
                "Инвертированный граф изображен на экране.\n");
        //событие: инвертирование графа
        return events;
    }

    private void initGraph() {
        //принимает список ребер и преобразовывает в списки смежности вершин

        for(int i = 0; i < listEdge.size(); i++) {

            Vertex temp1 = new Vertex(listEdge.get(i).getKey());                    //добавляемая вершина
            Vertex temp2 = new Vertex(listEdge.get(i).getValue());                  //добавляемая вершина

            int index1 = 0;                                                         //индекс вхождения в граф
            int index2 = 0;                                                         //индекс вхождения в граф

            if (graph.isEmpty()) {                                                  //в графе нет ни одной вершины
                graph.add(temp1);
                if (temp1.getNAME() != temp2.getNAME() && temp2.getNAME() != -1) {  //если не петля или изолированная вершина
                    graph.add(temp2);
                }
                if(temp2.getNAME() != -1) graph.get(0).setAdjacentVertex(temp2);    //если не изолированная вершина

            } else {                                                                //если граф не пуст
                for (int j = 0; j < graph.size(); j++) {
                    index1 = find(temp1.getNAME());
                    if (index1 == -1) {                                             //вершина еще не добавлена в граф
                        graph.add(temp1);                                           //добавляем
                        index1 = graph.size() - 1;                                  //запоминаем индекс
                    }
                    index2 = find(temp2.getNAME());
                    if (index2 == -1) {                                             //вершина еще не добавлена в граф
                        if (temp1.getNAME() != temp2.getNAME() && temp2.getNAME() != -1) {
                            graph.add(temp2);                                       //добавляем
                        }
                        index2 = graph.size() - 1;                                  //запоминаем индекс
                    }
                    if (temp2.getNAME() != -1) graph.get(index1).setAdjacentVertex(graph.get(index2));          //добавляем смежную вершину
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

        //  graph.get(index).setTimeIn(dfsTimer++);                                                                 //время входа
        graph.get(index).setColor(0);                                                                           //обрабатывается

        for(int i = 0; i < graph.get(index).getAdjacentVertex().size(); i++) {
            if(graph.get(index).getAdjacentVertex().get(i).getColor() == -1) {                                  //если есть смежная непосещенная
                if(graph.get(index).getNAME() != graph.get(index).getAdjacentVertex().get(i).getNAME())
                    graph.get(index).getAdjacentVertex().get(i).setWhence(graph.get(index).getNAME());          //запоминаем как в нее пришли

                events.add(new Event(3));
                events.get(events.size() - 1).setTransition(graph.get(index).getNAME(), graph.get(index).getAdjacentVertex().get(i).getNAME());
                events.get(events.size() - 1).setTextHints("Найдено ребро между вершинами " + graph.get(index).getNAME() +
                        " и " + graph.get(index).getAdjacentVertex().get(i).getNAME() + ". Переход из " +
                        graph.get(index).getNAME() + " в " + graph.get(index).getAdjacentVertex().get(i).getNAME() + ".\n");
                //событие: переход по ребру - откуда, куда

                dfs1(find(graph.get(index).getAdjacentVertex().get(i).getNAME()));                          //запускаем поиск от нее
            }
            else {
                events.add(new Event(11));
                events.get(events.size() - 1).setTransition(graph.get(index).getNAME(), graph.get(index).getAdjacentVertex().get(i).getNAME());
                events.get(events.size() - 1).setTextHints("Вершина " + graph.get(index).getAdjacentVertex().get(i).getNAME() +
                        " уже была посещена ранее\n");

            }
        }
        graph.get(index).setColor(1);                                                                           //обрабатана
        graph.get(index).setTimeOut(dfsTimer++);                                                                //время выхода
        timeOut.add(new Pair(graph.get(index).getNAME(), graph.get(index).getTimeOut()));

        if(graph.get(index).getWhence() != -1) {
            events.add(new Event(4));
            events.get(events.size() - 1).setTransition(graph.get(index).getNAME(), graph.get(index).getWhence());
            events.get(events.size() - 1).setTextHints("У вершины " + graph.get(index).getNAME() +
                    " больше нет непосещенных соседних вершин. Возврат на " + graph.get(index).getWhence() + ".\n");
            //событие: возврат - вершина, из которой возвращаемся, вершина в которую возвращаемся
        }
        else{
            events.add(new Event(2));
            events.get(events.size() - 1).setNameVertex(graph.get(index).getNAME());
            events.get(events.size() - 1).setTextHints("У вершины " + graph.get(index).getNAME() +
                    " больше нет непосещенных соседних вершин.\n");
            //событие: конец дфс - когда по дфс идти больше некуда и вернуться на другую вершину нельзя
        }
    }

    private ArrayList<Pair<Integer, Integer>> inverting() {

        ArrayList<Pair<Integer, Integer>> inventedListEdge = new ArrayList<Pair<Integer, Integer>>();

        for(int i = 0; i < listEdge.size(); i++) {
            if(listEdge.get(i).getValue() == -1)                                                                 //изолированная вершина
                inventedListEdge.add(new Pair(listEdge.get(i).getKey(), listEdge.get(i).getValue()));
            else
                inventedListEdge.add(new Pair(listEdge.get(i).getValue(), listEdge.get(i).getKey()));
        }
        return inventedListEdge;                                                                                //возвращаем инвертированный список ребер
    }

    private void dfs2(int index) {

        graph.get(index).setColor(0);                                                                           //обрабатывается
        for(int i = 0; i < graph.get(index).getAdjacentVertex().size(); i++) {
            if(graph.get(index).getAdjacentVertex().get(i).getColor() == -1) {                                  //если есть смежная непосещенная
                if(graph.get(index).getNAME() != graph.get(index).getAdjacentVertex().get(i).getNAME()) {
                    graph.get(index).getAdjacentVertex().get(i).setWhence(graph.get(index).getNAME());          //запоминаем как в нее пришли
                    graph.get(index).getAdjacentVertex().get(i).setComponent(graph.get(index).getComponent());  //принадлежность к компоненте

                    events.add(new Event(8));
                    events.get(events.size() - 1).setDataDFS2(graph.get(index).getNAME(),
                            graph.get(index).getAdjacentVertex().get(i).getNAME(), graph.get(index).getComponent());
                    events.get(events.size() - 1).setTextHints("Найдено ребро между вершинами " + graph.get(index).getNAME() +
                            " и " + graph.get(index).getAdjacentVertex().get(i).getNAME() +
                            ". Переход из " + graph.get(index).getNAME() + " в " + graph.get(index).getAdjacentVertex().get(i).getNAME() +
                            ". Текущая компонента связности: " + graph.get(index).getComponent() + ".\n");
                    //событие: переход по ребру - вершина, из которой вышли, вершина куда пришли

                    dfs2(find(graph.get(index).getAdjacentVertex().get(i).getNAME()));                          //запускаем поиск от нее
                }
            }
        }
        graph.get(index).setColor(1);                                               //обрабатана
        if(graph.get(index).getWhence() != -1) {
            events.add(new Event(9));
            events.get(events.size() - 1).setTransition(graph.get(index).getNAME(), graph.get(index).getWhence());
            events.get(events.size() - 1).setTextHints("У вершины " + graph.get(index).getNAME() +
                    " больше нет непосещенных соседних вершин. Возврат на " + graph.get(index).getWhence() + ".\n");
            //событие: возврат - вершина, из которой возвращаемся, вершина в которую возвращаемся
        }
        else{
            events.add(new Event(7));
            events.get(events.size() - 1).setNameVertex(graph.get(index).getNAME());
            events.get(events.size() - 1).setTextHints("У вершины " + graph.get(index).getNAME() +
                    " больше нет непосещенных соседних вершин.\n");
            //событие: конец дфс - когда по дфс идти больше некуда и вернуться на другую вершину нельзя
        }
    }

    private int find(Integer name) {                                                //возвращает индекс вхождения вершины в граф
        for(int i = 0; i < graph.size(); i++){                                      //или -1
            if (graph.get(i).getNAME() == name) {
                return i;
            }
        }
        return -1;
    }

}
