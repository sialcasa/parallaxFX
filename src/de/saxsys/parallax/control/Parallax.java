package de.saxsys.parallax.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class Parallax extends Control {
	
	private final BooleanProperty lightingEnabled = new SimpleBooleanProperty(false);
	private final IntegerProperty lightingOnLayer = new SimpleIntegerProperty(0);
	
	private final DoubleProperty perspectiveWeight = new SimpleDoubleProperty(1);
	private final DoubleProperty movementWeight = new SimpleDoubleProperty(1);
	
	ObjectProperty<Parent> targetContainer = new SimpleObjectProperty<>();
	
	public Parallax() {
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return new ParallaxSkin(this);
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
	
	public final IntegerProperty lightingOnLayerProperty() {
		return this.lightingOnLayer;
	}
	
	public final int getLightingOnLayer() {
		return this.lightingOnLayerProperty().get();
	}
	
	public final void setLightingOnLayer(final int lightingOnLayer) {
		this.lightingOnLayerProperty().set(lightingOnLayer);
	}
	
	public final DoubleProperty perspectiveWeightProperty() {
		return this.perspectiveWeight;
	}
	
	public final double getPerspectiveWeight() {
		return this.perspectiveWeightProperty().get();
	}
	
	public final void setPerspectiveWeight(final double perspectiveWeight) {
		this.perspectiveWeightProperty().set(perspectiveWeight);
	}
	
	public final DoubleProperty movementWeightProperty() {
		return this.movementWeight;
	}
	
	public final double getMovementWeight() {
		return this.movementWeightProperty().get();
	}
	
	public final void setMovementWeight(final double movementWeight) {
		this.movementWeightProperty().set(movementWeight);
	}
	
	
	public final ObjectProperty<Parent> targetContainerProperty() {
		return this.targetContainer;
	}
	
	
	
	public final javafx.scene.Parent getTargetContainer() {
		return this.targetContainerProperty().get();
	}
	
	
	
	public final void setTargetContainer(final javafx.scene.Parent target) {
		this.targetContainerProperty().set(target);
	}
	
	
	
	
	
}
