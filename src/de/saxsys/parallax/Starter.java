package de.saxsys.parallax;


import de.saxsys.parallax.algorithm.ParallaxPane;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Starter extends Application {
	
	@Override
	public void start(Stage stage) {
		
		Image[] loadImages = new ImageLoader().loadImages("/couch");
		
		ParallaxPane stack = new ParallaxPane();
		stack.setLightingEnabled(true);
		
		for (int i = 0; i < loadImages.length; i++) {
			Image image = loadImages[i];
			Group createImageGroup = createImageGroup(image, 0.5);
			stack.getChildren().add(createImageGroup);
		}
		
		Node tv = stack.getChildren().get(1);
		
		InvalidationListener change = observable -> tv
				.setLayoutX(stack.getWidth() / 2 - tv.getLayoutBounds().getWidth() / 2);
		stack.widthProperty().addListener(change);
		tv.layoutBoundsProperty().addListener(change);
		
		Scene scene = new Scene(new VBox(stack, new Button()));
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	private Group createImageGroup(Image front, double scaleFactor) {
		ImageView frontImage = new ImageView();
		frontImage.setFitWidth(front.getWidth() * scaleFactor);
		frontImage.setPreserveRatio(true);
		frontImage.setImage(front);
		return new Group(frontImage);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}