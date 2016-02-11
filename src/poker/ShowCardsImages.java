/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

/**
 *
 * @author ricky
 */
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class ShowCardsImages extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.out.println("start");
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20, 20, 20, 20));
        pane.setHgap(20);
        pane.setVgap(20);
        Card[] cards = Card.values();
        Image[] images = new Image[cards.length];
        ImageView[] imageViews = new ImageView[cards.length];
        BooleanProperty[] booleanProperties = new BooleanProperty[cards.length];

        for (int i = 0; i < cards.length; ++i) {
            String path = "file:PNG-cards-1.3/"
                    + cards[i].toString()
                    + ".png";
            System.out.println(path);
            images[i] = new Image(path);
            imageViews[i] = new ImageView(images[i]);
            imageViews[i].setFitHeight(180);
            imageViews[i].setFitWidth(150);
            booleanProperties[i] = imageViews[i].preserveRatioProperty();
            booleanProperties[i].set(true);

            if ((int) (100 * Math.random()) < 100) {
                pane.add(imageViews[i], i / 4, i % 4);
            }
        }

        Scene scene = new Scene(pane);
        primaryStage.setTitle("ShowImage"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.out.println("launch application");
        Application.launch(args);
    }
}
