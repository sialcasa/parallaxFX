package de.saxsys.parallax.algorithm;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ParallaxPane extends Pane {
	
	// TODO SHape das sich bewegt clip
	// Todo config
	private static final double PERSPECTIVE_WEIGHT = 0.05;
	private static final double MOVEMENT_WEIGHT = 0.005;
	
	BooleanProperty lightingEnabled = new SimpleBooleanProperty(false);
	private MouseEvent lastEvent;
	
	public ParallaxPane() {
		addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
			lastEvent = event;
			requestLayout();
		});
	}
	
	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		if (lastEvent != null) {
			calculateParallax(lastEvent);
		}
	}
	
	private void calculateParallax(MouseEvent event) {
		Calculator movementCalculator = new Calculator();
		Calculator childrenCalculator = new Calculator();
		updateCalculator(movementCalculator, event, MOVEMENT_WEIGHT);
		
		enableLightingEffect(movementCalculator);
		
		if (isLightingEnabled()) {
			// Todo configure
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
	
	private void enableLightingEffect(Calculator movementCalculator) {
		// TODO ist das korrekt?
		Effect effect = getEffect();
		PerspectiveParallaxEffect perspective = new PerspectiveParallaxEffect(movementCalculator);
		setEffect(perspective);
		if (effect != null && !(effect instanceof PerspectiveParallaxEffect)) {
			perspective.setInput(effect);
		}
	}
	
	private void updateCalculator(Calculator calc, MouseEvent event, double weight) {
		calc.setWeight(weight);
		calc.setCoordinates(event.getX(), event.getY());
		calc.setSize(getLayoutBounds().getMaxX(), getLayoutBounds().getMaxY());
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
