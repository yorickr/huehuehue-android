package space.imegumii.huehuehue;

import org.json.JSONObject;

import java.security.Key;
import java.util.Iterator;

/**
 * Created by imegumii on 9-12-15.
 */
public class APIHandler {
    private String hostname;
    private int port;
    private MainActivity parent;

    public static String apikey = "29f72b7e8fec279a6756e17f2c2ee21";

    public APIHandler(MainActivity parent, String hostname, int port)
    {
        this.parent = parent;
        this.hostname = hostname;
        this.port = port;
    }

    public void registerName()
    {
        new NetworkHandler(NetworkHandler.Requests.Post, new NetworkHandler.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    String temp = result.replace("[", "").replace("]", "");
                    JSONObject o = new JSONObject(temp);
                    apikey = o.getJSONObject("success").getString("username");
                    System.out.println(apikey);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).SetJson("{\"devicetype\":\"Android#1\"}").execute("http://"+hostname + ":"  + port + "/api/");

    }

    public void getAllLights()
    {
        new NetworkHandler(NetworkHandler.Requests.Get, new NetworkHandler.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    JSONObject o = new JSONObject(result);
                    Iterator<String> it = o.keys();
                    parent.itemsAdapter.clear();
                    while (it.hasNext()) {
                        String next = it.next();

                        System.out.println(next);
                        parent.itemsAdapter.add(next);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).execute("http://"+hostname + ":"  + port + "/api/" + apikey + "/lights");
    }
}
