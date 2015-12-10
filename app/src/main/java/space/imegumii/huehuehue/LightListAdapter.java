package space.imegumii.huehuehue;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imegumii on 9-12-15.
 */
public class LightListAdapter extends ArrayAdapter<Light>
{

    public LightListAdapter(Context context, int resource, List<Light> objects)
    {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Light l = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_light, parent, false);
        }

        TextView lampName = (TextView) convertView.findViewById(R.id.lightname);
        Switch onOffSwitch = (Switch) convertView.findViewById(R.id.switch1);

        lampName.setText(l.getName());
        onOffSwitch.setChecked(l.isOn());


        return convertView;
    }
}
