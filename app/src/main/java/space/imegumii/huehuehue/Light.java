package space.imegumii.huehuehue;

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
