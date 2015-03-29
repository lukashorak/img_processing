package katka_fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {

		// Launch the Application
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		MainController mainController = new MainController();

		stage.setScene(new Scene(mainController));
		
		stage.setTitle("Image analyzer");
		stage.setWidth(600);
		stage.setHeight(800);
		stage.setMaximized(true);
		stage.show();
		
	}

}
