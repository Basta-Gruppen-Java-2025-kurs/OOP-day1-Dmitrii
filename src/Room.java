import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Room implements Named {
    private final String name;
    private final ArrayList<SmartDevice> devices = new ArrayList<>();
    private final String[] MENU_OPTIONS = {"Back", "List devices", "Add device", "Remove device", "Move device", "Device settings"};
    private final String NO_DEVICES = "No devices in this room.";

    public void menu() {
        MenuHelper.menuLoop("Room '" + name + "'. Choose action by number:", MENU_OPTIONS,
                new Runnable[] {this::listDevices, this::addDeviceMenu, this::removeDeviceMenu,
                                this::moveDeviceMenu, this::deviceSettingsMenu}, false);
    }

    private void deviceSettingsMenu() {
        MenuHelper.listMenuLoop("Select device by number:", Main.BACK, NO_DEVICES, devices, SmartDevice::menu, true);
    }

    private void moveDeviceMenu() {
        MenuHelper.listMenuLoop("Select device to move:", Main.CANCEL, NO_DEVICES, devices,
                device -> MenuHelper.listMenuLoop("Select room to move device to:", Main.CANCEL, "No rooms found.", Arrays.asList(SmartHome.getInstance().getRooms()),
                        room -> {
                            device.placeInARoom(room);
                            this.devices.remove(device);
                            room.devices.add(device);
        }, true), true);
    }

    private void removeDeviceMenu() {
        MenuHelper.listMenuLoop("Select device to remove:", Main.CANCEL, NO_DEVICES, devices, this::removeDevice, true);
    }

    private void addDeviceMenu() {
        MenuHelper.listMenuLoop("Select device model:", Main.CANCEL, NO_DEVICES, SmartHome.getInstance().getDeviceModels(), model -> {
            SafeInput si = new SafeInput(new Scanner(System.in));
            si.nameInputLoop("Please name the device (empty to cancel): ", "New device placed.", "Device with this name already exists. Try again.", deviceName -> {
                if (devices.stream().anyMatch(d -> d.getId().equals(deviceName))) {
                    return false;
                }
                SmartDevice newDevice = SmartDevice.createNewDevice(deviceName, model);
                if (newDevice != null) {
                    newDevice.placeInARoom(this);
                    devices.add(newDevice);
                    return true;
                } else {
                    System.out.println("Failed to create device");
                    return true;
                }
            });
        }, true);
    }

    public void listDevices() {
        System.out.println("  Total devices: " + devices.size());
        for (SmartDevice device : devices) {
            System.out.println("  - " + device);
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

    public void addDevice(SmartDevice device) {
        System.out.println(!devices.contains(device)&&devices.add(device) ? "Device added." : "This device already exists in this room.");
    }

    public void removeDevice(SmartDevice device) {
        if (devices.contains(device)) {
            System.out.println(devices.remove(device) ? "Device removed." : "Failed to remove device");
        } else {
            System.out.println("Device not found.");
        }
    }

}
