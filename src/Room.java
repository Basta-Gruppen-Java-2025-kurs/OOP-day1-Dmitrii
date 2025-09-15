import java.util.ArrayList;

public class Room {
    private String name;
    private ArrayList<SmartDevice> devices;
    private final String[] MENU_OPTIONS = {"Exit", "List devices", "Add device"};

    public void menu() {
        // print number of devices
        //
    }

    public String getName() {
        return name;
    }

    Room(String name) {
        this.name = name;
    }

    public ArrayList<SmartDevice> getDevices() {
        return devices;
    }

    public boolean addDevice(SmartDevice device) {
        return false;
    }

    public boolean removeDevice(SmartDevice device) {
        if (devices.contains(device)) {
            devices.remove(device);
            return true;
        }
        return false;
    }

}
