package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("sample.fxml"));
        Parent root = loader.load();
        Controller contr = loader.getController();
        contr.resizeCanvas();

        primaryStage.setTitle("Strong connectivity");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.widthProperty().addListener(contr.listener);
        primaryStage.heightProperty().addListener(contr.listener);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
