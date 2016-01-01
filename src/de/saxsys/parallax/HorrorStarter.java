package de.saxsys.parallax;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.saxsys.parallax.algorithm.ParallaxPane;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HorrorStarter extends Application {
	
	Map<Node, Transition> transitions = new HashMap<>();
	
	@Override
	public void start(Stage stage) {
		
		Image[] loadImages = new ImageLoader().loadImages("/horror");
		
		ParallaxPane stack = new ParallaxPane();
		stack.setLightingEnabled(true);
		
		for (int i = 0; i < loadImages.length; i++) {
			Image image = loadImages[i];
			Group createImageGroup = createImageGroup(image);
			stack.getChildren().add(createImageGroup);
		}
		
		Node background = stack.getChildren().get(0);
		Node backgroundGang = stack.getChildren().get(1);
		Node tv = stack.getChildren().get(2);
		Node tvGhost = stack.getChildren().get(3);
		Node people = stack.getChildren().get(4);
		
		backgroundGang.setOpacity(0.8);
		backgroundGang.setVisible(false);
		tvGhost.setVisible(false);
		
		stack.setOnMouseMoved(e -> {
			
			double x = e.getX();
			double y = e.getY();
			if (!backgroundGang.isVisible()) {
				if (x > 439 && x < 765 && y > 73 && y < 247) {
					fade(tvGhost, true);
				} else {
					fade(tv, true);
					fade(tvGhost, false);
					fade(background, true);
				}
			}
		});
		
		stack.setOnMouseClicked(e ->
		
		{
			
			double x = e.getX();
			double y = e.getY();
			
			if (x > 439 && x < 765 && y > 73 && y < 247) {
				flickerIn(backgroundGang);
				fade(tv, false);
				// fade(tvGhost, false);
				tvGhost.setOpacity(0.8);
				
			}
		});
		
		
		Scene scene = new Scene(new StackPane(
				new Group(stack)));
		scene.setFill(Color.BLACK);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
		
	}
	
	private void flickerIn(Node backgroundGang) {
		KeyValue visible = new KeyValue(backgroundGang.visibleProperty(), true);
		KeyValue invisible = new KeyValue(backgroundGang.visibleProperty(), false);
		
		Timeline timeline = new Timeline();
		for (int i = 0; i < 20; i++) {
			double distance = Math.pow(2, i);
			KeyFrame invis = new KeyFrame(Duration.millis(distance + 50), invisible);
			KeyFrame vis = new KeyFrame(Duration.millis(distance + 120), visible);
			timeline.getKeyFrames().addAll(vis, invis);
		}
		timeline.play();
	}
	
	private Optional<FadeTransition> fade(Node node, boolean in) {
		
		if (node.isVisible() == in || transitions.get(node) != null) {
			return Optional.empty();
		}
		node.setVisible(true);
		FadeTransition transition = new FadeTransition(Duration.seconds(0.5), node);
		transitions.put(node, transition);
		transition.setFromValue(in ? 0 : 1);
		transition.setToValue(in ? 1 : 0);
		transition.setOnFinished(e -> {
			transitions.remove(node);
			node.setVisible(in);
		});
		transition.play();
		return Optional.of(transition);
	}
	
	
	private Group createImageGroup(Image front) {
		ImageView frontImage = new ImageView();
		frontImage.setFitWidth(1200);
		frontImage.setPreserveRatio(true);
		frontImage.setImage(front);
		return new Group(frontImage);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}