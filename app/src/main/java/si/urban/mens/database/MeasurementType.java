package si.urban.mens.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "measurement_types")
public class MeasurementType {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "type_name")
    @NonNull
    public String typeName;

}
