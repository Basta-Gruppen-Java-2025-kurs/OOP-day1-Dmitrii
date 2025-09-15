import java.lang.reflect.Array;
import java.util.ArrayList;

public class State {
    String name = "";
    State(String name) {
        this.name = name;
    }
    ArrayList<DeviceState> deviceStates = new ArrayList();

    void apply() {
        for (DeviceState ds : deviceStates) {
            ds.device.applyState(ds.state);
        }
    }

    void save() {
        for (Room room : SmartHome.getInstance().getRooms()) {
            for (SmartDevice device : room.getDevices()) {
                addDeviceState(device);
            }
        }
    }

    void addDeviceState(SmartDevice device) {
        //TODO: check that device is already in the list
        DeviceState ds = new DeviceState();
        ds.device = device;
        ds.state = device.getState();
        deviceStates.add(ds);
    }

    class DeviceState {
        SmartDevice device;
        String state;
    }
}
