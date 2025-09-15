import java.util.ArrayList;

public class Room {
    private String name;
    private ArrayList<SmartDevice> devices = new ArrayList();
    private final String[] MENU_OPTIONS = {"Exit", "List devices", "Add device", "Remove device", "Move device", "Device settings"};

    public void menu() {
        int choice = MenuHelper.menu("Room '" + name + "'. Choose action by number:",MENU_OPTIONS);
        switch(choice) {
            case 1 -> listDevices();
            case 2 -> addDeviceMenu();
            case 3 -> removeDeviceMenu();
            case 4 -> moveDeviceMenu();
            case 5 -> deviceSettingsMenu();
        }
    }

    private void deviceSettingsMenu() {
    }

    private void moveDeviceMenu() {
    }

    private void removeDeviceMenu() {
    }

    private void addDeviceMenu() {

    }

    public void listDevices() {
        System.out.println("  Total devices: " + devices.size());
        for (SmartDevice device : devices) {
            System.out.println("  - " + device.toString());
        }
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
