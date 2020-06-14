import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    String jsnFdir;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample/automata/sample.fxml"));
        primaryStage.setTitle("Automata coffee");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        URL jsnFileURL = Main.class.getResource("products and prices.json");
        String jsnFileDirection = jsnFileURL.toString();
        System.out.println(jsnFileDirection);


        launch(args);
    }



}
