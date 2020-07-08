package sample;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


import java.io.IOException;

public class Controller {

//    public final LogString logging = new LogString();

    boolean isBtnAddVClicked = false;
    boolean isBtnAddRClicked = false;
    boolean isBtnDelVClicked = false;
    boolean isBtnDelRClicked = false;
    boolean isVisualiseProcess = false;

    @FXML
    // Function for reacting to a button click
    public void isBtnAddVertClicked() {
        graph.standardView(mainCanvas.getGraphicsContext2D(), mainLabel);
        isVisualiseProcess = false;
        if(!isBtnAddVClicked) {
            isBtnAddVClicked = true;
            isBtnAddRClicked = false;
            isBtnDelVClicked = false;
            isBtnDelRClicked = false;

        }
        else isBtnAddVClicked = false;
        prevChosenVertex = null;
    }
    @FXML
    public void isBtnAddRibClicked() {
        graph.standardView(mainCanvas.getGraphicsContext2D(), mainLabel);
        isVisualiseProcess = false;
        if(!isBtnAddRClicked) {
            isBtnAddRClicked = true;
            isBtnAddVClicked = false;
            isBtnDelVClicked = false;
            isBtnDelRClicked = false;
        }
        else isBtnAddRClicked = false;
        prevChosenVertex = null;
    }
    @FXML
    public void isBtnDelVertClicked() {
        graph.standardView(mainCanvas.getGraphicsContext2D(), mainLabel);
        isVisualiseProcess = false;
        if(!isBtnDelVClicked) {
            isBtnDelVClicked = true;
            isBtnDelRClicked = false;
            isBtnAddRClicked = false;
            isBtnAddVClicked = false;
        }
        else isBtnDelVClicked = false;
        prevChosenVertex = null;
    }
    @FXML
    public void isBtnDelRibClicked() {
        graph.standardView(mainCanvas.getGraphicsContext2D(), mainLabel);
        isVisualiseProcess = false;
        if(!isBtnDelRClicked) {
            isBtnDelRClicked = true;
            isBtnDelVClicked = false;
            isBtnAddRClicked = false;
            isBtnAddVClicked = false;
        }
        else isBtnDelRClicked = false;
        prevChosenVertex = null;
    }

    public void canvasClick(javafx.scene.input.MouseEvent mouseEvent) {
        if (isVisualiseProcess) return;
        Vertex curChosenVertex = graph.checkCollision(mouseEvent.getX(), mouseEvent.getY(), 0);
        if (isBtnAddVClicked || isBtnAddRClicked) {
            if (curChosenVertex == null && isBtnAddVClicked) {  // только при нажатии кнопки добавления вершины
                graph.addVertex(new Vertex(mouseEvent.getX(), mouseEvent.getY()),
                        mainCanvas.getGraphicsContext2D());
                prevChosenVertex = null;
            }
            else if (curChosenVertex != null && prevChosenVertex != null) {
                graph.addEdge(prevChosenVertex, curChosenVertex, mainCanvas.getGraphicsContext2D());
                prevChosenVertex = null;
            }
            else if (isBtnAddRClicked)
                prevChosenVertex = curChosenVertex;
        }
        else if (isBtnDelVClicked) {
            if (curChosenVertex != null) {
                graph.removeVertex(curChosenVertex, mainCanvas.getGraphicsContext2D());
                prevChosenVertex = null;
            }
        }
        else if (isBtnDelRClicked) {
            if (curChosenVertex != null && prevChosenVertex != null) {
                graph.removeEdge(prevChosenVertex, curChosenVertex, mainCanvas.getGraphicsContext2D());
                prevChosenVertex = null;
            }
            else {
                prevChosenVertex = curChosenVertex;
            }
        }
    }

    public void runAlgorithm() {
        isBtnAddVClicked = false;
        isBtnAddRClicked = false;
        isBtnDelVClicked = false;
        isBtnDelRClicked = false;
        prevChosenVertex = null;

        if (!isVisualiseProcess) {
            mainTextArea.setText("Запуск алгоритма");
            isVisualiseProcess = true;
//            mainLabel.setMaxWidth(mainCanvas.getWidth());
            graph.standardView(mainCanvas.getGraphicsContext2D(), mainLabel);
            mainLabel.getChildren().add(new Label("Порядок вершин:"));
            graph.runAlgorithm(mainCanvas.getGraphicsContext2D());
        }
        else {
            mainTextArea.setText("Алгоритм уже запущен");
        }
    }

    public void clearButton() {
        isBtnAddVClicked = false;
        isBtnAddRClicked = false;
        isBtnDelVClicked = false;
        isBtnDelRClicked = false;
        prevChosenVertex = null;
        mainTextArea.setText("Поле очищено.");
        graph = new Graph();
        graph.standardView(mainCanvas.getGraphicsContext2D(), mainLabel);
        graph.drawAll(mainCanvas.getGraphicsContext2D());
    }

    public void stepForward() {
        if (isVisualiseProcess) {
            if (graph.isLastEvent()) {
                isVisualiseProcess = false;
                graph.endAlgorithm(mainCanvas.getGraphicsContext2D(), mainTextArea, mainLabel);
            }
            else
                graph.visualiseStep(mainCanvas.getGraphicsContext2D(), mainTextArea, mainLabel);
        }
    }

    public void showResult() {
        if (isVisualiseProcess)
            graph.endAlgorithm(mainCanvas.getGraphicsContext2D(), mainTextArea, mainLabel);
        isVisualiseProcess = false;
    }


    /**
     * Import from file
     * */

    @FXML
    public void importG() throws IOException
    {
        ModalWindow.newWindow("Импорт из файла");
        graph.standardView(mainCanvas.getGraphicsContext2D(), mainLabel);
        graph = new Graph();
        graph.drawAll(mainCanvas.getGraphicsContext2D());
        graph.inputFileGraph(mainCanvas.getGraphicsContext2D(), mainTextArea);
    }

    public ChangeListener listener = (observableValue, o, t1) -> resizeCanvas();

    public void resizeCanvas() {
        mainCanvas.setHeight(((Pane)(mainCanvas.getParent())).getHeight());
        mainCanvas.setWidth(((Pane)(mainCanvas.getParent())).getWidth());
        graph.drawAll(mainCanvas.getGraphicsContext2D());
    }

    @FXML
    private Canvas mainCanvas;

    @FXML
    private TextArea mainTextArea;

    @FXML
    private HBox mainLabel;

    private Graph graph = new Graph();

    private Vertex prevChosenVertex;

}
