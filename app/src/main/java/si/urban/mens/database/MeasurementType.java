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
                new MeasurementType(0, "Test1L"),
                new MeasurementType(1, "Test1R"),
                new MeasurementType(2, "Test2L"),
                new MeasurementType(3, "Test2R"),
                new MeasurementType(4, "Test3L"),
                new MeasurementType(5, "Test3R")
        };
    }

}
