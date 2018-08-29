package si.urban.mens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import si.urban.mens.database.AppDatabase;
import si.urban.mens.database.Measurement;

public class DataPickerActivity extends Activity implements DataPickerRecyclerViewAdapter.ItemClickListener {

    private DataPickerRecyclerViewAdapter adapter;
    private AppDatabase db;
    private Measurement[] measurements;

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
        RecyclerView recyclerView = findViewById(R.id.datarv);
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

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, VisActivity.class);
        intent.putExtra("measurementId", (long) measurements[position].id );
        startActivity(intent);
    }
}
