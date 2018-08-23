package si.urban.mens.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.constraint.Constraints;

@Entity(tableName = "measurements",
        foreignKeys = {@ForeignKey(entity = Test.class,
                                   parentColumns = "id",
                                   childColumns = "test_id",
                                   onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = MeasurementType.class,
                            parentColumns = "id",
                            childColumns = "measurement_type_id")})
public class Measurement {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "test_id")
    @NonNull
    public int testId;

    @ColumnInfo(name = "measurement_type_id")
    @NonNull
    public int measurementTypeId;

    public Measurement(int id, @NonNull int testId, @NonNull int measurementTypeId) {
        this.id = id;
        this.testId = testId;
        this.measurementTypeId = measurementTypeId;
    }
}
