package com.example.regulatorclient.dto;

public class TemperatureDto {
    private Float temperature;

    public TemperatureDto() {}

    public TemperatureDto(Float temperature) {
        this.temperature = temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getTemperature() {
        return temperature;
    }
}
