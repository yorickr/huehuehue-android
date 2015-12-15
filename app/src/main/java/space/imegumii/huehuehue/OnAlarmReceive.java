package space.imegumii.huehuehue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by imegumii on 12-12-15.
 */
public class OnAlarmReceive extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        System.out.println("Update weather");
        MainActivity.api.getCurrentWeather();
    }
}
