import java.util.ArrayList;
import java.util.Scanner;

public class Room implements Named {
    private final String name;
    private final ArrayList<SmartDevice> devices = new ArrayList<>();
    private final String[] MENU_OPTIONS = {"Back", "List devices", "Add device", "Remove device", "Move device", "Device settings"};

    public void menu() {
        MenuHelper.menuLoop("Room '" + name + "'. Choose action by number:", MENU_OPTIONS,
                new Runnable[] {this::listDevices, this::addDeviceMenu, this::removeDeviceMenu, this::moveDeviceMenu, this::deviceSettingsMenu});
    }

    private void deviceSettingsMenu() {
    }

    private void moveDeviceMenu() {
    }

    private void removeDeviceMenu() {
        //
    }

    private void addDeviceMenu() {
        ArrayList<SmartDeviceModel> modelList = SmartHome.getInstance().getDeviceModels();
        if (modelList.isEmpty()) {
            System.out.println("No device models available.");
            return;
        }
        String [] addMenuContent = new String[modelList.size() + 1];
        addMenuContent[0] = "Cancel";
        for (int i = 0; i < modelList.size(); i++) {
            addMenuContent[i + 1] = modelList.get(i).name;
        }

        // select device model
        int choice = MenuHelper.menu("Select device model: ", addMenuContent);
        if (choice == 0) {
            return;
        }
        // name the device
        SafeInput si = new SafeInput(new Scanner(System.in));
        while(true) {
            String deviceName = si.nextLine("Please name the device (empty to cancel): ");
            if (devices.stream().anyMatch(d -> d.getId().equals(deviceName))) {
                System.out.println("Device with this name already exists. Try again.");
            } else {
                SmartDevice newDevice = SmartDevice.createNewDevice(deviceName, modelList.get(choice - 1));
                if (newDevice != null) {
                    newDevice.placeInARoom(this);
                    System.out.println("New device placed.");
                }
                return;
            }
        }
    }

    public void listDevices() {
        System.out.println("  Total devices: " + devices.size());
        for (SmartDevice device : devices) {
            System.out.println("  - " + device.toString());
        }
    }

    public String getName() {
        return this.name;
    }

    Room(String name) {
        this.name = name;
    }

    public ArrayList<SmartDevice> getDevices() {
        return devices;
    }

    public boolean addDevice(SmartDevice device) {
        if (devices.contains(device)) {
            System.out.println("This device already exists in this room.");
            return false;
        }
        return devices.add(device);
    }

    public boolean removeDevice(SmartDevice device) {
        if (devices.contains(device)) {
            devices.remove(device);
            return true;
        }
        return false;
    }

}
