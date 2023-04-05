package com.example.demoiot.Controller;

import com.example.demoiot.Model.TemperatureHumidityModel;
import com.example.demoiot.View.TemperatureHumidityView;


public class TemperatureHumidityController  {

    private TemperatureHumidityModel model;
    private TemperatureHumidityView view;

    public TemperatureHumidityController(TemperatureHumidityModel model, TemperatureHumidityView view) {
        this.model = model;
        this.view = view;
    }

    public void updateTemperature(float temperature) {
        model.setTemperature(temperature);
        view.displayTemperature(temperature);
    }

    public void updateHumidity(float humidity) {
        model.setHumidity(humidity);
        view.displayHumidity(humidity);
    }
}

