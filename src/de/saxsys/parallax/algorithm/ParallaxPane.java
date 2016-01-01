package de.saxsys.parallax.algorithm;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ParallaxPane extends Pane {
	
	private static final double PERSPECTIVE_WEIGHT_FACTOR = 0.05;
	private static final double MOVEMENT_WEIGHT_FACTOR = 0.005;
	
	private final BooleanProperty lightingEnabled = new SimpleBooleanProperty(false);
	private final IntegerProperty lightingOnLayer = new SimpleIntegerProperty(0);
	
	private final DoubleProperty perspectiveWeight = new SimpleDoubleProperty(1);
	private final DoubleProperty movementWeight = new SimpleDoubleProperty(1);
	
	private MouseEvent lastEvent;
	
	public ParallaxPane(Node... children) {
		this();
		getChildren().addAll(children);
	}
	
	public ParallaxPane() {
		super();
		
		addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
			lastEvent = event;
			requestLayout();
		});
		
		InvalidationListener initialization = observable -> {
			lastEvent = new MouseEvent(MouseEvent.MOUSE_MOVED,
					getWidth() / 2, getHeight() / 2, 0, 0,
					MouseButton.PRIMARY, 1,
					true,
					true, true, true, true, true, true, true, true, true, null);
			requestLayout();
		};
		
		getChildren().addListener(initialization);
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
		updateCalculator(movementCalculator, event, MOVEMENT_WEIGHT_FACTOR * movementWeight.get());
		
		enablePerspectiveTransformation(movementCalculator);
		
		if (isLightingEnabled()) {
			if (lightingOnLayer.get() < getChildren().size()) {
				Node child = getChildren().get(lightingOnLayer.get());
				child.setEffect(new MouseLighting(movementCalculator));
			}// TODO ELSE WARNING
		}
		
		ObservableList<Node> children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			updateCalculator(childrenCalculator, event, PERSPECTIVE_WEIGHT_FACTOR * perspectiveWeight.get() * (i + 1));
			Node node = children.get(i);
			
			node.setTranslateX(childrenCalculator.getxDistanceFromCenter() * childrenCalculator.getWeight());
			node.setTranslateY(childrenCalculator.getyDistanceFromCenter() * childrenCalculator.getWeight());
		}
	}
	
	private void enablePerspectiveTransformation(Calculator movementCalculator) {
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
	
	
	/*
	 * ACCESSORS
	 */
	
	public final BooleanProperty lightingEnabledProperty() {
		return this.lightingEnabled;
	}
	
	
	public final boolean isLightingEnabled() {
		return this.lightingEnabledProperty().get();
	}
	
	
	public final void setLightingEnabled(final boolean lightingEnabled) {
		this.lightingEnabledProperty().set(lightingEnabled);
	}
	
	
	
	/*
	 * HELPER
	 */
	
	class Calculator {
		
		private double weight;
		private double x;
		private double y;
		private double width;
		private double height;
		
		public double getWeight() {
			return weight;
		}
		
		public double getX() {
			return x;
		}
		
		public double getY() {
			return y;
		}
		
		public double getWidth() {
			return width;
		}
		
		public double getHeight() {
			return height;
		}
		
		public void setWeight(double weight) {
			this.weight = weight;
		}
		
		public void setX(double x) {
			this.x = x;
		}
		
		public void setY(double y) {
			this.y = y;
		}
		
		public void setCoordinates(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public void setWidth(double width) {
			this.width = width;
		}
		
		public void setHeight(double height) {
			this.height = height;
		}
		
		public void setSize(double width, double height) {
			this.width = width;
			this.height = height;
		}
		
		
		/*
		 * Processing
		 */
		
		public double getxDistanceFromCenter() {
			return (x - getCenterX()) * weight;
		}
		
		public double getyDistanceFromCenter() {
			return (y - getCenterY()) * weight;
		}
		
		public double getRelativeXFromCenter() {
			return getxDistanceFromCenter() < 0 ? 1 - (x / getCenterX() % 1) : (x / getCenterX() % 1);
		}
		
		public double getRelativeYFromCenter() {
			return getyDistanceFromCenter() < 0 ? 1 - (y / getCenterY() % 1) : (y / getCenterY() % 1);
		}
		
		public double getCenterX() {
			return width / 2;
		}
		
		public double getCenterY() {
			return height / 2;
		}
	}
	
	class MouseLighting extends Lighting {
		public MouseLighting(Calculator calculator) {
			Light.Spot light = new Light.Spot();
			light.setColor(Color.WHITE);
			light.setX(calculator.getX());
			light.setY(calculator.getY());
			light.setZ(500);
			light.setPointsAtX(0);
			light.setPointsAtY(0);
			light.setPointsAtZ(-10000);
			light.setSpecularExponent(0);
			setDiffuseConstant(2);
			setLight(light);
			setSurfaceScale(1);
		}
	}
	
	
	class PerspectiveParallaxEffect extends PerspectiveTransform {
		
		public PerspectiveParallaxEffect(Calculator calculator) {
			
			DropShadow dropShadow = new DropShadow(10, Color.BLACK);
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
	
}
