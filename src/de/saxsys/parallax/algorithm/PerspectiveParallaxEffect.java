package de.saxsys.parallax.algorithm;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.paint.Color;

public class PerspectiveParallaxEffect extends PerspectiveTransform {
	
	public PerspectiveParallaxEffect(Calculator calculator) {
		
		DropShadow dropShadow = new DropShadow(10, Color.GRAY);
		dropShadow.setSpread(0.4);
		
		setInput(dropShadow);
		
		double width = calculator.getWidth();
		double height = calculator.getHeight();
		
		double xDistanceFromCenter = calculator.getxDistanceFromCenter();
		double yDistanceFromCenter = calculator.getyDistanceFromCenter();
		
		double relativeXFromCenter = calculator.getRelativeXFromCenter();
		double relativeYFromCenter = calculator.getRelativeYFromCenter();
		
		// Oben Links
		setUlx(-xDistanceFromCenter * relativeYFromCenter);
		setUly(-yDistanceFromCenter * relativeXFromCenter);
		
		// Oben rechts
		setUrx((width - xDistanceFromCenter * relativeYFromCenter));
		setUry(-yDistanceFromCenter * relativeXFromCenter);
		
		// Unten rechts
		setLrx((width - xDistanceFromCenter * relativeYFromCenter));
		setLry(height - yDistanceFromCenter * relativeXFromCenter);
		
		// Unten links
		setLlx(-xDistanceFromCenter * relativeYFromCenter);
		setLly(height - yDistanceFromCenter * relativeXFromCenter);
	}
	
}
