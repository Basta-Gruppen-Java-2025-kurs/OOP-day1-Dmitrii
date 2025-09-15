public abstract class SmartDevice {
    SmartDeviceModel model;
    public abstract String getId();
    public abstract void setId(String name);
    public abstract SmartDeviceModel getModel();

    public SmartDevice(SmartDeviceModel model) {
        this.model = model;
    };
}
