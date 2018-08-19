package si.urban.mens;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
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
    Button saveBtn;
    TextView accLbl;
    TextView gyroLbl;
    long startTime;
    private final String SAVE_FILE_NAME = "MesurementData";
    static private final long testDuration = 20 * 1000000000L;
    private final int PERMISSIN_CODE = 10;

    GraphView accGraph;
    GraphView gyroGraph;

    private final int MAX_DATA_POINTS = 1000;
    LineGraphSeries<DataPoint> accXSeries;
    LineGraphSeries<DataPoint> accYSeries;
    LineGraphSeries<DataPoint> accZSeries;
    LineGraphSeries<DataPoint> gyroXSeries;
    LineGraphSeries<DataPoint> gyroYSeries;
    LineGraphSeries<DataPoint> gyroZSeries;
    private long I = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        System.out.println("INIT DONE");
    }

    private void init() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIN_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

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
        saveBtn = (Button) findViewById(R.id.saveBtn);

        gyroData = new ArrayList<>();
        acclData = new ArrayList<>();

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


        accGraph = (GraphView) findViewById(R.id.accGraph);
        gyroGraph = (GraphView) findViewById(R.id.gyroGraph);

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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIN_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void startRecording(View view) {
        record = true;
        startTime = System.currentTimeMillis();
        startBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.GONE);
        accelTextView.setVisibility(View.VISIBLE);
        gyroTextView.setVisibility(View.VISIBLE);
        accLbl.setVisibility(View.VISIBLE);
        gyroLbl.setVisibility(View.VISIBLE);
    }

    public void saveToFile(View view) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File saveData = new File(path, SAVE_FILE_NAME + System.currentTimeMillis() + ".txt");
        try (PrintWriter printWriter = new PrintWriter(saveData)) {
            printWriter.write("aX;aY;aZ;at;gX;gY;gZ;gt\n");
            Iterator<DataChunk> accIt = acclData.iterator();
            Iterator<DataChunk> gyroIt = gyroData.iterator();
            while (accIt.hasNext() && gyroIt.hasNext()) {
                DataChunk accDataChunk = accIt.next();
                DataChunk gyroDataChunk = gyroIt.next();
                printWriter.append(accDataChunk.X + ";" + accDataChunk.Y + ";" + accDataChunk.Z + ";" + accDataChunk.timestamp + ";" + gyroDataChunk.X + ";" + gyroDataChunk.Y + ";" + gyroDataChunk.Z + ";" + gyroDataChunk.timestamp + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (record && (System.currentTimeMillis() - startTime) < testDuration) {
            DataChunk dataChunk = new DataChunk(event.values, System.currentTimeMillis() - startTime);
            switch (event.sensor.getType()) {
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    accelTextView.setText(String.format(Locale.getDefault(), "X:%.2f,Y:%.2f,Z:%.2f", event.values[0], event.values[1], event.values[2]));
                    acclData.add(dataChunk);
                    accXSeries.appendData(new DataPoint(dataChunk.timestamp,dataChunk.X),true,MAX_DATA_POINTS,false);   //mogoce ni pri vseh treba scrollToEnd dat true, samo na zadnjem?
                    accYSeries.appendData(new DataPoint(dataChunk.timestamp,dataChunk.Y),true,MAX_DATA_POINTS,false);
                    accZSeries.appendData(new DataPoint(dataChunk.timestamp,dataChunk.Z),true,MAX_DATA_POINTS,false);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    gyroTextView.setText(String.format(Locale.getDefault(), "X:%.2f,Y:%.2f,Z:%.2f", event.values[0], event.values[1], event.values[2]));
                    gyroData.add(dataChunk);
                    break;
            }
        } else {
            saveBtn.setVisibility(View.VISIBLE);
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

    class DataChunk {
        float X, Y, Z;
        long timestamp;

        public DataChunk(float x, float y, float z, long timestamp) {
            X = x;
            Y = y;
            Z = z;
            this.timestamp = timestamp;
        }

        DataChunk(float[] data, long timestamp) {
            X = data[0];
            Y = data[1];
            Z = data[2];
            this.timestamp = timestamp;
        }
    }
}
