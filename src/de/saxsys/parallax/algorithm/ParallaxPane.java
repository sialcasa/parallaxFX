package de.saxsys.parallax.algorithm;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ParallaxPane extends StackPane {
	
	private static final double PERSPECTIVE_WEIGHT = 0.05;
	private static final double MOVEMENT_WEIGHT = 0.005;
	
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
			
			Calculator calculator = new Calculator();
			Calculator childCalc = new Calculator();
			
			@Override
			public void handle(MouseEvent event) {
				initCalculator(calculator, event, MOVEMENT_WEIGHT);
				
				processPerspective(calculator);
				processBackgroundSpot(calculator);
				
				ObservableList<Node> childrenUnmodifiable = getChildrenUnmodifiable();
				
				for (int i = 0; i < childrenUnmodifiable.size(); i++) {
					Node child = childrenUnmodifiable.get(i);
					final int index = i;
					initCalculator(childCalc, event, PERSPECTIVE_WEIGHT * (index + 1));
					processMovement(child, childCalc);
				}
				
			}
			
			private void initCalculator(Calculator calc, MouseEvent event, double weight) {
				calc.setX(event.getX());
				calc.setY(event.getY());
				calc.setWeight(weight);
				calc.setWidth(getLayoutBounds().getMaxX());
				calc.setHeight(getLayoutBounds().getMaxY());
			}
		});
		
		
	}
	
	
	
	private void processBackgroundSpot(Calculator calculator) {
		Light.Spot light = new Light.Spot();
		light.setColor(Color.WHITE);
		light.setX(calculator.getX());
		light.setY(calculator.getY());
		light.setZ(500);
		light.setPointsAtX(0);
		light.setPointsAtY(0);
		light.setPointsAtZ(-10000);
		light.setSpecularExponent(0);
		
		Lighting lighting = new Lighting();
		lighting.setDiffuseConstant(2);
		lighting.setLight(light);
		lighting.setSurfaceScale(1);
		Node child = getChildrenUnmodifiable().get(0);
		child.setEffect(lighting);
	}
	
	
	
	private void processMovement(Node node, Calculator processor) {
		node.setTranslateX(processor.getxDistanceFromCenter() * processor.getWeight());
		node.setTranslateY(processor.getyDistanceFromCenter() * processor.getWeight());
	}
	
	
	
	private void processPerspective(Calculator calculator) {
		PerspectiveTransform perspective = new PerspectiveTransform();
		DropShadow dropShadow = new DropShadow(10, Color.GRAY);
		dropShadow.setSpread(0.4);
		perspective.setInput(dropShadow);
		
		setEffect(perspective);
		
		double width = calculator.getWidth();
		double height = calculator.getHeight();
		
		double xDistanceFromCenter = calculator.getxDistanceFromCenter();
		double yDistanceFromCenter = calculator.getyDistanceFromCenter();
		
		double relativeXFromCenter = calculator.getRelativeXFromCenter();
		double relativeYFromCenter = calculator.getRelativeYFromCenter();
		
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
}
