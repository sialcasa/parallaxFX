package de.saxsys.parallax;


import de.saxsys.parallax.algorithm.ParallaxImpl;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloMenu extends Application {
	
	@Override
	public void start(Stage stage) {
		// load the image
		Image image0 = new Image(getClass().getResource("couch/0.png").toExternalForm());
		Image image1 = new Image(getClass().getResource("couch/1.png").toExternalForm());
		Image image2 = new Image(getClass().getResource("couch/2.png").toExternalForm());
		
		// simple displays ImageView the image as is
		Group image0group = createImageGroup(image0);
		Group image1group = createImageGroup(image1);
		Group image2group = createImageGroup(image2);
		
		StackPane stack = new StackPane();
		stack.getChildren().addAll(image0group, image1group, image2group);
		
		// new Parallax().applyParallax(stack);
		new ParallaxImpl().applyParallax(stack);
		// new Parallax(stack);
		
		Scene scene = new Scene(stack);
		
		stage.setTitle("ImageView");
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	private Group createImageGroup(Image front) {
		ImageView frontImage = new ImageView();
		frontImage.setFitHeight(350);
		frontImage.setPreserveRatio(true);
		frontImage.setImage(front);
		return new Group(frontImage);
	}
	
	
	
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}