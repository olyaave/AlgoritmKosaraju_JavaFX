package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
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
        Pane pane = new Pane();

        Label label = new Label("Введите в файл количество рёбер графа и рёбра\n" +
                "в виде пар чисел через перенос строки. Далее введите\n" +
                "имя файла в поле ниже для импорта графа из файла.");
        TextField textField = new TextField();
        Button btn = new Button("OK");
        VBox vbox = new VBox(label, textField, btn);
        vbox.setSpacing(15);
        btn.setOnAction(event->closeFile(textField.getText(), window));

        pane.getChildren().addAll(vbox);

        Scene scene = new Scene(pane, 400, 200);
        window.setScene(scene);
        window.setTitle(title);
        window.showAndWait();
    }

    public static String fileName = "";
}
