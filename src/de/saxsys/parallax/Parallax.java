package de.saxsys.parallax;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Parallax {
	
	
	private final Parent parent;
	private final Rectangle gradient = new Rectangle();;
	
	public Parallax(Parent parent) {
		
		this.parent = parent;
		
		parent.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
			processMovement(event);
		});
		
		parent.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
			processEnter();
		});
		
		parent.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
			processExit();
		});
		
	}
	
	private void processExit() {
		// parent.getTransforms().clear();
	}
	
	private void processEnter() {
		// TODO Auto-generated method stub
		
	}
	
	private void processMovement(MouseEvent event) {
		
		clear();
		
		// Kann scene sein
		double x = event.getX();
		double y = event.getY();
		
		// Kann falsche höhe haben
		double width = parent.getLayoutBounds().getWidth();
		double height = parent.getLayoutBounds().getHeight();
		
		double centerX = width / 2;
		double centerY = height / 2;
		
		double dx = x - centerX;
		double dy = y - centerY;
		
		double offsetX = x / width;
		double offsetY = y / height;
		
		double wMultiple = 700 / width;
		
		double yRotate = (offsetX - dx) * (0.07 * wMultiple);
		double xRotate = (dy - offsetY) * (0.12 * wMultiple);
		
		final Rotate rx = new Rotate(xRotate, Rotate.X_AXIS);
		rx.setPivotX(centerX);
		rx.setPivotY(centerY);
		final Rotate ry = new Rotate(yRotate, Rotate.Y_AXIS);
		ry.setPivotX(centerX);
		ry.setPivotY(centerY);
		
		parent.getTransforms().addAll(ry, rx);
		
		double arad = Math.atan2(dy, dx);
		double angle = arad * 180 / Math.PI - 90;
		
		if (angle < 0) {
			angle = angle + 360;
		}
		
		gradient.setWidth(width);
		gradient.setHeight(height);
		System.out.println(height);
		Stop stop1 = new Stop(0.0, Color.rgb(255, 255, 255, y * 0.4 / height));
		Stop stop2 = new Stop(0.8, Color.rgb(255, 255, 255, 0));
		LinearGradient linearGradient = new LinearGradient(dx, dy, dx, dy, true, CycleMethod.NO_CYCLE,
				stop1, stop2);
		gradient.setFill(linearGradient);
		StackPane par = (StackPane) parent;
		
		if (!par.getChildren().contains(gradient)) {
			// par.getChildren().add(gradient);
		}
		par.setEffect(new DropShadow(10, Color.BLACK));
		
		// parent.getTransforms().addAll(new Scale(1.07, 1.07, 1.07));
		
		// TODO SHINE
		
		// ..
		ObservableList<Node> children = parent.getChildrenUnmodifiable();
		int size = children.size();
		int revNum = size;
		for (int i = 0; i < size; i++) {
			double translateX = (offsetX * revNum) * ((i * 2.5) / wMultiple);
			double translateY = (offsetY * size) * ((i * 2.5) / wMultiple);
			children.get(i).getTransforms().add(new Translate(translateX, translateY));
			revNum--;
		}
	}
	
	private void clear() {
		parent.getTransforms().clear();
		ObservableList<Node> children = parent.getChildrenUnmodifiable();
		int size = children.size();
		for (int i = 0; i < size; i++) {
			children.get(i).getTransforms().clear();
		}
	}
	
	
}
