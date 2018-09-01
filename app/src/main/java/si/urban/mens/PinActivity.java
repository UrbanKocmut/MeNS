package si.urban.mens;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import si.urban.mens.database.AppDatabase;
import si.urban.mens.database.Test;

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
                            switchToMainMenu(v);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

    }

    public void switchToMainMenu(View view) {

        String PIN = pinEntryEt.getText().toString();
        if(PIN.equals(""))
            return;

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        Test test = new Test(Integer.valueOf(PIN),System.currentTimeMillis());
        db.appDao().insertTests(test);
        AppDatabase.fillDatabase();
        SharedPreferences.Editor editor = getSharedPreferences("shared_pref",MODE_PRIVATE).edit();
        editor.putLong("testId",Integer.valueOf(PIN));
        editor.apply();

        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }



}
