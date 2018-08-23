package si.urban.mens.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "tests")
public class Test {
    @PrimaryKey
    private int id;

    @NonNull
    private long timestamp;

    public Test(int id, @NonNull long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public long getTimestamp() {
        return timestamp;
    }
}
