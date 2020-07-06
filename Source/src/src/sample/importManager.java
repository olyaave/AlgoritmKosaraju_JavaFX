package sample;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/*
* Класс для импорта графа из файла. Файл следует хранить в корневой папке
* "folder_project/name.txt"
*/

public class importManager {

    private File f;
    private boolean isOpenAndReadOK;
    ArrayList<Pair<Integer, Integer>> graph;
    int size;

    public importManager(String name) throws IOException {

        Path pathImport = Paths.get(name);
        graph = new ArrayList<Pair<Integer, Integer>>();

        try(BufferedReader in = Files.newBufferedReader(pathImport)){

            size = Integer.parseInt(in.readLine());
            for (int i = 0; i < size; i++){

                String[] line = (in.readLine()).split(" ", 2);
                Integer start = Integer.parseInt(line[0]);
                Integer end = Integer.parseInt(line[1]);
                graph.add(new Pair(start, end));

            }
            isOpenAndReadOK = true;
        }
        catch(IOException e){
            System.out.println("Файл для импорта не найден: " +   e.getMessage());
        }
        catch(ClassCastException|NumberFormatException e){
            System.out.println("Формат текста в файле некорректный: " +  e.getMessage());
        }
    }

    public ArrayList<Pair<Integer, Integer>> getGraph(){
        return isOpenAndReadOK ? graph : null;
    }

    public void printGraph(){
        if(isOpenAndReadOK)
        for (int i = 0; i < size; i++){
            System.out.println(graph.get(i).getKey() + " -> " + graph.get(i).getValue()); // считывание графа и сохранение его в структуру
        }
    }

}
