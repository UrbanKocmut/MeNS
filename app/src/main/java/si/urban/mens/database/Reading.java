package si.urban.mens.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "readings",
        foreignKeys = {@ForeignKey(entity = Measurement.class,
                                   parentColumns = "id",
                                   childColumns = "measurement_id",
                                   onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = SensorType.class,
                            parentColumns = "id",
                            childColumns = "sensor_type_id")})
public class Reading {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "measurement_id")
    @NonNull
    public long measurementId;

    @ColumnInfo(name = "sensor_type_id")
    @NonNull
    public int sensorTypeId;

    @NonNull
    public long timestamp;

    @NonNull
    public float X;

    @NonNull
    public float Y;

    @NonNull
    public float Z;


    public Reading(@NonNull long measurementId, @NonNull int sensorTypeId, @NonNull long timestamp, @NonNull float X, @NonNull float Y, @NonNull float Z) {
        this.id = 0;
        this.measurementId = measurementId;
        this.sensorTypeId = sensorTypeId;
        this.timestamp = timestamp;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }
}
