package sample;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.util.Pair;


import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

import static java.util.logging.Level.ALL;
import static java.util.logging.Level.WARNING;

public class Controller {

//    public final LogString logging = new LogString();

    /**
     * Add vertex
     */

    @FXML
    Button BtnAddV;
    @FXML
    Button BtnImport;

    boolean isBtnAddVClicked = false;
    boolean isBtnAddRClicked = false;
    boolean isBtnDelVClicked = false;
    boolean isBtnDelRClicked = false;

    @FXML
    // Function for reacting to a button click
    public void isBtnAddVertClicked() {
        if(!isBtnAddVClicked) {
            isBtnAddVClicked = true;
            isBtnAddRClicked = false;
        }
        else isBtnAddVClicked = false;
    }
    @FXML
    public void isBtnAddRibClicked() {
        if(!isBtnAddRClicked) {
            isBtnAddRClicked = true;
            isBtnAddVClicked = false;
        }
        else isBtnAddRClicked = false;
    }
    @FXML
    public void isBtnDelVertClicked() {
        if(!isBtnDelVClicked) {
            isBtnDelVClicked = true;
            isBtnDelRClicked = false;
            isBtnAddRClicked = false;
            isBtnAddVClicked = false;
        }
        else isBtnDelVClicked = false;
    }
    @FXML
    public void isBtnDelRibClicked() {
        if(!isBtnDelRClicked) {
            isBtnDelRClicked = true;
            isBtnDelVClicked = false;
            isBtnAddRClicked = false;
            isBtnAddVClicked = false;
        }
        else isBtnDelRClicked = false;
    }

    public void canvasClick(javafx.scene.input.MouseEvent mouseEvent) {
        if (isBtnAddVClicked || isBtnAddRClicked) {
            Vertex curChosenVertex = graph.checkCollision(mouseEvent.getX(), mouseEvent.getY());
            if (curChosenVertex == null && isBtnAddVClicked) {  // только при нажатии кнопки добавления вершины
                graph.addVertex(new Vertex(mouseEvent.getX(), mouseEvent.getY()),
                        mainCanvas.getGraphicsContext2D());
                prevChosenVertex = null;
            }
            else if (curChosenVertex != null && prevChosenVertex != null && prevChosenVertex
                    != curChosenVertex && isBtnAddRClicked) {
                graph.addEdge(prevChosenVertex, curChosenVertex, mainCanvas.getGraphicsContext2D());
                prevChosenVertex = null;
            }
            else if (isBtnAddRClicked)
                prevChosenVertex = curChosenVertex;
            else {
                prevChosenVertex = null;
            }

        }
    }

    public void runAlgorithm() {
        isBtnAddVClicked = false;
        isBtnAddRClicked = false;
        isBtnDelVClicked = false;
        isBtnDelRClicked = false;
        graph.runAlgorithm(mainCanvas.getGraphicsContext2D());
        prevChosenVertex = null;
    }

    public void clearButton() {
        isBtnAddVClicked = false;
        isBtnAddRClicked = false;
        isBtnDelVClicked = false;
        isBtnDelRClicked = false;
        prevChosenVertex = null;
    }


    /**
     * Import from file
     * */

    // Function for reacting to a button click
//    public void isBtnImportClicked(){
//        isBtnAddVClicked = true;
//    }

    @FXML
    public void importG() throws IOException
    {
        ModalWindow.newWindow("Импорт из файла");
        graph.inputFileGraph(mainCanvas.getGraphicsContext2D(), mainTextArea);
    }

    public ChangeListener listener = (observableValue, o, t1) -> resizeCanvas();

    private void resizeCanvas() {
        mainCanvas.setHeight(((Pane)(mainCanvas.getParent())).getHeight());
        mainCanvas.setWidth(((Pane)(mainCanvas.getParent())).getWidth());
        graph.drawAll(mainCanvas.getGraphicsContext2D());
    }


    @FXML
    private Canvas mainCanvas;

    @FXML
    private TextArea mainTextArea;

    private final Graph graph = new Graph();

    private Vertex prevChosenVertex;

}
