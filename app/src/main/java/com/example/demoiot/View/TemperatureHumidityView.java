package com.example.demoiot.View;


public class TemperatureHumidityView {
    private TextView txtTemperature;
    private TextView txtHumidity;

    public TemperatureHumidityView(View view) {
        txtTemperature = view.findViewById(R.id.txtTemperature);
        txtHumidity = view.findViewById(R.id.txtHumidity);
    }

    public void displayTemperature(float temperature) {
        txtTemperature.setText(String.format(Locale.getDefault(), "%.1f°C", temperature));
    }

    public void displayHumidity(float humidity) {
        txtHumidity.setText(String.format(Locale.getDefault(), "%.1f%%", humidity));
    }

}
