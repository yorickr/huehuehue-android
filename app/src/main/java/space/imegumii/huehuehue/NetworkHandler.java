package space.imegumii.huehuehue;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by imegumii on 9-12-15.
 */
public class NetworkHandler extends AsyncTask<String, Integer, String>
{

    private TaskListener taskListener;
    private Requests request;
    private String json;

    public NetworkHandler(Requests r, TaskListener listener)
    {
        this.taskListener = listener;
        this.request = r;

    }

    public NetworkHandler setJson(String s)
    {
        this.json = s;
        return this;
    }

    private String Get(URL url)
    {
        HttpURLConnection urlConnection = null;
        String json = "";
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            br.close();
            json = sb.toString();


        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
        return json;
    }

    private String Post(URL url)
    {
        HttpURLConnection urlConnection = null;
        String json = "";
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(this.json);
            writer.close();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            br.close();
            json = sb.toString();


        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
        return json;
    }

    private String Put(URL url)
    {
        HttpURLConnection urlConnection = null;
        String json = "";
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("PUT");


            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(this.json);
            writer.close();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            br.close();
            json = sb.toString();


        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
        return json;
    }

    protected void onPostExecute(String result)
    {
        System.out.println(result);
        if (taskListener != null)
        {
            this.taskListener.onFinished(result);
        }
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            URL url = new URL(params[0]);
            switch (this.request)
            {
                case Get:
                    return Get(url);
                case Post:
                    return Post(url);
                case Put:
                    return Put(url);
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public enum Requests
    {
        Post, Get, Put
    }

    public interface TaskListener
    {
        public void onFinished(String result);
    }
}
