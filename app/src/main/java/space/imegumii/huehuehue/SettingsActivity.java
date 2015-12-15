package space.imegumii.huehuehue;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        refreshViews();

        Spinner dropdown = (Spinner)findViewById(R.id.weatherspinner);
        String[] items = new String[]{"5 minutes","15 minutes" , "30 minutes" , "60 minutes"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                SharedPreferences.Editor editor = MainActivity.settings.edit();
                switch (position)
                {
                    //5 min
                    case 0:
                        System.out.println("Weathertime to 5");
                        editor.putInt("weathertime", 5);
                        editor.commit();
                        break;
                    //15 min
                    case 1:
                        System.out.println("Weathertime to 15");
                        editor.putInt("weathertime", 15);
                        editor.commit();
                        break;
                    //30 min
                    case 2:
                        System.out.println("Weathertime to 30");
                        editor.putInt("weathertime", 30);
                        editor.commit();
                        break;
                    //60 min
                    case 3:
                        System.out.println("Weathertime to 60");
                        editor.putInt("weathertime", 60);
                        editor.commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

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

    public void gif_clicked(View v)
    {
        System.out.println("gif_clicked");
        WebView gif = (WebView) findViewById(R.id.gifview);
        if (gif.getVisibility() == View.VISIBLE)
        {
            gif.setVisibility(View.INVISIBLE);
        }
        else
        {
            gif.loadUrl("file:///android_asset/huehuehue.gif");
            gif.setVisibility(View.VISIBLE);
        }

    }

    public void savebutton_clicked(View v)
    {
        EditText hostname = (EditText) findViewById(R.id.hostname);
        EditText port = (EditText) findViewById(R.id.port);
        SharedPreferences.Editor editor = MainActivity.settings.edit();
        editor.putString("hostname", hostname.getText().toString());
        editor.putInt("port", Integer.parseInt(port.getText().toString()));
        editor.commit();
        setResult(MainActivity.RETURN_VALUE_SETTINGS);
        refreshViews();
    }
}
