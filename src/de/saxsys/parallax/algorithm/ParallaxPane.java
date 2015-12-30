package de.saxsys.parallax.algorithm;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class ParallaxPane extends StackPane {
	
	// Todo config
	private static final double PERSPECTIVE_WEIGHT = 0.05;
	private static final double MOVEMENT_WEIGHT = 0.005;
	
	BooleanProperty lightingEnabled = new SimpleBooleanProperty(false);
	
	public ParallaxPane() {
		applyParallax();
	}
	
	/**
	 * Perspective
	 * 
	 * @param order
	 */
	private void applyParallax() {
		addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
			
			Calculator movementCalculator = new Calculator();
			Calculator childrenCalculator = new Calculator();
			
			@Override
			public void handle(MouseEvent event) {
				updateCalculator(movementCalculator, event, MOVEMENT_WEIGHT);
				
				setEffect(new PerspectiveParallaxEffect(movementCalculator));
				
				if (isLightingEnabled()) {
					Node child = getChildren().get(0);
					child.setEffect(new MouseLighting(movementCalculator));
				}
				
				ObservableList<Node> children = getChildren();
				for (int i = 0; i < children.size(); i++) {
					updateCalculator(childrenCalculator, event, PERSPECTIVE_WEIGHT * (i + 1));
					Node node = children.get(i);
					node.setTranslateX(childrenCalculator.getxDistanceFromCenter() * childrenCalculator.getWeight());
					node.setTranslateY(childrenCalculator.getyDistanceFromCenter() * childrenCalculator.getWeight());
				}
			}
			
			private void updateCalculator(Calculator calc, MouseEvent event, double weight) {
				calc.setWeight(weight);
				calc.setCoordinates(event.getX(), event.getY());
				calc.setSize(getLayoutBounds().getMaxX(), getLayoutBounds().getMaxY());
			}
		});
	}
	
	public final BooleanProperty lightingEnabledProperty() {
		return this.lightingEnabled;
	}
	
	
	public final boolean isLightingEnabled() {
		return this.lightingEnabledProperty().get();
	}
	
	
	public final void setLightingEnabled(final boolean lightingEnabled) {
		this.lightingEnabledProperty().set(lightingEnabled);
	}
	
}
