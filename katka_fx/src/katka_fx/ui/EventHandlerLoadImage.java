package katka_fx.ui;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import katka_fx.MainController;

public class EventHandlerLoadImage implements EventHandler<ActionEvent> {

	MainController c;

	public EventHandlerLoadImage(MainController c) {
		this.c = c;
	}

	@Override
	public void handle(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"JPG Photos", "*.jpg");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setInitialDirectory(new File("c:/tmp"));

		// Show open file dialog
		File dialogResultFile = fileChooser.showOpenDialog(null);

		if (dialogResultFile != null && dialogResultFile.exists()) {
			c.file = dialogResultFile;

			c.filePath.setText(c.file.getPath());
			c.img = new Image(c.file.toURI().toString());
			c.imageView.setImage(c.img);
		}

	}
}
