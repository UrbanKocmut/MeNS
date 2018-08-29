package si.urban.mens;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import si.urban.mens.database.AppDatabase;
import si.urban.mens.database.Reading;

public class VisActivity extends Activity {

    GraphView accVisGraph;
    GraphView gyroVisGraph;


    private Reading[] readingsAccl;
    private Reading[] readingsGyro;

    private final int MAX_DATA_POINTS = 100000;
    private LineGraphSeries<DataPoint> accXSeries;
    private LineGraphSeries<DataPoint> accYSeries;
    private LineGraphSeries<DataPoint> accZSeries;
    private LineGraphSeries<DataPoint> gyroXSeries;
    private LineGraphSeries<DataPoint> gyroYSeries;
    private LineGraphSeries<DataPoint> gyroZSeries;

    private void initGraphs() {

        accVisGraph.removeAllSeries();
        gyroVisGraph.removeAllSeries();

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

        Viewport vpa = accVisGraph.getViewport();
        vpa.setScalable(true);
        vpa.setScalableY(true);

        Viewport vpg = gyroVisGraph.getViewport();
        vpg.setScalable(true);
        vpg.setScalableY(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis);


        accVisGraph = (GraphView) findViewById(R.id.vis_accGraph);
        gyroVisGraph = (GraphView) findViewById(R.id.vis_gyroGraph);


        long measurementId = getIntent().getLongExtra("measurementId", -1);
        if (measurementId == -1) {
            throw new RuntimeException("MeasurementId missing from intent extra");
        }

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        readingsAccl = db.appDao().loadAllAccelerometerReadingsForMeasurement(measurementId);
        readingsGyro = db.appDao().loadAllGyroscopeReadingsForMeasurement(measurementId);

        initGraphs();
        fillGraphsRaw();
        addSeries();


    }

    public void drawRaw(View view){
        initGraphs();
        fillGraphsRaw();
        addSeries();
    }

    public void drawFreq(View view){
        initGraphs();
        fillGraphsFreq();
        addSeries();
    }
    public void drawAmp(View view){
        initGraphs();
        fillGraphsAmp();
        addSeries();
    }

    private void addSeries(){
        accVisGraph.addSeries(accXSeries);
        accVisGraph.addSeries(accYSeries);
        accVisGraph.addSeries(accZSeries);

        gyroVisGraph.addSeries(gyroXSeries);
        gyroVisGraph.addSeries(gyroYSeries);
        gyroVisGraph.addSeries(gyroZSeries);
    }

    private void fillGraphsRaw() {
        long startTime = readingsAccl[0].timestamp;
        for (int i = 0, readingsAcclLength = readingsAccl.length; i < readingsAcclLength; i++) {
            Reading reading = readingsAccl[i];
            double timestamp = (reading.timestamp - startTime) / 1000d;
            accXSeries.appendData(new DataPoint(i, reading.X), false, MAX_DATA_POINTS);
            accYSeries.appendData(new DataPoint(i, reading.Y), false, MAX_DATA_POINTS);
            accZSeries.appendData(new DataPoint(i, reading.Z), false, MAX_DATA_POINTS);
        }
        startTime = readingsGyro[0].timestamp;
        for (int i = 0, readingsGyroLength = readingsGyro.length; i < readingsGyroLength; i++) {
            Reading reading = readingsGyro[i];
            double timestamp = (reading.timestamp - startTime) / 1000d;
            gyroXSeries.appendData(new DataPoint(i, reading.X), false, MAX_DATA_POINTS);
            gyroYSeries.appendData(new DataPoint(i, reading.Y), false, MAX_DATA_POINTS);
            gyroZSeries.appendData(new DataPoint(i, reading.Z), false, MAX_DATA_POINTS);
        }
    }

    private void fillGraphsFreq() {
        Analyzer analyzerAccl = new Analyzer(readingsAccl);
        Analyzer analyzerGyro = new Analyzer(readingsGyro);

        ArrayList<Double> values = analyzerAccl.getValuesFreqX();
        for (int i = 0; i < values.size(); i++) {
            Double val = values.get(i);
            double time = i * (Analyzer.SAMPLE_WIDTH/1000d);
            accXSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
        }
        values = analyzerAccl.getValuesFreqY();
        for (int i = 0; i < values.size(); i++) {
            Double val = values.get(i);
            double time = i * (Analyzer.SAMPLE_WIDTH/1000d);
            accYSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
        }
        values = analyzerAccl.getValuesFreqZ();
        for (int i = 0; i < values.size(); i++) {
            Double val = values.get(i);
            double time = i * (Analyzer.SAMPLE_WIDTH/1000d);
            accZSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
        }

        values = analyzerGyro.getValuesFreqX();
        for (int i = 0; i < values.size(); i++) {
            Double val = values.get(i);
            double time = i * (Analyzer.SAMPLE_WIDTH/1000d);
            gyroXSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
        }
        values = analyzerGyro.getValuesFreqY();
        for (int i = 0; i < values.size(); i++) {
            Double val = values.get(i);
            double time = i * (Analyzer.SAMPLE_WIDTH/1000d);
            gyroYSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
        }
        values = analyzerGyro.getValuesFreqZ();
        for (int i = 0; i < values.size(); i++) {
            Double val = values.get(i);
            double time = i * (Analyzer.SAMPLE_WIDTH/1000d);
            gyroZSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
        }
    }

    private void fillGraphsAmp() {
        Analyzer analyzerAccl = new Analyzer(readingsAccl);
        Analyzer analyzerGyro = new Analyzer(readingsGyro);

        ArrayList<Double> values = analyzerAccl.getValuesAmpX();
        for (int i = 0; i < values.size(); i++) {
            Double val = values.get(i);
            double time = i * (Analyzer.SAMPLE_WIDTH/1000d);
            accXSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
        }
        values = analyzerAccl.getValuesAmpY();
        for (int i = 0; i < values.size(); i++) {
            Double val = values.get(i);
            double time = i * (Analyzer.SAMPLE_WIDTH/1000d);
            accYSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
        }
        values = analyzerAccl.getValuesAmpZ();
        for (int i = 0; i < values.size(); i++) {
            Double val = values.get(i);
            double time = i * (Analyzer.SAMPLE_WIDTH/1000d);
            accZSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
        }

        values = analyzerGyro.getValuesAmpX();
        for (int i = 0; i < values.size(); i++) {
            Double val = values.get(i);
            double time = i * (Analyzer.SAMPLE_WIDTH/1000d);
            gyroXSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
        }
        values = analyzerGyro.getValuesAmpY();
        for (int i = 0; i < values.size(); i++) {
            Double val = values.get(i);
            double time = i * (Analyzer.SAMPLE_WIDTH/1000d);
            gyroYSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
        }
        values = analyzerGyro.getValuesAmpZ();
        for (int i = 0; i < values.size(); i++) {
            Double val = values.get(i);
            double time = i * (Analyzer.SAMPLE_WIDTH/1000d);
            gyroZSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
        }
    }
}
