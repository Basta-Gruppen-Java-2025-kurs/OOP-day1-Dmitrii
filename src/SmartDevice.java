import java.lang.reflect.Constructor;

public abstract class SmartDevice implements Named {
    private final SmartDeviceModel model;
    protected final String id;
    private Room room = null;
    public String getId() {
        return id;
    }
    public SmartDeviceModel getModel() {
        return model;
    }

    protected String[] DEFAULT_MENU = {"Status", "Turn on", "Turn off", "Power consumption", "Components"};

    @Override
    public String getName() {
        return id;
    }

    @Override
    public String toString() {
        return id + " (" + model.getName() + " — " + model.kind.getName() + ")";
    }

    public void placeInARoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return this.room;
    }

    public SmartDevice(SmartDeviceModel model, String id) {
        this.model = model;
        this.id = id;
    }

    public abstract void menu();

    public abstract String getState();

    public abstract void applyState(String state);

    public static SmartDevice createNewDevice(String deviceName, SmartDeviceModel model) {
        SmartDevice newDevice = null;
        try {
            newDevice = model.kind.produceDevice(model, deviceName);
            if (newDevice == null) {
                System.out.println("Failed to create device. Check that device model is correct");
            }
        } catch (Exception e) {
            System.out.println("Error creating device: " + e);
        }
        return newDevice;
    }
}
