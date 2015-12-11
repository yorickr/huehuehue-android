package space.imegumii.huehuehue;

/**
 * Created by imegumii on 10-12-15.
 */
public final class LightBuilder
{

    String name;
    String type;
    int id;

    double brightness=254;
    double hue=65535;
    double saturation=254;

    boolean isOn;

    public LightBuilder(String name, String type, int id)
    {
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public static LightBuilder GetLightBuilder(String name, String type, int id)
    {
        return new LightBuilder(name, type, id);
    }

    public LightBuilder setOn(boolean isOn)
    {
        this.isOn = isOn;
        return this;
    }

    public LightBuilder setBrightness(double brightness)
    {
        this.brightness = brightness;
        return this;
    }
    public LightBuilder setHue(double hue)
    {
        this.hue = hue;
        return this;
    }
    public LightBuilder setSaturation(double saturation)
    {
        this.saturation = saturation;
        return this;
    }

    public Light getLight()
    {
        return new Light(this);
    }
}
