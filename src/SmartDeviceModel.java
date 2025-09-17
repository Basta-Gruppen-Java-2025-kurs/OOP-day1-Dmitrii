import java.util.ArrayList;

public class SmartDeviceModel implements Named {
    ArrayList<SmartElement> elements = new ArrayList<>();
    String name = "";
    String kind = "";

    public String getName() {
        return name;
    }
}
