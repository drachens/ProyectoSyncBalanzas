package com.marsol.sync.controller;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ConfigController extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    private ComboBox<String> comboBox;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Test Configuracion");
        ListView listView = new ListView();

        listView.getItems().add("API REST");
        listView.getItems().add("File JSON");
        listView.getItems().add("File XML");

        Button button = new Button("Configuracion");
        button.setOnAction((ActionEvent event) -> {
            ObservableList<String> items = listView.getSelectionModel().getSelectedItems();
            for (String item : items) {
                System.out.println(item);
            }
        });
        VBox vBox = new VBox(listView,button);
        Scene scene = new Scene(vBox,300,200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
