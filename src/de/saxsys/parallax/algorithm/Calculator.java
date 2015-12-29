package de.saxsys.parallax.algorithm;

public class Calculator {
	
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
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public void setHeight(double height) {
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
