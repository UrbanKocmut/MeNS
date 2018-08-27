package si.urban.mens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void SwitchToTest(View view){
        Intent intent = new Intent(this, TestPickerActivity.class);
        startActivity(intent);
    }
    public void SwitchToDataPicker(View view){
        Intent intent = new Intent(this, DataPickerActivity.class);
        startActivity(intent);
    }
}
