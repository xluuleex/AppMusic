package com.example.appmusic.controllers;

import com.sun.javafx.menu.MenuItemBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AppInicio implements Initializable {
    @FXML public Button btnPlay;
    @FXML public Button btnBack;
    @FXML public Button btnNext;
    @FXML public Button btnPause;
    @FXML public Slider sliderTiempo;

     File file = new File("musica/Seoul City.mp3");
     Media media = new Media(file.toURI().toString());
     MediaPlayer player = new MediaPlayer(media);

    boolean isPlaying = false;

    public AppInicio() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sliderTiempo.setMin(0);
        sliderTiempo.setMax(100);

        player.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            Duration total = player.getTotalDuration();
            if (total != null) {
                double progreso = newTime.toSeconds() / total.toSeconds();
                sliderTiempo.setValue(progreso * 100);
            }
        });

        sliderTiempo.setOnMouseReleased(event -> {
            double porcentaje = sliderTiempo.getValue() / 100;
            Duration total = player.getTotalDuration();
            if (total != null) {
                player.seek(total.multiply(porcentaje));
            }
        });

        Image imagen = new Image(getClass().getResourceAsStream("/imagen/icons8-play-50.png"));
        ImageView icono = new ImageView(imagen);
        icono.setFitWidth(30);
        icono.setFitHeight(30);
        btnPlay.setGraphic(icono);

        Image imagenNext = new Image(getClass().getResourceAsStream("/imagen/icons8-end-50.png"));
        ImageView iconoNext = new ImageView(imagenNext);
        iconoNext.setFitWidth(30);
        iconoNext.setFitHeight(30);
        btnNext.setGraphic(iconoNext);

        Image imagenBack = new Image((getClass().getResourceAsStream("/imagen/icons8-skip-to-start-50.png")));
        ImageView iconoBack = new ImageView(imagenBack);
        iconoBack.setFitWidth(30);
        iconoBack.setFitHeight(30);
        btnBack.setGraphic(iconoBack);

        Image imagenPause = new Image((getClass().getResourceAsStream("/imagen/icons8-pause-50.png")));
        ImageView iconoPause = new ImageView(imagenPause);
        iconoPause.setFitWidth(30);
        iconoPause.setFitHeight(30);
        btnPause.setGraphic(iconoPause);

    }

    public void onBackButton(ActionEvent actionEvent) {
    }

    public void onPlayButton(ActionEvent actionEvent) {
        if (isPlaying) {
            player.pause();

            isPlaying = false;
        } else {
            player.play();
            isPlaying = true;
        }
    }

    public void onNextButton(ActionEvent actionEvent) {
        player.seek(Duration.seconds(15));
    }

    public void onPauseButton(ActionEvent actionEvent) {
    }
}
