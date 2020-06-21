package sample;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main extends Application {
    final int bgH = 600; //background hight
    final int bgW = 600; //background width
    final int cX = 300; //centre X
    final int cY = 150; //centre Y
    final int cR0 = 20; //circle0 radius
    final int cR1 = 60; //circle0 radius
    final int cR2 = 100; //circle0 radius
    final int cR3 = 140; //circle0 radius

    @Override
    public void start(Stage primaryStage) throws Exception{

        Calendar myCalendar = new GregorianCalendar();
        double secDegree = myCalendar.get(Calendar.SECOND) * (360 / 60);
        double minDegree = (myCalendar.get(Calendar.MINUTE) + secDegree / 360.0) * (360 / 60);
        double hourDegree = (myCalendar.get(Calendar.HOUR) + minDegree / 360) * (360 / 12);

//start drawing
        primaryStage.setTitle("My clock");
        URL background = getClass().getResource("Clock.jpg");
        Image im = new Image(background.toString(), bgW, bgH, false, false);
        ImageView ivBackground = new ImageView(im);

        Circle circle0 = new Circle(cX,cY,cR0);
        circle0.setFill(null);
        circle0.setStroke(Paint.valueOf("#FF7F50"));

        Circle circle1 = new Circle(cX,cY,cR1);
        circle1.setFill(null);
        circle1.setStroke(Paint.valueOf("#FF7F50"));

        Circle circle2 = new Circle(cX,cY,cR2);
        circle2.setFill(null);
        circle2.setStroke(Paint.valueOf("#FF7F50"));

        Circle circle3 = new Circle(cX,cY,cR3);
        circle3.setFill(null);
        circle3.setStroke(Paint.valueOf("#FF7F50"));

        Group circles = new Group(circle0,circle1,circle2,circle3);

        Label label1 = new Label("Fury Road");
        label1.layoutXProperty().bind(circle0.centerXProperty().subtract(250));
        label1.layoutYProperty().bind(circle0.centerYProperty().subtract(100));
        label1.setTextFill(Paint.valueOf("#DAA520"));
        label1.setFont(new Font("Arial" ,20));
        label1.setRotate(-45);

        Label label2 = new Label("Mad Max");
        label2.layoutXProperty().bind(circle0.centerXProperty().subtract(-150));
        label2.layoutYProperty().bind(circle0.centerYProperty().subtract(100));
        label2.setTextFill(Paint.valueOf("#FF7F50"));
        label2.setFont(new Font("Arial" ,20));
        label2.setRotate(45);

        Label label3 = new Label("by asintsov");
        label3.layoutXProperty().bind(circle0.centerXProperty().subtract(-200));
        label3.layoutYProperty().bind(circle0.centerYProperty().subtract(-400));
        label3.setTextFill(Paint.valueOf("#F8F8FF"));
        label3.setFont(new Font("Arial" ,15));

        Group labels = new Group(label1, label2, label3);

        Line lHour = new Line(0, -100, 0, -140);
        lHour.setStroke(Paint.valueOf("#DAA520"));
        lHour.setStrokeWidth(5);
        lHour.layoutXProperty().bind(circle0.centerXProperty());
        lHour.layoutYProperty().bind(circle0.centerYProperty());
        Rotate rotateHour = new Rotate();
        rotateHour.setAngle(hourDegree);
        lHour.getTransforms().addAll(rotateHour);

        Line lMin = new Line(0, -60, -0, -100);
        lMin.setStroke(Paint.valueOf("#DAA520"));
        lMin.setStrokeWidth(5);
        lMin.layoutXProperty().bind(circle0.centerXProperty());
        lMin.layoutYProperty().bind(circle0.centerYProperty());
        Rotate rotateMin = new Rotate();
        rotateMin.setAngle(minDegree);
        lMin.getTransforms().addAll(rotateMin);

        Line lSec = new Line(0, -20, -0, -60);
        lSec.setStroke(Paint.valueOf("#DAA520"));
        lSec.setStrokeWidth(5);
        lSec.layoutXProperty().bind(circle0.centerXProperty());
        lSec.layoutYProperty().bind(circle0.centerYProperty());
        Rotate rotateSec = new Rotate();
        rotateSec.setAngle(secDegree);
        lSec.getTransforms().addAll(rotateSec);

        Group lines = new Group(lHour,lMin, lSec);

        Group ticks = new Group();
        for (int i = 0; i <= 12; i++) {
            Line tick = new Line (0,-60,0,-100);
            tick.setStrokeWidth(1);
            tick.setStroke(Paint.valueOf("#FF7F50"));
            tick.layoutXProperty().bind(circle0.centerXProperty());
            tick.layoutYProperty().bind(circle0.centerYProperty());
            tick.getTransforms().add(new Rotate(i*(360/12)));
            ticks.getChildren().add(tick);
        }

        Group numbs = new Group();
        for (int i = 0; i < 12; i++) {
            Label numb = new Label(Integer.toString(i));
            numb.setTextFill(Paint.valueOf("#FF7F50"));
            numb.setFont(new Font("Arial" ,15));
            numb.layoutXProperty().bind(circle2.centerXProperty());
            numb.layoutYProperty().bind(circle2.centerYProperty().subtract(-30));
            Rotate rotNumb = new Rotate(i*(360/12)+185);
            rotNumb.setPivotY(-30);
            numb.getTransforms().add(rotNumb);
            numbs.getChildren().add(numb);
        }
//end drawing

        final Timeline secTimeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(60),
                        new KeyValue(
                                rotateSec.angleProperty(),
                                360 + secDegree,
                                Interpolator.LINEAR
                        )
                )
        );

        final Timeline minTimeline = new Timeline(
                new KeyFrame(
                        Duration.minutes(60),
                        new KeyValue(
                                rotateMin.angleProperty(),
                                360 + minDegree,
                                Interpolator.LINEAR
                        )
                )
        );
        final Timeline hourTimeline = new Timeline(
                new KeyFrame(
                        Duration.hours(12),
                        new KeyValue(
                                rotateHour.angleProperty(),
                                360 + hourDegree,
                                Interpolator.LINEAR
                        )
                )
        );

        secTimeline.setCycleCount(Animation.INDEFINITE);
        minTimeline.setCycleCount(Animation.INDEFINITE);
        hourTimeline.setCycleCount(Animation.INDEFINITE);

        secTimeline.play();
        minTimeline.play();
        hourTimeline.play();


        Group root = new Group(ivBackground, lines, circles, labels, ticks, numbs);
        Scene scene = new Scene(root, bgW, bgH);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
