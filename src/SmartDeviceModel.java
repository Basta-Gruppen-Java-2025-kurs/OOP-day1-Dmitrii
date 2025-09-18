import java.util.ArrayList;

public class SmartDeviceModel implements Named {
    ArrayList<SmartElement> elements = new ArrayList<>();
    String name;
    DeviceKind kind;

    SmartDeviceModel(String name, DeviceKind kind) {
        this.name = name;
        this.kind = kind;
    }

    public String getName() {
        return name;
    }
}
