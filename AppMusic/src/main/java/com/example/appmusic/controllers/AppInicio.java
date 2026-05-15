package com.example.appmusic.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AppInicio implements Initializable {
    @FXML
    public Button btnPlay;
    @FXML
    public Button btnBack;
    @FXML
    public Button btnNext;
    @FXML
    public Slider sliderTiempo;
    @FXML
    public Slider sliderVolumen;
    @FXML
    public Button volumen;
    @FXML
    private TableView tablaCanciones;
    @FXML
    private TableColumn cancion;
    @FXML
    private TableColumn artista;
    @FXML
    private TableColumn duracion;


    private File file = new File("musica/Seoul City.mp3");
    private Media media = new Media(file.toURI().toString());
    private MediaPlayer player = new MediaPlayer(media);
    private boolean isPlaying = false;
    private double volumenAnterior = 0.5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarSliderTiempo();
        cargarUI();
        cargarSliderVolumen();
        cargarCanciones();
        cargarLista();
    }
    private void cargarCanciones(){
        cancion.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        artista.setCellValueFactory(new PropertyValueFactory<>("artista"));

        duracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

    }
    private void cargarLista(){
        ObservableList<Cancion> canciones =
                FXCollections.observableArrayList();

        canciones.add(
                new Cancion(
                        "Seoul City",
                        "Jennie",
                        "02:42"
                )
        );
        canciones.add(
                new Cancion(
                        "West Coast",
                        "Lana del Rey",
                        "04:15"
                )
        );
        tablaCanciones.setItems(canciones);
    }

    private void cargarSliderVolumen() {
        sliderVolumen.setMin(0.0);
        sliderVolumen.setMax(1.0);
        double volumenInicial = 1.0;
        player.setVolume(volumenInicial);
        sliderVolumen.setValue(volumenInicial);

        player.volumeProperty().bind(sliderVolumen.valueProperty());

        sliderVolumen.valueProperty().addListener((observable, oldValue, newValue) -> {
            double vol = newValue.doubleValue();

            if (!player.volumeProperty().isBound()) {
                player.setVolume(vol);
            }

            if (vol == 0.0) {
                cargarIcono(volumen,"/imagen/icons8-mute-50.png");
            } else {
                cargarIcono(volumen,"/imagen/icons8-audio-50.png");
            }

            double porcentaje = vol * 100;
            String estiloCss = String.format(
                    "-fx-background-color: linear-gradient(from 0.0%% 0.0%% to 100.0%% 0.0%%, #7c4a7485 0.0%%, #7c4a7485 %f%%, #ffcdf717 %f%%, #ffcdf717 100.0%%);",
                    porcentaje, porcentaje
            );
            sliderVolumen.lookup(".track").setStyle(estiloCss);
            sliderVolumen.applyCss();
            sliderVolumen.layout();
            sliderVolumen.lookup(".track").setStyle("-fx-background-color: linear-gradient(from 0.0% 0.0% to 100.0% 0.0%, #7c4a7485 0.0%, #7c4a7485 50.0%, #ffcdf717 50.0%, #ffcdf717 100.0%);");

        });
    }

    private void cargarSliderTiempo() {
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
    }

    private void cargarUI() {
        try {
            cargarIcono(btnPlay,"/imagen/icons8-play-50.png");
            cargarIcono(btnNext, "/imagen/icons8-end-50.png");
            cargarIcono(btnBack, "/imagen/icons8-skip-to-start-50.png");
            cargarIcono(volumen,"/imagen/icons8-audio-50.png");
        } catch (NullPointerException e) {
            System.err.println("Error: No se encontró uno de los recursos gráficos en 'resources/imagen/'");
            e.printStackTrace();
        }
    }

    private void cargarIcono(Button boton, String rutaRecurso) {
        boton.setGraphic(crearIcono(rutaRecurso));
    }

    private ImageView crearIcono(String rutaRecurso) {
        URL url = getClass().getResource(rutaRecurso);
        if (url == null) {
            throw new NullPointerException("No se pudo encontrar el recurso: " + rutaRecurso);
        }
        ImageView iv = new ImageView(new Image(url.toExternalForm()));
        iv.setFitWidth(20);
        iv.setFitHeight(20);
        return iv;
    }

    @FXML
    public void onPlayButton(ActionEvent actionEvent) {
        if (isPlaying) {
            player.pause();
            cargarIcono(btnPlay,"/imagen/icons8-play-50.png");
            isPlaying = false;
        } else {
            player.play();
            cargarIcono(btnPlay,"/imagen/icons8-pause-50.png");
            isPlaying = true;
        }
    }

    @FXML
    public void onBackButton(ActionEvent actionEvent) {
        player.seek(Duration.ZERO);
    }

    @FXML
    public void onNextButton(ActionEvent actionEvent) {
        Duration tiempoActual = player.getCurrentTime();
        player.seek(tiempoActual.add(Duration.seconds(15)));
    }

    @FXML
    public void onMuteButton(ActionEvent actionEvent) {
        double volumenActual = sliderVolumen.getValue();
        if (volumenActual > 0.0) {
            volumenAnterior = volumenActual;
            sliderVolumen.valueProperty().unbind();
            sliderVolumen.setValue(0.0);
            player.setVolume(0.0);
        } else {
            player.volumeProperty().bind(sliderVolumen.valueProperty());
            if (volumenAnterior == 0.0) {
                volumenAnterior = 0.2;
            }
            sliderVolumen.setValue(volumenAnterior);
        }
    }
}   

