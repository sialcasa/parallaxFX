package de.saxsys.parallax;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class HelloMenu extends Application {
	
	@Override
	public void start(Stage stage) {
		// load the image
		Image image = new Image(getClass().getResource("flower.jpg").toExternalForm());
		
		// simple displays ImageView the image as is
		ImageView iv1 = new ImageView();
		iv1.setImage(image);
		Group imageGroup = new Group(iv1);
		applyMovement(imageGroup, iv1);
		
		StackPane stack = new StackPane();
		stack.getChildren().add(imageGroup);
		
		Scene scene = new Scene(stack);
		
		stage.setTitle("ImageView");
		stage.setWidth(415);
		stage.setHeight(200);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	private void applyMovement(Group iv1, ImageView image) {
		Circle circle = new Circle(5);
		circle.setFill(Color.YELLOW);
		
		
		iv1.getChildren().add(circle);
		
		iv1.setOnMouseMoved(event -> {
			adjustCirclePosition(circle, event);
			
			PerspectiveTransform perspective = new PerspectiveTransform();
			image.setEffect(perspective);
			double x = event.getX();
			double y = event.getY();
			
			double width = image.getLayoutBounds().getMaxX();
			double height = image.getLayoutBounds().getMaxY();
			
			double xDistanceFromCenter = event.getX() - width / 2;
			double yDistanceFromCenter = event.getY() - height / 2;
			
			// Oben Links
			if (isUpperLeft(xDistanceFromCenter, yDistanceFromCenter)) {
				perspective.setUlx(-xDistanceFromCenter);
				perspective.setUly(-yDistanceFromCenter);
			} else {
				perspective.setUlx(0);
				perspective.setUly(0);
			}
			
			// Oben rechts
			if (isUpperRight(xDistanceFromCenter, yDistanceFromCenter)) {
				perspective.setUrx(width - xDistanceFromCenter);
				perspective.setUry(-yDistanceFromCenter);
			} else {
				perspective.setUrx(width);
				perspective.setUry(0);
			}
			
			// Unten rechts
			if (isBottomRight(xDistanceFromCenter, yDistanceFromCenter)) {
				perspective.setLrx(width - xDistanceFromCenter);
				perspective.setLry(height - yDistanceFromCenter);
			} else {
				perspective.setLrx(width);
				perspective.setLry(height);
			}
			
			// Unten links
			if (isBottomLeft(xDistanceFromCenter, yDistanceFromCenter)) {
				perspective.setLlx(-xDistanceFromCenter);
				perspective.setLly(width - yDistanceFromCenter);
			} else {
				perspective.setLlx(0);
				perspective.setLly(height);
			}
			System.out.println(image.getLayoutBounds());
		});
		
	}
	
	private boolean isBottomLeft(double xDistanceFromCenter, double yDistanceFromCenter) {
		return xDistanceFromCenter < 0 && yDistanceFromCenter > 0;
	}
	
	private boolean isBottomRight(double xDistanceFromCenter, double yDistanceFromCenter) {
		return xDistanceFromCenter > 0 && yDistanceFromCenter > 0;
	}
	
	private boolean isUpperRight(double xDistanceFromCenter, double yDistanceFromCenter) {
		return xDistanceFromCenter > 0 && yDistanceFromCenter < 0;
	}
	
	private boolean isUpperLeft(double xDistanceFromCenter, double yDistanceFromCenter) {
		return xDistanceFromCenter < 0 && yDistanceFromCenter < 0;
	}
	
	private void adjustCirclePosition(Circle circle, MouseEvent event) {
		double x = event.getX();
		double y = event.getY();
		circle.setTranslateX(x);
		circle.setTranslateY(y);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}