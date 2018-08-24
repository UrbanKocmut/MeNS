package si.urban.mens.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

@Database(entities = {Measurement.class, MeasurementType.class, Reading.class, SensorType.class, Test.class},
        version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract AppDao appDao();


    private static AppDatabase INSTANCE;


    public synchronized static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "app_db")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).appDao().insertMeasurementTypes(MeasurementType.populateData());
                                getInstance(context).appDao().insertSensorTypes(SensorType.populateData());
                            }
                        });
                    }
                })
                .allowMainThreadQueries()
                .build();
    }

}
