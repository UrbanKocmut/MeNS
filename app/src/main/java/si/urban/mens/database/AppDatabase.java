package si.urban.mens.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Measurement.class, MeasurementType.class, Reading.class, SensorType.class, Test.class},
          version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao appDao();
}
