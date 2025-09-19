public class CameraDevice extends SmartDevice {
    public final static DeviceKind deviceKind = new DeviceKind("Camera", CameraDevice.class);
    public CameraDevice(SmartDeviceModel model, String id) {
        super(model, id);
    }

    @Override
    public void menu() {
        System.out.println(this.getId() + " device menu ");
    }

    @Override
    public String getState() {
        return "";
    }

    @Override
    public void applyState(String state) {

    }

}
