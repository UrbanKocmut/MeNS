package si.urban.mens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TestPickerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_picker);
    }

    public void switchToTest(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }
}
