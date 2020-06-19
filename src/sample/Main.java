package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class Main extends Application {
    protected static String jsnFdir;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Automata coffee");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
//        URL u = Main.class.getClass().getResource("/sample/products and prices.json");
//        String s = u.toString();
//        System.out.println("1111");
//        System.out.println("***"+s);

        launch(args);
    }



}
