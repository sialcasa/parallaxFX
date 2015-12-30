package de.saxsys.parallax;


import de.saxsys.parallax.algorithm.ParallaxPane;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Starter extends Application {
	
	@Override
	public void start(Stage stage) {
		
		Image[] loadImages = new ImageLoader().loadImages("/couch");
		
		ParallaxPane stack = new ParallaxPane();
		
		for (int i = 0; i < loadImages.length; i++) {
			Image image = loadImages[i];
			Group createImageGroup = createImageGroup(image);
			stack.getChildren().add(createImageGroup);
		}
		
		
		Scene scene = new Scene(stack);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	private Group createImageGroup(Image front) {
		ImageView frontImage = new ImageView();
		frontImage.setFitWidth(800);
		frontImage.setPreserveRatio(true);
		frontImage.setImage(front);
		return new Group(frontImage);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}