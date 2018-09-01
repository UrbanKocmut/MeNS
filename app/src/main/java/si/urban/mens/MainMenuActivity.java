package si.urban.mens;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SharedPreferences sp = getSharedPreferences("shared_pref",MODE_PRIVATE);
        long testId = sp.getLong("testId",-1l);
        if(testId == 2390){
            ((Button) findViewById(R.id.settingsbtn)).setVisibility(View.VISIBLE);
        }

    }

    public void SwitchToTest(View view){
        Intent intent = new Intent(this, TestPickerActivity.class);
        startActivity(intent);
    }


    public void SwitchToDataPicker(View view){
        Intent intent = new Intent(this, DataPickerActivity.class);
        startActivity(intent);
    }

    public void SwitchToSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
