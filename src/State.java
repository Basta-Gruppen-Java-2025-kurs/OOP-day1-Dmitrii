import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class State {
    String name = "";
    State(String name) {
        this.name = name;
    }
    ArrayList<DeviceState> deviceStates = new ArrayList();

    public static State deserialize(String name, Scanner scanner) {
        //TODO add actual state reading
        scanner.nextLine();
        return new State(name);
    }

    public void serialize(PrintWriter f) {
        f.println("State: (" + this.name + ") " + "not implemented yet");
    }

    public void apply() {
        for (DeviceState ds : deviceStates) {
            ds.device.applyState(ds.state);
        }
    }

    public void save() {
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
