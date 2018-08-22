package si.urban.mens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class PinActivity extends Activity {

    EditText pinEntryEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        pinEntryEt = (EditText) findViewById(R.id.pinEntryEt);
        pinEntryEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            switchToTest(v);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

    }

    public void switchToTest(View view) {
        Intent intent = new Intent(this, TestPickerActivity.class);
        startActivity(intent);
    }
}
