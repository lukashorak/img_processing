package katka_fx;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import katka_fx.ui.EventHandlerLoadImage;

public class MainController extends HBox {

	public File file;
	public Image img;

	@FXML
	public Label filePath;

	@FXML
	private Label resultLabel;

	@FXML
	private Label toleranceLabel;

	@FXML
	private Button loadButton;

	@FXML
	private Button saveButton;

	@FXML
	private Button buttonHue;

	@FXML
	public ImageView imageView;

	@FXML
	private Slider tolerance;

	@FXML
	private Slider toleranceSaturation;

	@FXML
	private ColorPicker colorPickerFill;

	@FXML
	private ColorPicker colorPickerTarget;

	@FXML
	private ChoiceBox<String> methodChoice;

	public MainController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"ui1.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		loadButton.setOnAction(new EventHandlerLoadImage(this));
		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Image img = imageView.getImage();
				File outputfile = new File("c:/tmp/saved.jpg");
				// ImageIO.write(img, "png", outputfile);
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(img, null), "jpg",
							outputfile);
				} catch (Exception s) {
				}

			}
		});

		tolerance.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				changeTolerance();
			}
		});

		colorPickerFill.setValue(new Color(1.0, 0.1, 0.1, 1.0));
		colorPickerTarget.setValue(new Color(0.0, 1.0, 0.1, 1.0));
		colorPickerTarget.getCustomColors().add(
				new Color(149.0 / 256, 159.0 / 256.0, 55.0 / 256.0, 1.0));
		colorPickerTarget.getCustomColors().add(
				new Color(65.0 / 256, 87.0 / 256.0, 22.0 / 256.0, 1.0));

		ObservableList<String> methodList = FXCollections.observableArrayList(
				"Lower than Treshold", "Higher than Treshold",
				"Hue within Treshold");
		methodChoice.setItems(methodList);
		methodChoice.setValue("Hue within Treshold");

	}

	@FXML
	protected void openPicture() {
		filePath.setText("ABC");
		System.out.println("The button was clicked!");

	}

	@FXML
	protected void doSomething() {
		filePath.setText("ABC");
		System.out.println("The button was clicked!");

	}

	protected void changeTolerance() {
		this.toleranceLabel.setText(String.format("%.0f%%",
				this.tolerance.getValue() * 100D));
	}

	@FXML
	protected void processPicture() {

		if (this.img != null) {
			this.processImage(this.img, this.tolerance.getValue());
		} else {
			this.resultLabel.setText("No image selected!");
		}
	}

	@FXML
	private void handleButtonHueAction(ActionEvent event) {
		Image image = this.img;
		PixelReader pixelReader = image.getPixelReader();

		// Create WritableImage
		WritableImage wImage = new WritableImage((int) image.getWidth(),
				(int) image.getHeight());
		PixelWriter pixelWriter = wImage.getPixelWriter();

		for (int readY = 0; readY < image.getHeight(); readY++) {
			for (int readX = 0; readX < image.getWidth(); readX++) {
				Color pixelColor = pixelReader.getColor(readX, readY);

				Double h = pixelColor.getHue() / 360.0;
				
				if (h < 0.11) {
					h = 0.0;
				}
				if (h > 0.25) {
					h = 1.0;
				}
				
				Double s = pixelColor.getSaturation();

				pixelColor = new Color(h, h, h, 1.0);

				pixelWriter.setColor(readX, readY, pixelColor);

			}
		}

		imageView.setImage(wImage);
	}

	@FXML
	private void handleButtonSaturationAction(ActionEvent event) {
		Image image = this.img;
		PixelReader pixelReader = image.getPixelReader();

		// Create WritableImage
		WritableImage wImage = new WritableImage((int) image.getWidth(),
				(int) image.getHeight());
		PixelWriter pixelWriter = wImage.getPixelWriter();

		for (int readY = 0; readY < image.getHeight(); readY++) {
			for (int readX = 0; readX < image.getWidth(); readX++) {
				Color pixelColor = pixelReader.getColor(readX, readY);

				Double h = pixelColor.getHue() / 360.0;
				Double s = pixelColor.getSaturation();

				pixelColor = new Color(s, s, s, 1.0);

				pixelWriter.setColor(readX, readY, pixelColor);

			}
		}

		imageView.setImage(wImage);
	}

	@FXML
	private void handleButtonBrightnessAction(ActionEvent event) {
		Image image = this.img;
		PixelReader pixelReader = image.getPixelReader();

		// Create WritableImage
		WritableImage wImage = new WritableImage((int) image.getWidth(),
				(int) image.getHeight());
		PixelWriter pixelWriter = wImage.getPixelWriter();

		for (int readY = 0; readY < image.getHeight(); readY++) {
			for (int readX = 0; readX < image.getWidth(); readX++) {
				Color pixelColor = pixelReader.getColor(readX, readY);

				Double h = pixelColor.getHue() / 360.0;
				Double s = pixelColor.getSaturation();
				Double b = pixelColor.getBrightness();

				pixelColor = new Color(b, b, b, 1.0);

				pixelWriter.setColor(readX, readY, pixelColor);

			}
		}

		imageView.setImage(wImage);
	}

	@FXML
	private void handleButtonExpAction(ActionEvent event) {
		Image image = this.img;
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

		imageView.setImage(wImage);
	}

	private void processImage(Image image, Double tolerance) {
		PixelReader pixelReader = image.getPixelReader();

		Long total = (long) (image.getHeight() * (long) image.getWidth());
		Long effected = 0L;
		// Create WritableImage
		WritableImage wImage = new WritableImage((int) image.getWidth(),
				(int) image.getHeight());
		PixelWriter pixelWriter = wImage.getPixelWriter();

		for (int readY = 0; readY < image.getHeight(); readY++) {
			for (int readX = 0; readX < image.getWidth(); readX++) {
				Color pixelColor = pixelReader.getColor(readX, readY);

				// Now write a brighter color to the PixelWriter.
				if (this.checkTreshold(pixelColor)) {
					pixelColor = colorPickerFill.getValue();
					effected++;
				}
				pixelWriter.setColor(readX, readY, pixelColor);

			}
		}
		DecimalFormat df = new DecimalFormat("#.##");

		System.out.println("Total effected : " + effected + " / " + total
				+ " = " + df.format(((double) effected / (double) total)));

		resultLabel.setText(df
				.format((100.0 * (double) effected / (double) total)) + "%");
		imageView.setImage(wImage);
	}

	public void saveImageToFile() {

	}

	public boolean checkTreshold(Color color) {
		Double tolerance = this.tolerance.getValue();
		Double targetHue = this.colorPickerTarget.getValue().getHue();

		String choiceValue = (String) this.methodChoice.getValue();
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
