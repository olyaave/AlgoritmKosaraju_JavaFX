package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModalWindow {
    public static void closeFile(String str, Stage window) {
        fileName = str;
        window.close();
    }

    public static void newWindow(String title) {
        fileName = "";
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
//        Pane pane = new Pane();
        StackPane pane = new StackPane();
        double width = 500;
        double heigth = 300;

        Label label = new Label("Введите в файл рёбра в виде пар чисел через перенос строки. Далее введите " +
                "имя файла(name.txt) в поле ниже для импорта графа из файла.\n" +
                "В папке Tests находятся тестовые файлы, которые можно использовать для тестирования. Для этого введите \"Tests/test{номер теста}.txt.\"" +
                "\n\n"+
                "Формат ввода: \n" +
                "                               1 1 - ориентированное ребро,\n" +
                "                               3 -1 - изолированная вершина,\n" +
                "                               7 7 - петля\n");
        label.setMaxWidth(width*9/10);
        label.setWrapText(true);
        TextField textField = new TextField();
        textField.setMaxWidth(width*9/10);
        Button btn = new Button("OK");
        btn.setMinWidth(width*3/20);
        VBox vbox = new VBox(label, textField);
        VBox.setMargin(textField, new Insets(5, 10, 5, 10));
        VBox.setMargin(label, new Insets(5, 10, width*1/40, 10));

        vbox.setAlignment(Pos.CENTER);

        btn.setOnAction(event->closeFile(textField.getText(), window));

        pane.getChildren().addAll(vbox, btn);
        pane.setMargin(btn, new Insets(Math.max(width*1/40, 5), width*1/20, width*1/40, 10));
        pane.setAlignment(btn, Pos.BOTTOM_RIGHT);

        Scene scene = new Scene(pane, width, heigth);
        window.setScene(scene);
        window.setTitle(title);
        window.showAndWait();
    }

    public static String fileName = "";
}
