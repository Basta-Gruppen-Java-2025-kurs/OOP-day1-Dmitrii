public final class SmartHome {



    private static final String[] MENU_OPTIONS = { "Exit", "Add room", "Remove Room", "Add device", "New device model", "List device models", "List all devices in the house", "Switch state", "Add state", "Remove state" };
    private static SmartHome instance = null;
    private SmartHome() {
    }

    public void initialConfig() {
        addRoom("Room 1");
        addRoom("Room 2");
    }

    public void menu() {
        int choice = MenuHelper.menu("Choose an action:", MENU_OPTIONS);

        // add room
        // remove room
        // room menu
        // create new device model
        // list all device models
        // list all devices in the house
        // switch states
        // add state: select devices, select settings for the devices
        // remove state
    }

    public static SmartHome getInstance() {
        if (instance == null) {
            instance = new SmartHome();
        }
        return instance;
    }

    boolean addRoom(String roomName) {
        return false;
    }

    boolean removeRoom(String roomName) {
        return false;
    }

    boolean addDeviceModel(String deviceModelName) {
        return false;
    }

    void listAllDevices() {

    }

    void listAllDeviceModels() {

    }

    boolean addState(String stateName) {
        return false;
    }

    boolean switchState(String stateName) {
        return false;
    }

    boolean removeState(String stateName) {
        return false;
    }

    void roomMenu() {
        // list rooms for selection
        // run that room's menu
    }

}
