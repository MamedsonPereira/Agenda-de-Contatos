package com.ifsc.tds.Vitor.Cassio.Igor.menu;

import com.ifsc.tds.Vitor.Cassio.Igor.controller.ContatoListaController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class menu extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ifsc/tds/Vitor/Cassio/Igor/view/ContatoLista.fxml"));
			Parent menuXML = loader.load();

			ContatoListaController menuController = loader.getController();
			Scene menuLayout = new Scene(menuXML);

			Stage menuJanela = new Stage();
			menuJanela.initModality(Modality.APPLICATION_MODAL);
			menuJanela.resizableProperty().setValue(Boolean.FALSE);
			menuJanela.setScene(menuLayout);
			menuJanela.setTitle("Menu do sistema");

			menuJanela.setOnCloseRequest(e -> {
				if (menuController.onCloseQuery()) {
					System.exit(0);
				} else {
					e.consume();
				}
			});

			menuJanela.show();

			Rectangle2D posicaoJanela = Screen.getPrimary().getVisualBounds();
			menuJanela.setX((posicaoJanela.getWidth() - menuJanela.getWidth()) / 2);
			menuJanela.setY((posicaoJanela.getHeight() - menuJanela.getHeight()) / 2);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
