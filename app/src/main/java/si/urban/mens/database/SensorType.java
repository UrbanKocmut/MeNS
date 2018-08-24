package si.urban.mens.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.hardware.Sensor;
import android.support.annotation.NonNull;

@Entity(tableName = "sensor_types")
public class SensorType {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "type_name")
    @NonNull
    public String typeName;

    public SensorType(int id, @NonNull String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public static SensorType[] populateData() {
        return new SensorType[] {
                new SensorType(Sensor.TYPE_LINEAR_ACCELERATION, "Accelerometer"),
                new SensorType(Sensor.TYPE_GYROSCOPE, "Gyroscope")
        };
    }

}
