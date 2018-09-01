package si.urban.mens.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.hardware.Sensor;
import android.support.annotation.NonNull;

import si.urban.mens.R;

@Entity(tableName = "measurement_types")
public class MeasurementType {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "type_name")
    @NonNull
    public String typeName;

    public MeasurementType(int id, @NonNull String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public static MeasurementType[] populateData() {
        return new MeasurementType[] {
                new MeasurementType(0, "Test1"),
                new MeasurementType(1, "Test2"),
                new MeasurementType(2, "Test3"),
        };
    }

}
