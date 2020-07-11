package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Controller {
    boolean isBtnAddVClicked = false;
    boolean isBtnAddRClicked = false;
    boolean isBtnDelVClicked = false;
    boolean isBtnDelRClicked = false;
    boolean isDraggedProcess = false;
    boolean isVisualiseProcess = false;

    @FXML
    // Function for reacting to a button click
    private void isBtnAddVertClicked() {
        if(!isBtnAddVClicked) {
            mainTextArea.setText("Нажмите на поле для добавления вершины.");
            isBtnAddVClicked = true;
            isBtnAddRClicked = false;
            isBtnDelVClicked = false;
            isBtnDelRClicked = false;
        }
        else isBtnAddVClicked = false;
        setPrevChosenVertex(null);
    }

    @FXML
    private void isBtnAddRibClicked() {
        if(!isBtnAddRClicked) {
            mainTextArea.setText("Выберите две вершины для добавления ребра.");
            isBtnAddRClicked = true;
            isBtnAddVClicked = false;
            isBtnDelVClicked = false;
            isBtnDelRClicked = false;
        }
        else {
            isBtnAddRClicked = false;
            mainTextArea.setText("");
        }
        setPrevChosenVertex(null);
    }

    @FXML
    private void isBtnDelVertClicked() {
        if(!isBtnDelVClicked) {
            mainTextArea.setText("Выберите вершины для удаления.");
            isBtnDelVClicked = true;
            isBtnDelRClicked = false;
            isBtnAddRClicked = false;
            isBtnAddVClicked = false;
        }
        else {
            isBtnDelVClicked = false;
            mainTextArea.setText("");
        }
        setPrevChosenVertex(null);
    }

    @FXML
    private void isBtnDelRibClicked() {
        if(!isBtnDelRClicked) {
            mainTextArea.setText("Выберите две вершины для удаления ребра.");
            isBtnDelRClicked = true;
            isBtnDelVClicked = false;
            isBtnAddRClicked = false;
            isBtnAddVClicked = false;
        }
        else isBtnDelRClicked = false;
        setPrevChosenVertex(null);
    }

    // обработка нажатия на область для добавления графа
    private void canvasClick(javafx.scene.input.MouseEvent mouseEvent) {
        Vertex curChosenVertex = graph.checkCollision(mouseEvent.getX(), mouseEvent.getY(), 0);
        if (isBtnAddVClicked || isBtnAddRClicked) {
            if (curChosenVertex == null && isBtnAddVClicked) {
                int numb = 1;
                while(graph.findVertex(numb) != null)
                    numb++;
                graph.addVertex(new Vertex(mouseEvent.getX(), mouseEvent.getY(), numb));
                setPrevChosenVertex(null);
            }
            else if (curChosenVertex != null && prevChosenVertex != null) {
                graph.addEdge(prevChosenVertex, curChosenVertex);
                setPrevChosenVertex(null);
            }
            else if (isBtnAddRClicked) {
                setPrevChosenVertex(curChosenVertex);
            }
        }
        else if (isBtnDelVClicked) {
            if (curChosenVertex != null) {
                graph.removeVertex(curChosenVertex);
                setPrevChosenVertex(null);
            }
        }
        else if (isBtnDelRClicked) {
            if (curChosenVertex != null && prevChosenVertex != null) {
                graph.removeEdge(prevChosenVertex, curChosenVertex);
                setPrevChosenVertex(null);
            }
            else {
                setPrevChosenVertex(curChosenVertex);
            }
        }
    }

    @FXML
    private void canvasMousePressed(javafx.scene.input.MouseEvent mouseEvent) {
        draggedVertex = graph.checkCollision(mouseEvent.getX(), mouseEvent.getY(), 0);
    }

    @FXML
    private void canvasMouseReleased(javafx.scene.input.MouseEvent mouseEvent) {
        if (!isVisualiseProcess && !isDraggedProcess) {
            canvasClick(mouseEvent);
        }
        draggedVertex = null;
        isDraggedProcess = false;
    }

    @FXML
    private void canvasMouseDragged(javafx.scene.input.MouseEvent mouseEvent) {
        if (draggedVertex != null) {
            draggedVertex.setX(mouseEvent.getX());
            draggedVertex.setY(mouseEvent.getY());
            isDraggedProcess = true;
        }
    }

    @FXML
    private void runAlgorithm() {
        isBtnAddVClicked = false;
        isBtnAddRClicked = false;
        isBtnDelVClicked = false;
        isBtnDelRClicked = false;
        setPrevChosenVertex(null);
        if (graph.isEmpty()) {
            mainTextArea.setText("Граф пуст.\n");
            return;
        }

        switchVisualize();
        mainTextArea.setText("Алгоритм запущен.\n");
        mainLabel.getChildren().add(new Label("Порядок вершин:"));
        graph.runAlgorithm();
    }

    @FXML
    private void clearButton() {
        isBtnAddVClicked = false;
        isBtnAddRClicked = false;
        isBtnDelVClicked = false;
        isBtnDelRClicked = false;
        setPrevChosenVertex(null);
        mainTextArea.setText("Поле очищено.\n");
        graph = new Graph();
    }

    @FXML
    private void stopButton() {
        isBtnAddVClicked = false;
        isBtnAddRClicked = false;
        isBtnDelVClicked = false;
        isBtnDelRClicked = false;
        setPrevChosenVertex(null);
        graph.endAlgorithm(mainTextArea, mainLabel);
        mainTextArea.setText("Алгоритм остановлен.\n");
        switchVisualize();
    }

    @FXML
    private void stepForward() {
        if (graph.isLastEvent()) {
            graph.endAlgorithm(mainTextArea, mainLabel);
            disableButton(stepButton);
            disableButton(showButton);
        }
        else
            graph.visualiseStep(mainTextArea, mainLabel);
        activeButton(stepButtonBack);
    }

    @FXML
    private void stepBack() {
        graph.visualiseStepBack(mainTextArea, mainLabel);
        if (graph.isFirstEvent()) {
            disableButton(stepButtonBack);
        }
        activeButton(stepButton);
        activeButton(showButton);
        if (graph.isLastEvent()) {
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(50),
                            ae -> mainTextArea.setScrollTop(Double.MAX_VALUE)
                    )
            );
            timeline.setCycleCount(1);
            timeline.play();
        }
    }

    @FXML
    private void showResult() {
        graph.endAlgorithm(mainTextArea, mainLabel);
        disableButton(stepButton);
        disableButton(showButton);
        activeButton(stepButtonBack);
    }

    @FXML
    private void importG() throws Throwable
    {
        ModalWindow.newWindow("Импорт из файла");
        graph = new Graph();
        graph.inputFileGraph(mainCanvas.getGraphicsContext2D(), mainTextArea);
    }

    public void resizeCanvas() {
        mainCanvas.setHeight(((Pane)(mainCanvas.getParent())).getHeight());
        mainCanvas.setWidth(((Pane)(mainCanvas.getParent())).getWidth());
        graph.drawAll(mainCanvas.getGraphicsContext2D());
    }

    //переключение между режимими добавления графа и показом алгоритма
    private void switchVisualize() {
        graph.standardView(mainLabel);
        if (isVisualiseProcess) {
            activeButton(importButton);
            activeButton(addVButton);
            activeButton(addEButton);
            activeButton(delVButton);
            activeButton(delEButton);
            activeButton(runButton);
            disableButton(stepButton);
            disableButton(stepButtonBack);
            disableButton(showButton);
            disableButton(stopButton);
            activeButton(clearButton);
        }
        else {
            disableButton(importButton);
            disableButton(addVButton);
            disableButton(addEButton);
            disableButton(delVButton);
            disableButton(delEButton);
            disableButton(runButton);
            activeButton(stepButton);
            disableButton(stepButtonBack);
            activeButton(showButton);
            activeButton(stopButton);
            disableButton(clearButton);
        }
        isVisualiseProcess = !isVisualiseProcess;
    }

    private void disableButton(Button button) {
        button.setOpacity(0.75);
        button.setDisable(true);
    }
    private void activeButton(Button button) {
        button.setOpacity(1.0);
        button.setDisable(false);
    }

    // запоминание предыдущей выбраной вершины для добавления и удаления рёбер
    private void setPrevChosenVertex(Vertex vert) {
        if (prevChosenVertex != null) prevChosenVertex.setRinged(false);
        prevChosenVertex = vert;
        if (prevChosenVertex != null) prevChosenVertex.setRinged(true);
    }

    @FXML
    private Canvas mainCanvas;

    @FXML
    private TextArea mainTextArea;

    @FXML
    private HBox mainLabel;

    @FXML
    private Button importButton, addVButton, addEButton, delVButton, delEButton,
            runButton, stepButton, stepButtonBack, showButton, stopButton, clearButton;

    private Graph graph = new Graph();

    private Vertex prevChosenVertex;
    private Vertex draggedVertex;

}
