package space.imegumii.huehuehue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    public static final String PREFS_NAME = "HuehuehuePreferences";
    public static SharedPreferences settings;
    public static APIHandler api;

    public static final int RETURN_VALUE_SETTINGS = 1;

    ListView lv;
    ArrayList<Light> lights;
    //    ArrayList<String> lights;
    LightListAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init settings before API
        settings = getSharedPreferences(PREFS_NAME, 0);
        api = new APIHandler(this, settings.getString("hostname", "hue.imegumii.space"), settings.getInt("port", 80));


        lights = new ArrayList<>();

        lv = (ListView) findViewById(R.id.lightlistView);
        itemsAdapter = new LightListAdapter(this, R.layout.item_light, lights);

        lv.setAdapter(itemsAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                System.out.println("Clicked" + id);
            }
        });

        ToggleButton tb = (ToggleButton) findViewById(R.id.togglebutton);
        final ToggleButton tempTb = tb;
        final ArrayList<Light> tempLights = lights;
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked)
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        while (tempTb.isChecked())
                        {
                            for (Light light : tempLights)
                            {
                                light.setHue(Math.random() * 65534);
                                light.setBrightness(Math.random() * 254);
                                light.setSaturation(254);
                                MainActivity.api.setInstantLightValues(light);
                                try
                                {
                                    Thread.sleep(10);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                new Handler(Looper.getMainLooper()).post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        itemsAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            try
                            {
                                Thread.sleep(500);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

            }
        });

        api.getAllLights();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void createNewAPI()
    {
        System.out.println(settings.getString("hostname", "hue.imegumii.space") + " " + settings.getInt("port", 80));
        api = new APIHandler(this, settings.getString("hostname", "hue.imegumii.space"), settings.getInt("port", 80));
    }

    public void weather_clicked(View v)
    {
        api.getCurrentWeather();
    }

    public void disco_clicked(View v)
    {
        ToggleButton discoToggle = (ToggleButton) findViewById(R.id.togglebutton);
        if (discoToggle.isChecked())
        {
            discoToggle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_circle_outline_black_24dp, 0,0,0);
            return;
        }
        discoToggle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_circle_filled_black_24dp, 0,0,0);
    }


    public void refresh_clicked(View v)
    {

        api.getAllLights();
        //            api.registerName();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case RETURN_VALUE_SETTINGS:
                createNewAPI();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId())
        {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, RETURN_VALUE_SETTINGS);
        }

        return super.onOptionsItemSelected(item);
    }
}
