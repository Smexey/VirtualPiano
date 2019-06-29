package gui;

import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

class KeyboardPane extends GridPane {
    KeyboardPane(double width, double height) {
        super();
        setWidth(width);
        setHeight(height);

        for (int i = 0; i < 10; i++) {
            Canvas canvas = new Canvas(30, 60);
            add(canvas, i, 0);
            System.out.println(i);
            canvas.widthProperty().bind(this.widthProperty());
            canvas.heightProperty().bind(this.heightProperty());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            if (i % 2 == 0) {
                gc.setFill(Color.GREEN);
                gc.setStroke(Color.BLUE);
                gc.fillOval(10, 60, 30, 30);
            } else {
                gc.setFill(Color.YELLOW);
                gc.setStroke(Color.GRAY);
                gc.strokeLine(40, 10, 10, 40);
            }

            gc.setLineWidth(5);
            
            
            gc.strokeOval(60, 60, 30, 30);
            System.out.println(canvas.getLayoutX());
        }

        System.out.println(getChildren());

        setStyle("-fx-background-color: red");
    }

}