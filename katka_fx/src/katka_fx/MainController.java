package katka_fx;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
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

public class MainController extends HBox {

	public File file;
	public Image img;

	@FXML
	private Label filePath;

	@FXML
	private Label resultLabel;

	@FXML
	private Label toleranceLabel;

	@FXML
	private Button loadButton;

	@FXML
	private ImageView imageView;

	@FXML
	private Slider tolerance;

	@FXML
	private ColorPicker colorPickerFill;

	@FXML
	private ColorPicker colorPickerTarget;

	@FXML
	private ChoiceBox methodChoice;

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

		tolerance.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				changeTolerance();
			}
		});

		colorPickerFill.setValue(new Color(1.0, 0.1, 0.1, 1.0));
		colorPickerTarget.setValue(new Color(0.0, 1.0, 0.1, 1.0));

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
				Color pixelColor = pixelReader.getColor(readX, readY);

				// Now write a brighter color to the PixelWriter.
				if (this.checkTresholdHSV(pixelColor)) {
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

	public boolean checkTreshold(Color color) {

		String choiceValue = (String) this.methodChoice.getValue();
		switch (choiceValue) {
		case "Lower than Treshold":
			return this.checkTresholdSmaller(color);
		case "Higher than Treshold":
			return this.checkTresholdBigger(color);
		case "Hue within Treshold":
			return this.checkTresholdHSV(color);
		default:
			return false;
		}
	}

	public boolean checkTresholdSmaller(Color color) {
		double toleranceValue = this.tolerance.getValue();
		return (color.getGreen() < toleranceValue);
	}

	public boolean checkTresholdBigger(Color color) {
		double toleranceValue = this.tolerance.getValue();
		return (color.getGreen() > toleranceValue);
	}

	public boolean checkTresholdHSV(Color color) {
		double toleranceValue = this.tolerance.getValue();
		double targetHue = this.colorPickerTarget.getValue().getHue();

		double hue = color.getHue();

		boolean is = (Math.abs(targetHue - hue)) < (toleranceValue * 360.0);

		return is;
	}

}
