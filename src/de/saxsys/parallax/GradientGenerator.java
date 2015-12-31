package de.saxsys.parallax;

import java.util.Locale;

import com.sun.javafx.binding.StringFormatter;

public class GradientGenerator {
	
	private static class Point {
		double x;
		
		public Point(double x, double y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		double y;
	}
	
	private static Point pointOfAngle(double angle, double width, double height) {
		return new Point(Math.cos(angle) * width, Math.sin(angle) * height);
	}
	
	
	static double degreesToRadians(double degree) {
		return ((degree * Math.PI) / 180);
	}
	
	public static String generateGradiant(double inputAngle, double alphaBasedOnEvent, double width, double height) {
		
		double eps = Math.pow(2, -52);
		double angle = (inputAngle % 360);
		Point startPoint = pointOfAngle(degreesToRadians(180 - angle), width, height);
		Point endPoint = pointOfAngle(degreesToRadians(360 - angle), width, height);
		
		// if you want negative values you can remove the following checks
		// but most likely it will produce undesired results
		if (startPoint.x <= 0 || Math.abs(startPoint.x) <= eps)
			startPoint.x = 0;
			
		if (startPoint.y <= 0 || Math.abs(startPoint.y) <= eps)
			startPoint.y = 0;
			
		if (endPoint.x <= 0 || Math.abs(endPoint.x) <= eps)
			endPoint.x = 0;
			
		if (endPoint.y <= 0 || Math.abs(endPoint.y) <= eps)
			endPoint.y = 0;
			
			
		String grad = "linear-gradient(from %dpx %dpx to %dpx %dpx, rgba(255,255,255,%.2f) 0%%, rgba(255,255,255,0) 80%%)";
		String gradEval = StringFormatter
				.format(Locale.ENGLISH, grad, (int) startPoint.x, (int) startPoint.y, (int) endPoint.x,
						(int) endPoint.y,
						alphaBasedOnEvent)
				.get();
		System.out.println(gradEval);
		return gradEval;
		
	}
}
