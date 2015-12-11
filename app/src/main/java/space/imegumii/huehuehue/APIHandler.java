package space.imegumii.huehuehue;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by imegumii on 9-12-15.
 */
public class APIHandler
{
    public static String apikey;
    private String hostname;
    private int port;
    private MainActivity parent;

    public APIHandler(MainActivity parent, String hostname, int port)
    {
        this.parent = parent;
        this.hostname = hostname;
        this.port = port;
        if (MainActivity.settings.getString("apikey", null) != null)
        {
            apikey = MainActivity.settings.getString("apikey", "");
        }
    }

    public void setLightsUsingWeatherID(int id)
    {
        switch ((int)Math.floor(id / 100))
        {
            //Thunderstorm
            case 2:
                System.out.println("Thunder");
                for (Light l : parent.itemsAdapter.getLights())
                {
                    l.setColorUsingRGB(255, 255, 0);
                }
                break;
            //Drizzle
            case 3:
                System.out.println("Drizzle");
                for (Light l : parent.itemsAdapter.getLights())
                {
                    l.setColorUsingRGB(157, 255, 218);
                }
                break;
            //Rain
            case 5:
                System.out.println("Rain");
                for (Light l : parent.itemsAdapter.getLights())
                {
                    l.setColorUsingRGB(107, 144, 255);
                }
                break;
            //Snow
            case 6:
                System.out.println("Snow");
                for (Light l : parent.itemsAdapter.getLights())
                {
                    l.setColorUsingRGB(255, 255, 255);
                }
                break;
            //Clouds
            case 8:
                if (id == 800)
                {
                    System.out.println("Clear sky");
                    for (Light l : parent.itemsAdapter.getLights())
                    {
                        l.setColorUsingRGB(0, 0, 0);
                    }
                    break;
                }
                System.out.println("Clouds");
                for (Light l : parent.itemsAdapter.getLights())
                {
                    l.setColorUsingRGB(97, 96, 97);
                }
                break;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                parent.itemsAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getCurrentWeather()
    {
        new NetworkHandler(NetworkHandler.Requests.Get, new TaskListener()
        {
            @Override
            public void onFinished(String result)
            {
                try
                {
                    JSONObject o = new JSONObject(result);
                    JSONArray weather = o.getJSONArray("weather");
                    int weatherId = weather.getJSONObject((int) Math.floor(Math.random() * weather.length())).getInt("id");
                    System.out.println(weatherId);
                    setLightsUsingWeatherID(weatherId);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }).execute("http://api.openweathermap.org/data/2.5/weather?id=2758401&appid=35ecaf59475b958644a56e07b6ac0700");
    }

    public void registerName(final TaskListener taskListener)
    {
        new NetworkHandler(NetworkHandler.Requests.Post, new TaskListener()
        {
            @Override
            public void onFinished(String result)
            {
                try
                {
                    String temp = result.replace("[", "").replace("]", "");
                    JSONObject o = new JSONObject(temp);
                    apikey = o.getJSONObject("success").getString("username");
                    SharedPreferences.Editor editor = MainActivity.settings.edit();
                    editor.putString("apikey", apikey);
                    editor.commit();
                    System.out.println(apikey);
                    taskListener.onFinished("ok");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).setJson("{\"devicetype\":\"Android#1\"}").execute("http://" + hostname + ":" + port + "/api/");

    }

    public void registerName()
    {
        new NetworkHandler(NetworkHandler.Requests.Post, new TaskListener()
        {
            @Override
            public void onFinished(String result)
            {
                try
                {
                    String temp = result.replace("[", "").replace("]", "");
                    JSONObject o = new JSONObject(temp);
                    apikey = o.getJSONObject("success").getString("username");
                    SharedPreferences.Editor editor = MainActivity.settings.edit();
                    editor.putString("apikey", apikey);
                    editor.commit();
                    System.out.println(apikey);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).setJson("{\"devicetype\":\"Android#1\"}").execute("http://" + hostname + ":" + port + "/api/");

    }

    public void toggleLight(Light light)
    {
        JSONObject o = new JSONObject();
        try
        {
            o.put("on", light.isOn);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        new NetworkHandler(NetworkHandler.Requests.Put, null).setJson(o.toString()).execute("http://" + hostname + ":" + port + "/api/" + apikey + "/lights/" + light.id + "/state/");
    }

    public void setLightValues(Light light)
    {
        JSONObject o = new JSONObject();
        try
        {
            o.put("bri", Math.floor(light.getBrightness()));
            o.put("sat", Math.floor(light.getSaturation()));
            o.put("hue", Math.floor(light.getHue()));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        new NetworkHandler(NetworkHandler.Requests.Put, null).setJson(o.toString()).execute("http://" + hostname + ":" + port + "/api/" + apikey + "/lights/" + light.id + "/state/");
    }

    public void setInstantLightValues(Light light)
    {
        JSONObject o = new JSONObject();
        try
        {
            o.put("bri", Math.floor(light.getBrightness()));
            o.put("sat", Math.floor(light.getSaturation()));
            o.put("hue", Math.floor(light.getHue()));
            o.put("transitiontime", 0);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        new NetworkHandler(NetworkHandler.Requests.Put, null).setJson(o.toString()).execute("http://" + hostname + ":" + port + "/api/" + apikey + "/lights/" + light.id + "/state/");
    }

    public void getAllLights()
    {
        new NetworkHandler(NetworkHandler.Requests.Get, new TaskListener()
        {
            @Override
            public void onFinished(String result)
            {
                try
                {
                    JSONObject o = new JSONObject(result);
                    Iterator<String> it = o.keys();
                    parent.itemsAdapter.clear();
                    while (it.hasNext())
                    {
                        String next = it.next();
                        JSONObject lamp = o.getJSONObject(next);
                        JSONObject state = lamp.getJSONObject("state");
                        System.out.println(next);
                        LightBuilder lb = LightBuilder.GetLightBuilder(lamp.getString("name"), lamp.getString("type"), Integer.parseInt(next));
                        if (!state.isNull("hue"))
                        {
                            lb.setHue(state.getDouble("hue"));

                        }
                        if (!state.isNull("sat"))
                        {
                            lb.setSaturation(state.getDouble("sat"));
                        }
                        if (!state.isNull("bri"))
                        {
                            lb.setBrightness(state.getDouble("bri"));
                        }
                        lb.setOn(state.getBoolean("on"));
                        parent.itemsAdapter.add(lb.getLight());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute("http://" + hostname + ":" + port + "/api/" + apikey + "/lights");
    }
}
