import java.util.ArrayList;
import java.util.Arrays;

public class DeviceKind implements Named {
    public static final ArrayList<DeviceKind> AVAILABLE_KINDS = new ArrayList<>(Arrays.asList(CameraDevice.deviceKind, LampDevice.deviceKind));
    private final String name;
    private final Class<? extends SmartDevice> deviceClass;

    DeviceKind(String name, Class<? extends SmartDevice> deviceClass) {
        this.name = name;
        this.deviceClass = deviceClass;
    }

    public static DeviceKind getDeviceKind(String kindName) {
        return AVAILABLE_KINDS.stream().filter(k -> k.getName().equals(kindName)).findFirst().orElse(null);
    }

    Class<? extends SmartDevice> produceDevice() {
        return deviceClass;
    }

    @Override
    public String getName() {
        return name;
    }
}
