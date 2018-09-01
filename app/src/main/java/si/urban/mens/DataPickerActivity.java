package si.urban.mens;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;



import si.urban.mens.database.AppDatabase;
import si.urban.mens.database.Measurement;
import si.urban.mens.database.Reading;

public class DataPickerActivity extends Activity implements DataPickerRecyclerViewAdapter.ItemClickListener {

    private DataPickerRecyclerViewAdapter adapter;
    private AppDatabase db;
    private Measurement[] measurements;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_picker);


        db = AppDatabase.getInstance(getApplicationContext());

        // data to populate the RecyclerView with
        ArrayList<String> measurementIds = new ArrayList<>();
        ArrayList<LocalDateTime> dateTimes = new ArrayList<>();

        long testId = getSharedPreferences("shared_pref", MODE_PRIVATE).getLong("testId", -1l);
        measurements = db.appDao().loadAllMeasurementsForTest(testId);
        for (Measurement measurement : measurements) {
            measurementIds.add("" + measurement.id);
            dateTimes.add(LocalDateTime.ofInstant(Instant.ofEpochMilli(measurement.timestamp), TimeZone.getDefault().toZoneId()));
        }

        // set up the RecyclerView
        recyclerView = findViewById(R.id.datarv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        adapter = new DataPickerRecyclerViewAdapter(this, measurementIds, dateTimes);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


    }

    public void switchToMainMenu(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    public void deleteElements(View view) {
        ArrayList<Integer> removed = adapter.removeSelected();
        ArrayList<Measurement> forDelete = new ArrayList<>();
        for (int i = removed.size() - 1; i >= 0; i--) {
            forDelete.add(measurements[i]);
            Integer index = removed.get(i);
            adapter.notifyItemRemoved(index);
        }
        db.appDao().deleteMeasurements(forDelete);
    }

    public void sendElements(View view) {
        ArrayList<Integer> selected = adapter.returnSelected();
        deselectElements(selected);
        ArrayList<Measurement> measurements = new ArrayList<>();
        ArrayList<Reading> readings = new ArrayList<>();

        SharedPreferences sp = getSharedPreferences("shared_pref", MODE_PRIVATE);

        String url = "jdbc:mysql://" + sp.getString("IP", "") + ":" + sp.getString("PORT", "") + "/" + sp.getString("SCHEME", "");
        String username = sp.getString("USERNAME", "");
        String password = sp.getString("PASSWORD", "");

        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");

            Statement statement = connection.createStatement();
            for (Measurement m : measurements) {
                String sql = "INSERT INTO `measurements` (id,test_id,measurement_type_id,timestamp,hand) VALUES (" + m.id + m.testId + m.measurementTypeId + m.timestamp + m.hand + ");";
                statement.addBatch(sql);
            }

            for (Reading r : readings) {
                String sql = "INSERT INTO `readings` (id,measurement_id,sensor_type_id,timestamp,X,Y,Z) VALUES (" + r.id + r.measurementId + r.sensorTypeId + r.timestamp + r.X + r.Y + r.Z + ");";
                statement.addBatch(sql);
            }

            statement.executeBatch();
            connection.commit();
            connection.close();

        } catch (SQLException e) {
//            Toast.makeText(getApplicationContext(), "Cannot connect to database! Check if settings are correct!", Toast.LENGTH_LONG).show();
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }

    private void deselectElements(ArrayList<Integer> selected) {
        adapter.deselect(selected);
        for (int index : selected) {
            CheckBox cb = (CheckBox) recyclerView.getLayoutManager().findViewByPosition(index).findViewById(R.id.selectcb);
            cb.setChecked(false);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, VisActivity.class);
        intent.putExtra("measurementId", (long) measurements[position].id);
        startActivity(intent);
    }
}
