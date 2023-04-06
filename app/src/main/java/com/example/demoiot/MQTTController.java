package com.example.demoiot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.angads25.toggle.widget.LabeledSwitch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;

public class MQTTController extends AppCompatActivity {
    MQTTModel mqttModel;
    TextView txtTemp, txtHumid, txtAI;
    LabeledSwitch btnLED;
    SeekBar btnFAN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize view
        initializeView();
        // initialize model
        startMQTT();
    }

    private void initializeView() {
        setContentView(R.layout.activity_main);
        // set view elements
        txtTemp = findViewById(R.id.txtTemperature);
        txtHumid = findViewById(R.id.txtHumidity);
        btnLED = findViewById(R.id.btnLED);
        btnFAN = findViewById(R.id.btnFan);
        txtAI = findViewById(R.id.txtAI);

        btnLED.setOnToggledListener((labeledSwitch, isOn) -> {
            // Implement your switching logic here
            if (isOn == true) {
                sendDataMQTT("KanNan312/feeds/iot.led", "1");
//                if(res == false) {
//                    btnLED.setOn(false);
//                }
            } else {
                sendDataMQTT("KanNan312/feeds/iot.led", "0");
//                if(res == false) {
//                    btnLED.setOn(true);
//                }
            }
        });

        btnFAN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sendDataMQTT("KanNan312/feeds/iot.fan", Integer.toString(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void startMQTT() {
        mqttModel = new MQTTModel(this);
        mqttModel.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.d("MQTT", "Connected!");
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.d("MQTT", "Connection lost!");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d("MQTT", "Delivery completed!");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String message_txt = message.toString();
                Log.w("MQTT", topic + "***" + message_txt);

                if (topic.contains("iot.temperature")) {
                    updateTemperature(message_txt);
                } else if (topic.contains("iot.humidity")) {
                    updateHumidity(message_txt);
                } else if (topic.contains("iot.led")) {
                    updateLED(message_txt);
                } else if (topic.contains("iot.fan")) {
                    updateFan(message_txt);
                } else if (topic.contains("iot.human-detect")) {
                    updateAI(message_txt);
                }
            }

        });
    }
    public boolean sendDataMQTT (String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttModel.mqttAndroidClient.publish(topic, msg);
        } catch (MqttException e) {
            Log.e("MQTT", "Failed to publish message: " + e.getMessage(), e);
            return false;
        }

        return true;
    }
    public void updateTemperature(String value) {
        txtTemp.setText(value + "°C");
    }

    public void updateHumidity(String value) {
        txtHumid.setText(value + "%");
    }

    public void updateAI(String value) {
        txtAI.setText(value);
    }

    public void updateFan(String value) {
        btnFAN.setProgress(Integer.parseInt(value));
    }

    public void updateLED(String value) {
        if (value.equals("1")) {
            btnLED.setOn(true);
        } else {
            btnLED.setOn(false);
        }
    }
}