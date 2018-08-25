package si.urban.mens.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.hardware.Sensor;
import android.media.Rating;

import java.util.Collection;
import java.util.List;

@Dao
public interface AppDao {

    //INSERTS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTests(Test... tests);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSensorTypes(SensorType... sensorTypes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMeasurementTypes(MeasurementType... measurementTypes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertMeasurement(Measurement measurements);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMeasurements(Collection<Measurement> measurements);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertReadings(Reading... readings);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertReadings(Collection<Reading> readings);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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
    public Reading[] loadAllReadingsForMeasurement(int measurementId);

    @Query("SELECT * FROM readings WHERE measurement_id == :measurementId AND sensor_type_id == " + Sensor.TYPE_LINEAR_ACCELERATION)
    public Reading[] loadAllAccelerometerReadingsForMeasurement(int measurementId);

    @Query("SELECT * FROM readings WHERE measurement_id == :measurementId AND sensor_type_id == " + Sensor.TYPE_GYROSCOPE)
    public Reading[] loadAllGyroscopeReadingsForMeasurement(int measurementId);


}
