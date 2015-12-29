package de.saxsys.parallax.algorithm;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ParallaxImpl {
	/**
	 * Perspective
	 * 
	 * @param order
	 */
	public void applyParallax(Parent node) {
		node.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
			
			Calculator calculator = new Calculator();
			
			@Override
			public void handle(MouseEvent event) {
				calculator.setX(event.getX());
				calculator.setY(event.getX());
				calculator.setWeight(0.005);
				calculator.setWidth(node.getLayoutBounds().getMaxX());
				calculator.setHeight(node.getLayoutBounds().getMaxY());
				
				processPerspective(node, calculator);
				processBackgroundSpot(node, calculator);
			}
		});
		
		ObservableList<Node> childrenUnmodifiable = node.getChildrenUnmodifiable();
		
		for (int i = 0; i < childrenUnmodifiable.size(); i++) {
			Node child = childrenUnmodifiable.get(i);
			final int index = i;
			node.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
				
				Calculator calculator = new Calculator();
				
				@Override
				public void handle(MouseEvent event) {
					calculator.setX(event.getX());
					calculator.setY(event.getY());
					calculator.setWeight(0.1 * (index + 1));
					calculator.setWidth(node.getLayoutBounds().getMaxX());
					calculator.setHeight(node.getLayoutBounds().getMaxY());
					processMovement(child, calculator);
				}
				
			});
		}
	}
	
	
	
	private void processBackgroundSpot(Parent node, Calculator calculator) {
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
		Node child = node.getChildrenUnmodifiable().get(0);
		child.setEffect(lighting);
	}
	
	
	
	private void processMovement(Node node, Calculator processor) {
		node.setTranslateX(processor.getxDistanceFromCenter() * processor.getWeight());
		node.setTranslateY(processor.getyDistanceFromCenter() * processor.getWeight());
	}
	
	
	
	private void processPerspective(Node node, Calculator calculator) {
		PerspectiveTransform perspective = new PerspectiveTransform();
		DropShadow dropShadow = new DropShadow(10, Color.GRAY);
		dropShadow.setSpread(0.4);
		perspective.setInput(dropShadow);
		
		node.setEffect(perspective);
		
		
		
		
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
