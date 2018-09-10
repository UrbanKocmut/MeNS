package si.urban.mens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

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

    private CheckBox acXcb;
    private CheckBox acYcb;
    private CheckBox acZcb;
    private CheckBox gyXcb;
    private CheckBox gyYcb;
    private CheckBox gyZcb;

    private final int MAX_DATA_POINTS = 1000000;
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
        vpa.setScrollable(true);

        Viewport vpg = gyroVisGraph.getViewport();
        vpg.setScalable(true);
        vpg.setScrollable(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis);


        accVisGraph = (GraphView) findViewById(R.id.vis_accGraph);
        gyroVisGraph = (GraphView) findViewById(R.id.vis_gyroGraph);

        acXcb = (CheckBox) findViewById(R.id.acXcb);
        acYcb = (CheckBox) findViewById(R.id.acYcb);
        acZcb = (CheckBox) findViewById(R.id.acZcb);

        gyXcb = (CheckBox) findViewById(R.id.gyXcb);
        gyYcb = (CheckBox) findViewById(R.id.gyYcb);
        gyZcb = (CheckBox) findViewById(R.id.gyZcb);


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

    private void setScale(){
        accVisGraph.getViewport().setMinX(2);
        accVisGraph.getViewport().setMaxX(30);
        accVisGraph.getViewport().setMinY(0);
        accVisGraph.getViewport().setMaxY(10);

        accVisGraph.getViewport().setXAxisBoundsManual(true);
        accVisGraph.getViewport().setYAxisBoundsManual(true);
        accVisGraph.getViewport().setScalable(false);
        accVisGraph.getViewport().setScrollable(false);

        gyroVisGraph.getViewport().setMinX(2);
        gyroVisGraph.getViewport().setMaxX(30);
        gyroVisGraph.getViewport().setMinY(0);
        gyroVisGraph.getViewport().setMaxY(10);

        gyroVisGraph.getViewport().setXAxisBoundsManual(true);
        gyroVisGraph.getViewport().setYAxisBoundsManual(true);
        gyroVisGraph.getViewport().setScrollable(false);
        gyroVisGraph.getViewport().setScalable(false);
    }

    private void setScaleAuto(){

        accVisGraph.getViewport().setXAxisBoundsManual(false);
        accVisGraph.getViewport().setYAxisBoundsManual(false);
        accVisGraph.getViewport().setScalable(true);
        accVisGraph.getViewport().setScalableY(true);


        gyroVisGraph.getViewport().setXAxisBoundsManual(false);
        gyroVisGraph.getViewport().setYAxisBoundsManual(false);
        gyroVisGraph.getViewport().setScrollable(true);
        gyroVisGraph.getViewport().setScalableY(true);

    }

    public void drawRaw(View view) {
        initGraphs();
        setScaleAuto();
        fillGraphsRaw();
        addSeries();
    }

    public void drawFreq(View view) {
        initGraphs();
        fillGraphsFreq();
        addSeries();
        setScale();
    }

    public void drawAmp(View view) {
        initGraphs();
        setScaleAuto();
        fillGraphsAmp();
        addSeries();
    }

    private void addSeries() {
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
            if (acXcb.isChecked())
                accXSeries.appendData(new DataPoint(i, reading.X), false, MAX_DATA_POINTS);
            if (acYcb.isChecked())
                accYSeries.appendData(new DataPoint(i, reading.Y), false, MAX_DATA_POINTS);
            if (acZcb.isChecked())
                accZSeries.appendData(new DataPoint(i, reading.Z), false, MAX_DATA_POINTS);
        }
        startTime = readingsGyro[0].timestamp;
        for (int i = 0, readingsGyroLength = readingsGyro.length; i < readingsGyroLength; i++) {
            Reading reading = readingsGyro[i];
            double timestamp = (reading.timestamp - startTime) / 1000d;
            if (gyXcb.isChecked())
                gyroXSeries.appendData(new DataPoint(i, reading.X), false, MAX_DATA_POINTS);
            if (gyYcb.isChecked())
                gyroYSeries.appendData(new DataPoint(i, reading.Y), false, MAX_DATA_POINTS);
            if (gyZcb.isChecked())
                gyroZSeries.appendData(new DataPoint(i, reading.Z), false, MAX_DATA_POINTS);
        }
    }

    private void fillGraphsFreq() {
        Analyzer analyzerAccl = new Analyzer(readingsAccl);
        Analyzer analyzerGyro = new Analyzer(readingsGyro);
        ArrayList<Double> values;

        if (acXcb.isChecked()) {
            values = analyzerAccl.getValuesFreqX();
            for (int i = 0; i < values.size(); i++) {
                Double val = values.get(i);
                accXSeries.appendData(new DataPoint(i / 10d, val), false, MAX_DATA_POINTS);
            }
        }
        if (acYcb.isChecked()) {
            values = analyzerAccl.getValuesFreqY();
            for (int i = 0; i < values.size(); i++) {
                Double val = values.get(i);
                accYSeries.appendData(new DataPoint(i / 10d, val), false, MAX_DATA_POINTS);
            }
        }
        if (acZcb.isChecked()) {
            values = analyzerAccl.getValuesFreqZ();
            for (int i = 0; i < values.size(); i++) {
                Double val = values.get(i);
                accZSeries.appendData(new DataPoint(i / 10d, val), false, MAX_DATA_POINTS);
            }
        }
        if (gyXcb.isChecked()) {
            values = analyzerGyro.getValuesFreqX();
            for (int i = 0; i < values.size(); i++) {
                Double val = values.get(i);
                gyroXSeries.appendData(new DataPoint(i / 10d, val), false, MAX_DATA_POINTS);
            }
        }
        if (gyYcb.isChecked()) {
            values = analyzerGyro.getValuesFreqY();
            for (int i = 0; i < values.size(); i++) {
                Double val = values.get(i);
                gyroYSeries.appendData(new DataPoint(i / 10d, val), false, MAX_DATA_POINTS);
            }
        }
        if (gyZcb.isChecked()) {
            values = analyzerGyro.getValuesFreqZ();
            for (int i = 0; i < values.size(); i++) {
                Double val = values.get(i);
                gyroZSeries.appendData(new DataPoint(i / 10d, val), false, MAX_DATA_POINTS);
            }
        }
    }

    private void fillGraphsAmp() {
        Analyzer analyzerAccl = new Analyzer(readingsAccl);
        Analyzer analyzerGyro = new Analyzer(readingsGyro);
        ArrayList<Double> values;

        if (acXcb.isChecked()) {
            values = analyzerAccl.getValuesAmpX();
            for (int i = 0; i < values.size(); i++) {
                Double val = values.get(i);
                double time = i * (Analyzer.SAMPLE_WIDTH / 1000d);
                accXSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
            }
        }
        if (acYcb.isChecked()) {
            values = analyzerAccl.getValuesAmpY();
            for (int i = 0; i < values.size(); i++) {
                Double val = values.get(i);
                double time = i * (Analyzer.SAMPLE_WIDTH / 1000d);
                accYSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
            }
        }
        if (acZcb.isChecked()) {
            values = analyzerAccl.getValuesAmpZ();
            for (int i = 0; i < values.size(); i++) {
                Double val = values.get(i);
                double time = i * (Analyzer.SAMPLE_WIDTH / 1000d);
                accZSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
            }
        }
        if (gyXcb.isChecked()) {
            values = analyzerGyro.getValuesAmpX();
            for (int i = 0; i < values.size(); i++) {
                Double val = values.get(i);
                double time = i * (Analyzer.SAMPLE_WIDTH / 1000d);
                gyroXSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
            }
        }
        if (gyYcb.isChecked()) {
            values = analyzerGyro.getValuesAmpY();
            for (int i = 0; i < values.size(); i++) {
                Double val = values.get(i);
                double time = i * (Analyzer.SAMPLE_WIDTH / 1000d);
                gyroYSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
            }
        }
        if (gyZcb.isChecked()) {
            values = analyzerGyro.getValuesAmpZ();
            for (int i = 0; i < values.size(); i++) {
                Double val = values.get(i);
                double time = i * (Analyzer.SAMPLE_WIDTH / 1000d);
                gyroZSeries.appendData(new DataPoint(i, val), false, MAX_DATA_POINTS);
            }
        }
    }

    public void switchToDataPicker(View view) {
        Intent intent = new Intent(this, DataPickerActivity.class);
        startActivity(intent);
    }

}
