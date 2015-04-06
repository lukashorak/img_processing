package katka_fx;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageProcessor {

	public class ImgProcessorResult {
		public Image image;
		public Double result;

		public ImgProcessorResult() {

		}

		public ImgProcessorResult(Image image, Double result) {
			this.image = image;
			this.result = result;
		}
	}

	public ImageProcessor() {

	}

	public Image processNone(Image img) {

		return img;
	}

	public Image processBrightness(Image image) {
		PixelReader pixelReader = image.getPixelReader();

		// Create WritableImage
		WritableImage wImage = new WritableImage((int) image.getWidth(),
				(int) image.getHeight());
		PixelWriter pixelWriter = wImage.getPixelWriter();

		for (int readY = 0; readY < image.getHeight(); readY++) {
			for (int readX = 0; readX < image.getWidth(); readX++) {
				Color pixelColor = pixelReader.getColor(readX, readY);

				Double b = pixelColor.getBrightness();
				pixelColor = new Color(b, b, b, 1.0);

				pixelWriter.setColor(readX, readY, pixelColor);

			}
		}

		return wImage;
	}

	public Image processHue(Image image) {
		PixelReader pixelReader = image.getPixelReader();

		// Create WritableImage
		WritableImage wImage = new WritableImage((int) image.getWidth(),
				(int) image.getHeight());
		PixelWriter pixelWriter = wImage.getPixelWriter();

		for (int readY = 0; readY < image.getHeight(); readY++) {
			for (int readX = 0; readX < image.getWidth(); readX++) {
				Color pixelColor = pixelReader.getColor(readX, readY);

				Double h = pixelColor.getHue() / 360.0;

				// if (h < 0.11) {
				// h = 0.0;
				// }
				// if (h > 0.25) {
				// h = 1.0;
				// }

				pixelColor = new Color(h, h, h, 1.0);

				pixelWriter.setColor(readX, readY, pixelColor);

			}
		}

		return wImage;
	}

	public Image processSaturation(Image image) {
		PixelReader pixelReader = image.getPixelReader();

		// Create WritableImage
		WritableImage wImage = new WritableImage((int) image.getWidth(),
				(int) image.getHeight());
		PixelWriter pixelWriter = wImage.getPixelWriter();

		for (int readY = 0; readY < image.getHeight(); readY++) {
			for (int readX = 0; readX < image.getWidth(); readX++) {
				Color pixelColor = pixelReader.getColor(readX, readY);
				Double s = pixelColor.getSaturation();
				pixelColor = new Color(s, s, s, 1.0);
				pixelWriter.setColor(readX, readY, pixelColor);

			}
		}

		return wImage;
	}

	public Image processExp(Image image) {
		PixelReader pixelReader = image.getPixelReader();

		// Create WritableImage
		WritableImage wImage = new WritableImage((int) image.getWidth(),
				(int) image.getHeight());
		PixelWriter pixelWriter = wImage.getPixelWriter();

		for (int readY = 0; readY < image.getHeight(); readY++) {
			for (int readX = 0; readX < image.getWidth(); readX++) {
				Color pixelColor = pixelReader.getColor(readX, readY);

				Double h = 0.0;
				if (30 < pixelColor.getHue() && pixelColor.getHue() < 90) {
					h = pixelColor.getHue() / 360.0;
				}

				Double s = 0.0;
				if (0.7 < pixelColor.getSaturation()) {
					s = pixelColor.getSaturation();
				}

				Double b = pixelColor.getBrightness();
				if (b < 0.2) {
					b = 0.0;
				}

				Double v = h * s;
				if (v > 0)
					v = 1.0;

				pixelColor = new Color(v, v, v, 1.0);

				pixelWriter.setColor(readX, readY, pixelColor);

			}
		}

		return wImage;
	}

	public ImgProcessorResult processMagic(Image image, Double targetHue,
			Double tolerance) {
		PixelReader pixelReader = image.getPixelReader();

		// Create WritableImage
		WritableImage wImage = new WritableImage((int) image.getWidth(),
				(int) image.getHeight());
		PixelWriter pixelWriter = wImage.getPixelWriter();

		Long total = (long) (image.getHeight() * (long) image.getWidth());
		Long effected = 0L;

		for (int readY = 0; readY < image.getHeight(); readY++) {
			for (int readX = 0; readX < image.getWidth(); readX++) {
				Color pixelColor = pixelReader.getColor(readX, readY);
				int action = 0;

				double targetSaturation = 0.30;
				// double targetHue = 67.0;
				// double tolerance = 25;

				double hue = pixelColor.getHue();
				double saturation = pixelColor.getSaturation();

				boolean is = (Math.abs(targetHue - hue)) < (tolerance * 360.0)
						&& (saturation > targetSaturation);

				if (is) {
					action = 1;
				}

				if (pixelColor.getBrightness() < 0.2) {
					action = 2;
				}
				if (pixelColor.getSaturation() > 0.7
						&& pixelColor.getBrightness() < 0.3) {
					action = 3;
				}

				switch (action) {
				case 1:
					pixelColor = Color.RED;
					effected++;
					break;
				case 2:
					pixelColor = Color.VIOLET;
					break;
				case 3:
					pixelColor = Color.BLUE;
					break;
				}

				// pixelColor = new Color(v, v, v, 1.0);

				pixelWriter.setColor(readX, readY, pixelColor);

			}
		}

		System.out.println(String.format("Result (%.3f %.3f): %d / %d = %.4f",targetHue, tolerance,
				effected, total, (double) effected / (double) total));

		ImgProcessorResult result = new ImgProcessorResult(wImage,
				((double) effected / (double) total));
		return result;
	}
	
	public boolean checkTreshold(Color color, String choiceValue, Double tolerance, Double targetHue) {
		
		switch (choiceValue) {
		case "Lower than Treshold":
			return this.checkTresholdSmaller(color, tolerance);
		case "Higher than Treshold":
			return this.checkTresholdBigger(color, tolerance);
		case "Hue within Treshold":
			return this.checkTresholdHSV(color, tolerance, targetHue);
		default:
			return false;
		}
	}

	public boolean checkTresholdSmaller(Color color, Double tolerance) {
		return (color.getGreen() < tolerance);
	}

	public boolean checkTresholdBigger(Color color, Double tolerance) {
		return (color.getGreen() > tolerance);
	}

	public boolean checkTresholdHSV(Color color, Double tolerance,
			Double targetHue) {
		double targetSaturation = 0.50;

		double hue = color.getHue();
		double saturation = color.getSaturation();

		boolean is = (Math.abs(targetHue - hue)) < (tolerance * 360.0)
				&& (saturation > targetSaturation);

		return is;
	}

}
