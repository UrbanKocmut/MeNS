package si.urban.mens;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private boolean record = false;
    TextView accelTextView;
    TextView gyroTextView;
    ArrayList<DataChunk> gyroData;
    ArrayList<DataChunk> acclData;
    Button startBtn;
    TextView accLbl;
    TextView gyroLbl;
    long startTime;
    static private final long testDuration = 20 * 1000000000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        System.out.println("INIT DONE");
    }

    private void init(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelTextView = (TextView) findViewById(R.id.accelTextView);
        accelTextView.setVisibility(View.GONE);
        accLbl = (TextView) findViewById(R.id.accLbl);
        accLbl.setVisibility(View.GONE);
        gyroTextView = (TextView) findViewById(R.id.gyroTextView);
        gyroTextView.setVisibility(View.GONE);
        gyroLbl = (TextView) findViewById(R.id.gyroLbl);
        gyroLbl.setVisibility(View.GONE);
        startBtn = (Button) findViewById(R.id.startBtn);
        gyroData = new ArrayList<>();
        acclData = new ArrayList<>();
    }

    public void startRecording(View view) {
        record = true;
        startTime = System.nanoTime();
        startBtn.setVisibility(View.GONE);
        accelTextView.setVisibility(View.VISIBLE);
        gyroTextView.setVisibility(View.VISIBLE);
        accLbl.setVisibility(View.VISIBLE);
        gyroLbl.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (record && (System.nanoTime() - startTime) < testDuration) {
            DataChunk dataChunk = new DataChunk(event.values, System.nanoTime());
            switch (event.sensor.getType()) {
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    accelTextView.setText(String.format(Locale.getDefault(), "X:%.2f,Y:%.2f,Z:%.2f",event.values[0], event.values[1], event.values[2]));
                    acclData.add(dataChunk);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    gyroTextView.setText(String.format(Locale.getDefault(), "X:%.2f,Y:%.2f,Z:%.2f", event.values[0], event.values[1], event.values[2]));
                    gyroData.add(dataChunk);
                    break;
            }
        } else {
            startBtn.setVisibility(View.VISIBLE);
            gyroTextView.setVisibility(View.GONE);
            accelTextView.setVisibility(View.GONE);
            accLbl.setVisibility(View.GONE);
            gyroLbl.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    class DataChunk{
        float X, Y, Z;
        long ts;

        public DataChunk(float x, float y, float z, long timestamp){
            X = x;
            Y = y;
            Z = z;
            ts = timestamp;
        }

        public DataChunk(float [] data, long timestamp){
            X = data[0];
            Y = data[1];
            Z = data[2];
            ts = timestamp;
        }
    }
}
