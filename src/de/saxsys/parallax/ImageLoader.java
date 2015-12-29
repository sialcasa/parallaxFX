package de.saxsys.parallax;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.scene.image.Image;

public class ImageLoader {
	
	private Image[] loadImages;
	
	public Image[] loadImages(String path) {
		try {
			
			URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
			String pathname = new File(url.toURI()).getParentFile().getAbsolutePath() + path;
			File directory = new File(pathname);
			
			if (!directory.exists()) {
				throw new RuntimeException("No folder at:" + pathname);
			}
			
			
			File[] listFiles = directory.listFiles();
			loadImages = new Image[listFiles.length];
			
			for (int i = 0; i < listFiles.length; i++) {
				final int index = i;
				try {
					loadImages[index] = new Image(listFiles[index].toURL().toExternalForm());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return loadImages;
	}
	
}
