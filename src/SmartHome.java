import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public final class SmartHome {

    private ArrayList<Room> rooms = new ArrayList();
    private ArrayList<SmartDeviceModel> models = new ArrayList();

    private static final String[] MENU_OPTIONS = { "Exit", "Add room", "Remove Room", "New device model", "List device models", "List all devices in the house", "Switch state", "Add state", "Remove state" };
    private static SmartHome instance = null;
    private SmartHome() {
    }

    public void initialConfig() {
        addRoom("Room 1");
        addRoom("Room 2");
        SmartDeviceModel camera = new SmartDeviceModel();
        camera.name = "Super Camera";
        addDeviceModel(camera);
        SmartDeviceModel thermo = new SmartDeviceModel();
        thermo.name = "Mega Thermometer";
        addDeviceModel(thermo);
    }

    public void menu() {
        int choice = -1;
        do {
            choice = MenuHelper.menu("Choose an action:", MENU_OPTIONS);
            switch(choice) {
                case 1 -> addRoomMenu();
                case 2 -> removeRoomMenu();
                case 3 -> newDeviceModelMenu();
                case 4 -> listAllDeviceModels();
                case 5 -> listAllDevices();
                case 6 -> switchStateMenu();
                case 7 -> addStateMenu();
                case 8 -> removeStateMenu();
            }
        } while(choice > 0);
        System.out.println("Good bye.");
    }

    private void removeStateMenu() {
        //select state to remove or exit
    }

    Room[] getRooms() {
        return rooms.toArray(Room[]::new);
    }

    private void addStateMenu() {
        // input new state name
        // create the state if not exists
    }

    private void switchStateMenu() {
        //
    }

    private void newDeviceModelMenu() {
    }

    private void removeRoomMenu() {
    }

    private void addRoomMenu() {
        SafeInput si = new SafeInput(new Scanner(System.in));
    }

    public static SmartHome getInstance() {
        if (instance == null) {
            instance = new SmartHome();
        }
        return instance;
    }

    boolean addRoom(String roomName) {
        if (rooms.stream().anyMatch(r->r.getName().equals(roomName))) {
            System.out.println("Room '" + roomName + "' already exists");
            return false;
        }
        return rooms.add(new Room(roomName));
    }

    boolean removeRoom(String roomName) {
        return false;
    }

    boolean addDeviceModel(SmartDeviceModel deviceModel) {
        if (models.contains(deviceModel)) {
            return false;
        }
        models.add(deviceModel);
        return true;
    }

    void listAllDevices() {
        for (Room room : rooms) {
            System.out.println("* Room '" + room.getName() + "':");
            room.listDevices();
        }
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
        ArrayList<String> roomList = new ArrayList();
        roomList.add("Exit");
        for (Room room : rooms) {
            roomList.add(room.getName());
        }
        int choice = MenuHelper.menu("Select room", (String[])  roomList.toArray());
        if (choice > 1) {
            rooms.get(choice-1).menu();
        }
    }

}
