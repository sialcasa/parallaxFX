package de.saxsys.parallax;


import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
		applyParallax(stack);
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
	
	
	
	/**
	 * Perspective
	 * 
	 * @param order
	 */
	private void applyParallax(Parent node) {
		node.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
			processPerspective(node, event);
			processBackgroundSpot(node, event);
		});
		
		ObservableList<Node> childrenUnmodifiable = node.getChildrenUnmodifiable();
		
		for (int i = 0; i < childrenUnmodifiable.size(); i++) {
			Node child = childrenUnmodifiable.get(i);
			final int index = i;
			node.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
				processMovement(child, event, index + 1);
			});
		}
		
	}
	
	
	
	private void processBackgroundSpot(Parent node, MouseEvent event) {
		Light.Spot light = new Light.Spot();
		light.setColor(Color.WHITE);
		light.setX(event.getX());
		light.setY(event.getY());
		light.setZ(500);
		light.setPointsAtX(0);
		light.setPointsAtY(0);
		light.setPointsAtZ(-10000);
		light.setSpecularExponent(0);
		
		Lighting lighting = new Lighting();
		lighting.setDiffuseConstant(2);
		lighting.setLight(light);
		lighting.setSurfaceScale(1);
		Node child = node.getChildrenUnmodifiable().get(0);
		child.setEffect(lighting);
	}
	
	
	
	private void processMovement(Node node, MouseEvent event, int order) {
		double weight = 0.1 * order;
		
		double x = event.getX();
		double y = event.getY();
		
		double width = node.getLayoutBounds().getMaxX();
		double height = node.getLayoutBounds().getMaxY();
		
		double centerX = width / 2;
		double centerY = height / 2;
		
		double xDistanceFromCenter = (x - centerX) * weight;
		double yDistanceFromCenter = (y - centerY) * weight;
		
		node.setTranslateX(xDistanceFromCenter * weight);
		node.setTranslateY(yDistanceFromCenter * weight);
	}
	
	
	
	private void processPerspective(Node node, MouseEvent event) {
		PerspectiveTransform perspective = new PerspectiveTransform();
		DropShadow dropShadow = new DropShadow(10, Color.GRAY);
		dropShadow.setSpread(0.4);
		perspective.setInput(dropShadow);
		
		node.setEffect(perspective);
		
		double weight = 0.005;
		
		double x = event.getX();
		double y = event.getY();
		
		double width = node.getLayoutBounds().getMaxX();
		double height = node.getLayoutBounds().getMaxY();
		
		double centerX = width / 2;
		double centerY = height / 2;
		
		double xDistanceFromCenter = (x - centerX) * weight;
		double yDistanceFromCenter = (y - centerY) * weight;
		
		double relativeXFromCenter = xDistanceFromCenter < 0 ? 1 - (x / centerX % 1) : (x / centerX % 1);
		double relativeYFromCenter = yDistanceFromCenter < 0 ? 1 - (y / centerY % 1) : (y / centerY % 1);
		
		// Oben Links
		perspective.setUlx(-xDistanceFromCenter * relativeYFromCenter);
		perspective.setUly(-yDistanceFromCenter * relativeXFromCenter); // - height / 2 * zwischen
		
		// Oben rechts
		perspective.setUrx((width - xDistanceFromCenter * relativeYFromCenter));
		perspective.setUry(-yDistanceFromCenter * relativeXFromCenter);
		
		// Unten rechts
		perspective.setLrx((width - xDistanceFromCenter * relativeYFromCenter));
		perspective.setLry(height - yDistanceFromCenter * relativeXFromCenter);
		
		// Unten links
		perspective.setLlx(-xDistanceFromCenter * relativeYFromCenter);
		perspective.setLly(height - yDistanceFromCenter * relativeXFromCenter);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}