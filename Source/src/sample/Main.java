package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("sample.fxml"));
        Parent root = loader.load();
        Controller contr = loader.getController();

        Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(contr::resizeCanvas);
            }
        }, 0, 50);
        primaryStage.setOnCloseRequest(windowEvent -> {timer.cancel();});
        primaryStage.setTitle("Strong connectivity");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
