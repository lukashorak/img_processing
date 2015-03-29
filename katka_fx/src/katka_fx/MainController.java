package katka_fx;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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

public class MainController extends HBox {

	public File file;
	public Image img;

	@FXML
	private Label filePath;

	@FXML
	private Label resultLabel;

	@FXML
	private Button loadButton;

	@FXML
	private ImageView imageView;

	@FXML
	private Slider tolerance;

	@FXML
	private ColorPicker colorPicker;

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

		loadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();

				// Set extension filter
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
						"JPG Photos", "*.jpg");
				fileChooser.getExtensionFilters().add(extFilter);

				// Show open file dialog
				File dialogResultFile = fileChooser.showOpenDialog(null);

				if (dialogResultFile != null && dialogResultFile.exists()) {
					file = dialogResultFile;

					filePath.setText(file.getPath());
					img = new Image(file.toURI().toString());
					imageView.setImage(img);
				}

			}
		});

		colorPicker.setValue(new Color(1.0, 0.1, 0.1, 1.0));

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

	@FXML
	protected void changeTolerance() {
		System.out.println("Change tolerance to:" + this.tolerance.getValue());
		// this.processImage(this.img, this.tolerance.getValue());
	}

	@FXML
	protected void processPicture() {
		this.processImage(this.img, this.tolerance.getValue());
	}

	private void processImage(Image image, double value) {
		PixelReader pixelReader = image.getPixelReader();

		Long total = (long) (image.getHeight() * (long) image.getWidth());
		Long effected = 0L;
		// Create WritableImage
		WritableImage wImage = new WritableImage((int) image.getWidth(),
				(int) image.getHeight());
		PixelWriter pixelWriter = wImage.getPixelWriter();

		for (int readY = 0; readY < image.getHeight(); readY++) {
			for (int readX = 0; readX < image.getWidth(); readX++) {
				Color color = pixelReader.getColor(readX, readY);
				// System.out.println("\nPixel color at coordinates (" + readX
				// + "," + readY + ") " + color.toString());
				// System.out.println("R = " + color.getRed());
				// System.out.println("G = " + color.getGreen());
				// System.out.println("B = " + color.getBlue());
				// System.out.println("Opacity = " + color.getOpacity());
				// System.out.println("Saturation = " + color.getSaturation());

				// Now write a brighter color to the PixelWriter.
				if (color.getGreen() < this.tolerance.getValue()) {
					color = colorPicker.getValue();
					effected++;
				}
				pixelWriter.setColor(readX, readY, color);

			}
		}

		System.out.println("Total effected : " + effected + " / " + total
				+ " = " + ((double) effected / (double) total));

		DecimalFormat df = new DecimalFormat("#.##");
		resultLabel.setText(df
				.format((100.0 * (double) effected / (double) total)) + "%");
		imageView.setImage(wImage);
	}
	
	
}
