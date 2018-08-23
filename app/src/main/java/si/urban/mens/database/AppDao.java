package si.urban.mens.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.hardware.Sensor;

import java.util.Collection;
import java.util.List;

@Dao
public interface AppDao {

    //INSERTS
    @Insert
    public void insertTests(Test... tests);

    @Insert
    public void insertSensorTypes(SensorType... sensorTypes);

    @Insert
    public void insertMeasurementTypes(MeasurementType... measurementTypes);

    @Insert
    public void insertMeasurements(Measurement... measurements);

    @Insert
    public void insertMeasurements(Collection<Measurement> measurements);

    @Insert
    public void insertReadings(Reading... readings);

    @Insert
    public void insertReadings(Collection<Reading> readings);

    @Insert
    public void insertMeasurementAndReadings(Measurement measurement, List<Reading> readings);

    //UPDATES
    @Update
    public void updateSensorTypes(SensorType... sensorTypes);

    @Update
    public void updateMeasurementTypes(MeasurementType... measurementTypes);

    //DELETES
    @Delete
    public void deleteTests(Test... tests);

    @Delete
    public void deleteMeasurements(Measurement... measurements);

    //QUERIES

    @Query("SELECT * FROM tests")
    public Test[] loadAllTests();

    @Query("SELECT * FROM measurements WHERE test_id == :testId")
    public Measurement[] loadAllMeasurementsForTest(int testId);

    @Query("SELECT * FROM readings WHERE measurement_id == :measurementId")
    public Measurement[] loadAllReadingsForMeasurement(int measurementId);

    @Query("SELECT * FROM readings WHERE measurement_id == :measurementId AND sensor_type_id == " + Sensor.TYPE_LINEAR_ACCELERATION)
    public Measurement[] loadAllAccelerometerReadingsForMeasurement(int measurementId);

    @Query("SELECT * FROM readings WHERE measurement_id == :measurementId AND sensor_type_id == " + Sensor.TYPE_GYROSCOPE)
    public Measurement[] loadAllGyroscopeReadingsForMeasurement(int measurementId);


}