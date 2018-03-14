package test;

/**
 * Created by cliu_yjs15 on 2018/3/7.
 * name string, price FLOAT,processor FLOAT ,screen FLOAT ,backup FLOAT ,cam FLOAT
 */
public class SmartPhone {
    private String name;
    private float price;
    private float processor;
    private float screen;
    private float backup;
    private float cam;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getProcessor() {
        return processor;
    }

    public void setProcessor(float processor) {
        this.processor = processor;
    }

    public float getScreen() {
        return screen;
    }

    public void setScreen(float screen) {
        this.screen = screen;
    }

    public float getBackup() {
        return backup;
    }

    public void setBackup(float backup) {
        this.backup = backup;
    }

    public float getCam() {
        return cam;
    }

    public void setCam(float cam) {
        this.cam = cam;
    }
}
