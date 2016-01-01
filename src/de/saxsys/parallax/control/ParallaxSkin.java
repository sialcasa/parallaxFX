package de.saxsys.parallax.control;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SkinBase;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ParallaxSkin extends SkinBase<Parallax> {
	
	private static final double PERSPECTIVE_WEIGHT_FACTOR = 0.05;
	private static final double MOVEMENT_WEIGHT_FACTOR = 0.005;
	
	private MouseEvent lastEvent;
	
	private final Group container = new Group();
	
	protected ParallaxSkin(Parallax control) {
		super(control);
		
		getChildren().add(container);
		container.getChildren().add(control.getTargetContainer());
		
		control.targetContainerProperty().addListener(
				(ChangeListener<Parent>) (observable, oldValue, newValue) -> {
					container.getChildren().clear();
					container.getChildren().add(newValue);
				});
				
		container.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
			lastEvent = event;
			control.requestLayout();
		});
		
		InvalidationListener initialization = observable -> {
			Parent targetContainer = control.getTargetContainer();
			lastEvent = new MouseEvent(MouseEvent.MOUSE_MOVED,
					targetContainer.getLayoutBounds().getWidth() / 2,
					targetContainer.getLayoutBounds().getHeight() / 2, 0, 0,
					MouseButton.PRIMARY, 1,
					true,
					true, true, true, true, true, true, true, true, true, null);
			targetContainer.requestLayout();
		};
		
		getChildren().addListener(initialization);
		
		// // DS Around
		DropShadow dropShadow = new DropShadow(5, Color.BLACK);
		dropShadow.setSpread(0.4);
		container.setEffect(dropShadow);
	}
	
	
	
	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		// super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
		if (lastEvent != null) {
			calculateParallax(lastEvent);
		}
	}
	
	private void calculateParallax(MouseEvent event) {
		Calculator movementCalculator = new Calculator();
		Calculator childrenCalculator = new Calculator();
		updateCalculator(movementCalculator, event, MOVEMENT_WEIGHT_FACTOR * getSkinnable().getMovementWeight());
		System.out.println(movementCalculator);
		enablePerspectiveTransformation(movementCalculator);
		
		if (getSkinnable().isLightingEnabled()) {
			if (getSkinnable().getLightingOnLayer() < getSkinnable().getTargetContainer().getChildrenUnmodifiable()
					.size()) {
				Node child = getSkinnable().getTargetContainer().getChildrenUnmodifiable()
						.get(getSkinnable().getLightingOnLayer());
				child.setEffect(new MouseLighting(movementCalculator));
			}// TODO ELSE WARNING
		}
		
		ObservableList<Node> children = getSkinnable().getTargetContainer().getChildrenUnmodifiable();
		for (int i = 0; i < children.size(); i++) {
			updateCalculator(childrenCalculator, event,
					PERSPECTIVE_WEIGHT_FACTOR * getSkinnable().getPerspectiveWeight() * (i + 1));
			Node node = children.get(i);
			
			node.setTranslateX(childrenCalculator.getxDistanceFromCenter() * childrenCalculator.getWeight());
			node.setTranslateY(childrenCalculator.getyDistanceFromCenter() * childrenCalculator.getWeight());
		}
	}
	
	private void enablePerspectiveTransformation(Calculator movementCalculator) {
		// TODO - NICHT AUF DEN TARGET CONTAINER SONDERN AUF DEN WRAPPER
		Parent targetContainer = getSkinnable().getTargetContainer();
		Effect effect = targetContainer.getEffect();
		PerspectiveParallaxEffect perspective = new PerspectiveParallaxEffect(movementCalculator);
		targetContainer.setEffect(perspective);
		if (effect != null && !(effect instanceof PerspectiveParallaxEffect)) {
			perspective.setInput(effect);
		}
	}
	
	private void updateCalculator(Calculator calc, MouseEvent event, double weight) {
		calc.setWeight(weight);
		calc.setCoordinates(event.getX(), event.getY());
		calc.setSize(getNode().getLayoutBounds().getMaxX(), getNode().getLayoutBounds().getMaxY());
	}
	
	
	/*
	 * ACCESSORS
	 */
	
	
	
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
		
		@Override
		public String toString() {
			return "Calculator [weight=" + weight + ", x=" + x + ", y=" + y + ", width=" + width + ", height=" + height
					+ ", getxDistanceFromCenter()=" + getxDistanceFromCenter() + ", getyDistanceFromCenter()="
					+ getyDistanceFromCenter() + ", getRelativeXFromCenter()=" + getRelativeXFromCenter()
					+ ", getRelativeYFromCenter()=" + getRelativeYFromCenter() + ", getCenterX()=" + getCenterX()
					+ ", getCenterY()=" + getCenterY() + "]";
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
			double width = calculator.getWidth();
			double height = calculator.getHeight();
			
			double xDistanceFromCenter = calculator.getxDistanceFromCenter();
			double yDistanceFromCenter = calculator.getyDistanceFromCenter();
			
			double relativeXFromCenter = calculator.getRelativeXFromCenter();
			double relativeYFromCenter = calculator.getRelativeYFromCenter();
			
			// Oben Links
			double ulx = -xDistanceFromCenter * relativeYFromCenter;
			double uly = -yDistanceFromCenter * relativeXFromCenter;
			double urx = width - xDistanceFromCenter * relativeYFromCenter;
			double lry = height - yDistanceFromCenter * relativeXFromCenter;
			
			setUlx(ulx);
			setUly(uly);
			
			// Oben rechts
			setUrx(urx);
			setUry(uly);
			
			// Unten rechts
			setLrx(urx);
			setLry(lry);
			
			// Unten links
			setLlx(ulx);
			setLly(lry);
		}
		
	}
	
}
