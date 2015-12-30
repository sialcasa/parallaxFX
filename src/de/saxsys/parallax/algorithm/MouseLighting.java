package de.saxsys.parallax.algorithm;

import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;

public class MouseLighting extends Lighting {
	
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
