package si.urban.mens;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import si.urban.mens.database.AppDatabase;
import si.urban.mens.database.Measurement;

public class TestPickerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_picker);
    }

    public void switchToTest(View view) {
        int measurementTypeId = 0;
        switch (view.getId()){
            case R.id.test1Lbtn:
                measurementTypeId = 0;
                break;
            case R.id.test1Rbtn:
                measurementTypeId = 1;
                break;
            case R.id.test2Lbtn:
                measurementTypeId = 2;
                break;
            case R.id.test2Rbtn:
                measurementTypeId = 3;
                break;
            case R.id.test3Lbtn:
                measurementTypeId = 4;
                break;
            case R.id.test3Rbtn:
                measurementTypeId = 5;
                break;
        }
        SharedPreferences sp = getSharedPreferences("shared_pref",MODE_PRIVATE);
        long testId = sp.getLong("testId",-1);
        if(testId == -1){
            throw new RuntimeException("NO TEST ID");
        }
        Measurement measurement = new Measurement(0,testId,measurementTypeId,System.currentTimeMillis());

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        long measurementId = db.appDao().insertMeasurement(measurement);

        sp.edit().putLong("measurementId",measurementId).apply();


        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    public void switchToMainMenu(View view){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
}
