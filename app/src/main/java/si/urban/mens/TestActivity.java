package si.urban.mens;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Locale;

public class TestActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    TextView accelTextView;
    TextView gyroTextView;
    ArrayList<MainActivity.DataChunk> gyroData;
    ArrayList<MainActivity.DataChunk> acclData;
    TextView accLbl;
    TextView gyroLbl;
    long startTime;
    static private final long testDuration = 20 * 1000L;

    GraphView accGraph;
    GraphView gyroGraph;

    private final int MAX_DATA_POINTS = 1000;
    LineGraphSeries<DataPoint> accXSeries;
    LineGraphSeries<DataPoint> accYSeries;
    LineGraphSeries<DataPoint> accZSeries;
    LineGraphSeries<DataPoint> gyroXSeries;
    LineGraphSeries<DataPoint> gyroYSeries;
    LineGraphSeries<DataPoint> gyroZSeries;

    private boolean record =false;


    private void initGraphs() {
        accXSeries = new LineGraphSeries<>();
        accYSeries = new LineGraphSeries<>();
        accZSeries = new LineGraphSeries<>();

        accXSeries.setColor(Color.RED);
        accYSeries.setColor(Color.GREEN);
        accZSeries.setColor(Color.BLUE);

        gyroXSeries = new LineGraphSeries<>();
        gyroYSeries = new LineGraphSeries<>();
        gyroZSeries = new LineGraphSeries<>();

        gyroXSeries.setColor(Color.RED);
        gyroYSeries.setColor(Color.GREEN);
        gyroZSeries.setColor(Color.BLUE);

        Viewport vp = accGraph.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(MAX_DATA_POINTS);
        vp.setMaxY(50d);
        vp.setMinY(-50d);
        vp.setXAxisBoundsManual(true);
        vp.setYAxisBoundsManual(true);

        vp = gyroGraph.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(MAX_DATA_POINTS);
        vp.setMaxY(50d);
        vp.setMinY(-50d);
        vp.setXAxisBoundsManual(true);
        vp.setYAxisBoundsManual(true);

        accGraph.addSeries(accXSeries);
        accGraph.addSeries(accYSeries);
        accGraph.addSeries(accZSeries);

        gyroGraph.addSeries(gyroXSeries);
        gyroGraph.addSeries(gyroYSeries);
        gyroGraph.addSeries(gyroZSeries);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);



        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelTextView = (TextView) findViewById(R.id.activity_test_accelTextView);
        accLbl = (TextView) findViewById(R.id.activity_test_accLbl);
        gyroTextView = (TextView) findViewById(R.id.activity_test_gyroTextView);
        gyroLbl = (TextView) findViewById(R.id.activity_test_gyroLbl);
        accGraph = (GraphView) findViewById(R.id.activity_test_accGraph);
        gyroGraph = (GraphView) findViewById(R.id.activity_test_gyroGraph);

        startTime = System.currentTimeMillis();

        initGraphs();
        startRecording();


    }

    private void startRecording() {
        record = true;
    }

    private void stopRecording() {
        record = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!record)
            return;

        if ((System.currentTimeMillis() - startTime) < testDuration) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    accelTextView.setText(String.format(Locale.getDefault(), "X:%.2f,Y:%.2f,Z:%.2f", event.values[0], event.values[1], event.values[2]));
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    gyroTextView.setText(String.format(Locale.getDefault(), "X:%.2f,Y:%.2f,Z:%.2f", event.values[0], event.values[1], event.values[2]));
                    break;
            }
        } else {
            stopRecording();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
