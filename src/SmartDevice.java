public abstract class SmartDevice implements Named {
    private final SmartDeviceModel model;
    private String id;
    private Room room = null;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public SmartDeviceModel getModel() {
        return model;
    }

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

    public SmartDevice(SmartDeviceModel model) {
        this.model = model;
    }

    public abstract void menu();

    public abstract String getState();

    public abstract void applyState(String state);

    public static SmartDevice createNewDevice(String deviceName, SmartDeviceModel model) {
        SmartDevice newDevice = null;
        try {
            newDevice = model.kind.produceDevice().getConstructor(SmartDeviceModel.class).newInstance(model);
        } catch (Exception e) {
            System.out.println("Error creating device: " + e);
        }
        if (newDevice == null) {
            System.out.println("Failed to create device. Check that device model is correct");
        } else {
            newDevice.setId(deviceName);
        }
        return newDevice;
    }
}
