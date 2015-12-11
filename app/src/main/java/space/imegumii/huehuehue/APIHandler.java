package space.imegumii.huehuehue;

import android.content.SharedPreferences;

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

    public void registerName()
    {
        new NetworkHandler(NetworkHandler.Requests.Post, new NetworkHandler.TaskListener()
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
        new NetworkHandler(NetworkHandler.Requests.Put, null).setJson("{\"on\":\"" + light.isOn + "\"}").execute("http://" + hostname + ":" + port + "/api/" + apikey + "/lights/" + light.id + "/state/");
    }

    public void setLightValues(Light light)
    {
        JSONObject o = new JSONObject();
        try
        {
            o.put("bri", light.getBrightness());
            o.put("sat", light.getSaturation());
            o.put("hue", light.getHue());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        new NetworkHandler(NetworkHandler.Requests.Put, null).setJson(o.toString()).execute("http://" + hostname + ":" + port + "/api/" + apikey + "/lights/" + light.id + "/state/");
    }

    public void getAllLights()
    {
        new NetworkHandler(NetworkHandler.Requests.Get, new NetworkHandler.TaskListener()
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
