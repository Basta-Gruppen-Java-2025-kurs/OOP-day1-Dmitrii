public abstract class SmartDevice {
    private SmartDeviceModel model;
    private String id;
    private Room room = null;
    public String getId() {
        return id;
    };
    public void setId(String id) {
        this.id = id;
    }
    public SmartDeviceModel getModel() {
        return model;
    }

    public void placeInARoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return this.room;
    }

    public SmartDevice(SmartDeviceModel model) {
        this.model = model;
    };

    public String getState() {
        return "Not implemented yet";
    }

    public void applyState(String state) {
        // TODO: apply state
    }
}
