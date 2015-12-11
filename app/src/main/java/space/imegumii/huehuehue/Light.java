package space.imegumii.huehuehue;

import android.graphics.Color;

/**
 * Created by imegumii on 9-12-15.
 */
public class Light
{
    String name;
    String type;
    int id;

    double brightness;
    double hue;
    double saturation;

    boolean isOn;

    public Light(String name, String type, int id)
    {
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public Light(LightBuilder builder)
    {
        this.name = builder.name;
        this.type = builder.type;
        this.id = builder.id;
        this.brightness = builder.brightness;
        this.hue = builder.hue;
        this.saturation = builder.saturation;
        this.isOn = builder.isOn;
    }

    public void toggleOnState()
    {
        this.isOn = !isOn;
        MainActivity.api.toggleLight(this);
    }

    public void setColorUsingRGB(int r, int g, int b)
    {
        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);

        this.setHue(hsv[0] * 182);
        this.setSaturation(hsv[1] * 254);
        this.setBrightness(hsv[2] * 254);
        MainActivity.api.setLightValues(this);
    }

    public boolean isOn()
    {

        return isOn;
    }

    public void setIsOn(boolean isOn)
    {

        this.isOn = isOn;
    }

    public String getName()
    {

        return name;
    }

    public String getType()
    {

        return type;
    }

    public int getId()
    {

        return id;
    }

    public double getSaturation()
    {

        return saturation;
    }

    public void setSaturation(double saturation)
    {

        this.saturation = saturation;
    }

    public double getBrightness()
    {

        return brightness;
    }

    public void setBrightness(double brightness)
    {

        this.brightness = brightness;
    }

    public double getHue()
    {
        return hue;
    }

    public void setHue(double hue)
    {
        this.hue = hue;
    }
}
