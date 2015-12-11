package space.imegumii.huehuehue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        refreshViews();

    }

    public void refreshViews()
    {
        EditText hostname = (EditText) findViewById(R.id.hostname);
        EditText port = (EditText) findViewById(R.id.port);
        TextView apikeyTextview = (TextView) findViewById(R.id.apikeyfield);
        hostname.setText(MainActivity.settings.getString("hostname", "hue.imegumii.space"));
        port.setText(String.valueOf(MainActivity.settings.getInt("port", 80)));
        apikeyTextview.setText(MainActivity.settings.getString("apikey", ""));
    }

    public void register_clicked(View v)
    {

        MainActivity.api.registerName(new TaskListener()
        {
            @Override
            public void onFinished(String result)
            {
                System.out.println(result);
                refreshViews();
            }
        });
    }

    public void savebutton_clicked(View v)
    {
        SharedPreferences.Editor editor = MainActivity.settings.edit();
        EditText hostname = (EditText) findViewById(R.id.hostname);
        EditText port = (EditText) findViewById(R.id.port);
        editor.putString("hostname", hostname.getText().toString());
        editor.putInt("port", Integer.parseInt(port.getText().toString()));
        editor.commit();
        setResult(MainActivity.RETURN_VALUE_SETTINGS);
        refreshViews();
    }
}
