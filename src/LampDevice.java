public class LampDevice extends SmartDevice {
    public final static DeviceKind deviceKind = new DeviceKind("Lamp", LampDevice.class);
    public LampDevice(SmartDeviceModel model, String id) {
        super(model, id);
    }

    @Override
    public void menu() {

    }

    @Override
    public String getState() {
        return "";
    }

    @Override
    public void applyState(String state) {

    }
}
