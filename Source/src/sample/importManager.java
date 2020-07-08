package sample;

import javafx.scene.control.TextArea;
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

public class ImportManager {

    private File f;
    private boolean isOpenAndReadOK;
    private ArrayList<Pair<Integer, Integer>> graph;
    int size;

    public ImportManager(String name, TextArea mainTextArea) throws IOException {

        Path pathImport = Paths.get(name);
        graph = new ArrayList<Pair<Integer, Integer>>();

        try(BufferedReader in = Files.newBufferedReader(pathImport)){
//            size = Integer.parseInt(in.readLine());
//            for (int i = 0; i < size; i++){
            String line = in.readLine();
            while (line != null){

                String[] rib = (line).split(" ", 2);
                if (rib.length < 2 || rib[0].equals("") || rib[1].equals("")) {
                    throw new NumberFormatException(line);
                }
                Integer start = Integer.parseInt(rib[0]);
                Integer end = Integer.parseInt(rib[1]);
                if (start < 0 || end == 0 || end < -1)
                    throw new NumberFormatException(line);
                graph.add(new Pair(start, end));

                line = in.readLine();
            }
            isOpenAndReadOK = true;
        }
        catch(IOException e) {
            mainTextArea.setText("Файл для импорта не найден.");
        }
        catch(ClassCastException|NumberFormatException e){
            mainTextArea.setText("Формат текста в файле некорректный: \"" +  e.getMessage() + "\"");
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
