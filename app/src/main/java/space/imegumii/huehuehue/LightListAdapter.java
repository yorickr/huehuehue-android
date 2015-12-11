package space.imegumii.huehuehue;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

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
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_light, parent, false);
        }

        TextView lampName = (TextView) convertView.findViewById(R.id.lightname);
        Switch onOffSwitch = (Switch) convertView.findViewById(R.id.switch1);
        SeekBar redSlider = (SeekBar) convertView.findViewById(R.id.red);
        SeekBar greenSlider = (SeekBar) convertView.findViewById(R.id.green);
        SeekBar blueSlider = (SeekBar) convertView.findViewById(R.id.blue);
        View circle = (View) convertView.findViewById(R.id.circle);
        float[] hsv = new float[3];
        hsv[0] = (float) l.getHue()/182;
        hsv[1] = (float) l.getSaturation()/254;
        hsv[2] = (float) l.getBrightness()/254;
        final int color = Color.HSVToColor(hsv);

        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        redSlider.setProgress(r);
        greenSlider.setProgress(g);
        blueSlider.setProgress(b);

        final View tempConvertView = convertView;
        final Light tempLight = l;
        final View tempCircle = circle;
        onOffSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println("Clicked on off switch of lamp " + tempLight.getId());
                tempLight.toggleOnState();

            }
        });
        SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                seekBar.setProgress(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                SeekBar redSlider = (SeekBar) tempConvertView.findViewById(R.id.red);
                SeekBar greenSlider = (SeekBar) tempConvertView.findViewById(R.id.green);
                SeekBar blueSlider = (SeekBar) tempConvertView.findViewById(R.id.blue);
                float[] hsv = new float[3];

                int r = redSlider.getProgress();
                int g = greenSlider.getProgress();
                int b = blueSlider.getProgress();
                Color.RGBToHSV(r, g, b, hsv);
                //                Color.RGBToHSV(255,255,0,hsv);

                System.out.println("HSV: " + hsv[0] + " " + hsv[1] + " " + hsv[2]);

                tempLight.setHue(hsv[0] * 182);
                tempLight.setSaturation(hsv[1] * 254);
                tempLight.setBrightness(hsv[2] * 254);

                Drawable background = tempCircle.getBackground();
                ((GradientDrawable) background).setColor(Color.argb(255, r, g, b));

                MainActivity.api.setLightValues(tempLight);
            }
        };

        redSlider.setOnSeekBarChangeListener(changeListener);
        greenSlider.setOnSeekBarChangeListener(changeListener);
        blueSlider.setOnSeekBarChangeListener(changeListener);

        lampName.setText(l.getName());
        onOffSwitch.setChecked(l.isOn());
        Drawable background = circle.getBackground();
        ((GradientDrawable) background).setColor(Color.argb(255, r, g, b));
        return convertView;
    }
}
