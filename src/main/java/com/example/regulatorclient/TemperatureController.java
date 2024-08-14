package com.example.regulatorclient;

import com.example.regulatorclient.dto.TemperatureDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Random;

public class TemperatureController {
    @FXML
    private TextField textField;

    @FXML
    private TableView<TemperatureDto> tableView;

    @FXML
    private TableColumn<TemperatureDto, Float> temperatureColumn;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String uri = "http://localhost:8080/temperatures";

    // Окно оповещения с информацией о добавлении температуры
    private final Notifications notificationBuilder = Notifications.create()
            .title("Добавление температуры")
            .hideAfter(Duration.seconds(3))
            .position(Pos.TOP_LEFT);

    @FXML
    public void insertTemperatureButtonClick() throws Exception {
        var temperature = Float.parseFloat(textField.getText());

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .headers("test", String.valueOf(new Random().nextInt()))
                .headers("content-type", "application/json")
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(
                        new TemperatureDto(temperature))))
                .build();

        sendRequest(httpRequest);
    }

    @FXML
    public void getListButtonClick() throws Exception {
        var count = new Random().nextInt(10) + 3;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .headers("test", String.valueOf(new Random().nextInt()))
                .uri(URI.create(uri + "?offsetOut=0&count=" + count))
                .GET()
                .build();

        var response = sendRequest(httpRequest);

        List<TemperatureDto> temperatures = objectMapper.readValue(response.body(), new TypeReference<>() {});
        temperatures.forEach(temperatureDto -> {
            var temperature = temperatureDto.getTemperature();
            if (temperature > 1000 || temperature < -200) {
                notificationBuilder.text("Температура превышена");
                notificationBuilder.showConfirm();
            }
            tableView.getItems().add(temperatureDto);
        });

        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
    }

    private HttpResponse<String> sendRequest(HttpRequest httpRequest) throws Exception {
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Ошибка отправки запроса");
        }
        var error = ErrorHandler.error(Integer.parseInt(response.body()));
        if (error.getKey()) {
            notificationBuilder.text(error.getValue());
            notificationBuilder.showError();
        }

        return response;
    }
}