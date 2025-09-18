public class CameraDevice extends SmartDevice {
    public final static DeviceKind deviceKind = new DeviceKind("Camera", CameraDevice.class);
    public CameraDevice(SmartDeviceModel model) {
        super(model);
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
