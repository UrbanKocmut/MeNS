package si.urban.mens;

import org.jtransforms.fft.DoubleFFT_1D;

import java.util.ArrayList;

import si.urban.mens.database.Reading;

public class Analyzer {

    private Reading[] readings;
    private ArrayList<Double> valuesFreq;
    private ArrayList<Double> valuesAmp;
    public static final int SAMPLE_WIDTH = 1000;//100ms

    /**
     * Readings have to be prefildered for sensorType and axis.
     *
     * @param readings
     */
    public Analyzer(Reading[] readings) {
        this.readings = readings;
        valuesFreq = new ArrayList<>();
        valuesAmp = new ArrayList<>();
    }

    private void calSpectralDensityX(){
        valuesFreq = new ArrayList<>();
        double [] data = new double [readings.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = (double)readings[i].X;
        }
        DoubleFFT_1D df1 = new DoubleFFT_1D(100);
        df1.realForwardFull(data);
        for (double d : data) {
            valuesFreq.add(d*d);

        }
    }

    private void calSpectralDensityY(){
        valuesFreq = new ArrayList<>();
        double [] data = new double [readings.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = (double)readings[i].Y;
        }
        DoubleFFT_1D df1 = new DoubleFFT_1D(100);
        df1.realForwardFull(data);
        for (double d : data) {
            valuesFreq.add((d*d));

        }
    }

    private void calSpectralDensityZ(){
        valuesFreq = new ArrayList<>();
        double [] data = new double [readings.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = (double)readings[i].Z;
        }
        DoubleFFT_1D df1 = new DoubleFFT_1D(100);
        df1.realForwardFull(data);
        for (double d : data) {
            valuesFreq.add((d*d));

        }
    }

    private void calculateFrequencyX() {
        valuesFreq = new ArrayList<>();
        long timeDelta = 0;
        long startTime = readings[0].timestamp;
        double previous = readings[0].X;
        int n = 0;
        for (int i = 1; i < readings.length; i++) {
            Reading reading = readings[i];
            timeDelta += (reading.timestamp - startTime);
            if (Math.signum(reading.X) + Math.signum(previous) == 0) {
                n++;
            }
            if (timeDelta > SAMPLE_WIDTH) {
                timeDelta = 0;
                valuesFreq.add(n == 0 ? 0 : (1000d / SAMPLE_WIDTH) * n);
                n=0;
            }
        }
    }

    private void calculateFrequencyY() {
        valuesFreq = new ArrayList<>();
        long timeDelta = 0;
        long startTime = readings[0].timestamp;
        double previous = readings[0].Y;
        int n = 0;
        for (int i = 1; i < readings.length; i++) {
            Reading reading = readings[i];
            timeDelta += (reading.timestamp - startTime);
            if (Math.signum(reading.Y) + Math.signum(previous) == 0) {
                n++;
            }
            if (timeDelta > SAMPLE_WIDTH) {
                timeDelta = 0;
                valuesFreq.add(n == 0 ? 0 : (1000d / SAMPLE_WIDTH)  / n);
                n=0;
            }
        }
    }

    private void calculateFrequencyZ() {
        valuesFreq = new ArrayList<>();
        long timeDelta = 0;
        long startTime = readings[0].timestamp;
        double previous = readings[0].Z;
        int n = 0;
        for (int i = 1; i < readings.length; i++) {
            Reading reading = readings[i];
            timeDelta += (reading.timestamp - startTime);
            if (Math.signum(reading.Z) + Math.signum(previous) == 0) {
                n++;
            }
            if (timeDelta > SAMPLE_WIDTH) {
                timeDelta = 0;
                valuesFreq.add(n == 0 ? 0 : (1000d / SAMPLE_WIDTH)  / n);
                n=0;
            }
        }
    }

    private void calculateAmplitudeX() {
        valuesAmp = new ArrayList<>();
        long timeDelta = 0;
        long startTime = readings[0].timestamp;
        int n = 0;
        double sum = 0d;
        for (int i = 0; i < readings.length; i++) {
            Reading reading = readings[i];
            timeDelta += (reading.timestamp - startTime);
            sum += Math.abs(reading.X);
            n++;
            if (timeDelta > SAMPLE_WIDTH) {
                timeDelta = 0;
                valuesAmp.add(sum / n);
                sum = 0;
                n = 0;
            }
        }
    }

    private void calculateAmplitudeY() {
        valuesAmp = new ArrayList<>();
        long timeDelta = 0;
        long startTime = readings[0].timestamp;
        int n = 0;
        double sum = 0d;
        for (int i = 0; i < readings.length; i++) {
            Reading reading = readings[i];
            timeDelta += (reading.timestamp - startTime);
            sum += Math.abs(reading.Y);
            n++;
            if (timeDelta > SAMPLE_WIDTH) {
                timeDelta = 0;
                valuesAmp.add(sum / n);
                sum = 0;
                n = 0;
            }
        }
    }

    private void calculateAmplitudeZ() {
        valuesAmp = new ArrayList<>();
        long timeDelta = 0;
        long startTime = readings[0].timestamp;
        int n = 0;
        double sum = 0d;
        for (int i = 0; i < readings.length; i++) {
            Reading reading = readings[i];
            timeDelta += (reading.timestamp - startTime);
            sum += Math.abs(reading.Z);
            n++;
            if (timeDelta > SAMPLE_WIDTH) {
                timeDelta = 0;
                valuesAmp.add(sum / n);
                sum = 0;
                n = 0;
            }
        }
    }

    public ArrayList<Double> getValuesFreqX() {
        calSpectralDensityX();
        return valuesFreq;
    }

    public ArrayList<Double> getValuesFreqY() {
        calSpectralDensityY();
        return valuesFreq;
    }

    public ArrayList<Double> getValuesFreqZ() {
        calSpectralDensityZ();
        return valuesFreq;
    }

    public ArrayList<Double> getValuesAmpX() {
        calculateAmplitudeX();
        return valuesAmp;
    }

    public ArrayList<Double> getValuesAmpY() {
        calculateAmplitudeY();
        return valuesAmp;
    }

    public ArrayList<Double> getValuesAmpZ() {
        calculateAmplitudeZ();
        return valuesAmp;
    }
}
