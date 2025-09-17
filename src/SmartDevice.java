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
        SmartDevice newDevice;
        switch(model.kind) {
            case CameraDevice.deviceKind -> newDevice = new CameraDevice(model);
            case LampDevice.deviceKind -> newDevice = new LampDevice(model);
            default -> newDevice = null;
        }
        if (newDevice == null) {
            System.out.println("Failed to create device. Check that device model is correct");
        } else {
            newDevice.setId(deviceName);
        }
        return newDevice;
    }
}
