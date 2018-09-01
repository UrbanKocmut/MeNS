package si.urban.mens;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {
    private EditText ip;
    private EditText port;
    private EditText username;
    private EditText password;
    private EditText scheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ip = (EditText) findViewById(R.id.ipet);
        port = (EditText) findViewById(R.id.portet);
        username = (EditText) findViewById(R.id.usret);
        password = (EditText) findViewById(R.id.pwdet);
        scheme = (EditText) findViewById(R.id.schmet);

    }

    public void saveData(View view) {
        if (ip.getText().toString().isEmpty() ||
                port.getText().toString().isEmpty() ||
                username.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() ||
                scheme.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill all forms!",
                    Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences sp = getSharedPreferences("shared_pref",MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.putString("IP",ip.getText().toString());
            ed.putString("PORT",port.getText().toString());
            ed.putString("USERNAME",username.getText().toString());
            ed.putString("PASSWORD",password.getText().toString());
            ed.putString("SCHEME",scheme.getText().toString());
            ed.apply();
            switchToMainMenu(view);
        }
    }

    public void switchToMainMenu(View view){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
}
