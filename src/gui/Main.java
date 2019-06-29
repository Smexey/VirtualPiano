package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import piano.Piano;

/**
 * Main
 */
public class Main extends Application {
    private static Piano piano;
    private KeyboardPane keyboard;

    public Main() {
    }

    public static void main(String[] args) {
        System.out.println("started main");

        // String path = args[0];
        String path = "C:\\Users\\Pyo\\Desktop\\VirtualPiano\\map.csv";
        // Main m = new Main(new Piano(path));
        piano = new Piano(path);
        System.out.println("prelaunch");
        // showwindow
        launch(args);
    }


    

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Mužiža");
        // keyboard = new Scene();



        BorderPane root = new BorderPane();
        keyboard = new KeyboardPane(300,200);

        // root.getChildren().add(canvas);
        root.setTop(keyboard);
        root.setBottom(new Button("asdf"));
        root.setLeft(new Button("left"));
        // root.setRight(keyboard);
        
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        System.out.println(keyboard.getWidth());

    }

    private void drawShapes(GraphicsContext gc){
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        
    }
}