import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SmartHome {
    //region Header
    private final String SAVE_FILENAME = "SavedState.txt";

    private final ArrayList<Room> rooms = new ArrayList<>();
    private final ArrayList<SmartDeviceModel> models = new ArrayList<>();
    private final ArrayList<State> states = new ArrayList<>();

    private static final String[] MENU_OPTIONS = { " ⍇\tExit", "➕⛶\tAdd room", "❌⛶\tRemove Room", "☰⛶\tRoom Menu", "➕⚡\tNew device model", "☰⚡\tList device models", "☰⚙\tList all devices in the house", "☰⛿\tSwitch state", "➕⛿\tAdd state", "❌⛿\tRemove state", "\uD83D\uDCBE\tSave to file" };
    private static SmartHome instance = null;
    private SmartHome() {
    }
    //endregion

    //region Serialization/Deserialization
    public void saveToFile() {
        PrintWriter saveFile;
        try {
            File f = new File(SAVE_FILENAME);
            if (!f.exists() && !f.createNewFile()) {
                throw new RuntimeException("Failed to create file");
            }
            saveFile = new PrintWriter(f);
        } catch (Exception e) {
            System.out.println("Couldn't open a save file '" + SAVE_FILENAME + "' for writing: "+ e);
            return;
        }
        try {
            // save all models
            for (SmartDeviceModel model : models) {
                saveFile.println("Model: " + "(" + model.kind + ") " + model.name);
            }
            // save each room with its devices
            for (Room room : rooms) {
                saveFile.println("Room: " + room.getName());
                for (SmartDevice device : room.getDevices()) {
                    saveFile.println("Device: (" + device.getModel().name + ") " + device.getId());
                }
            }
            // save all states
            for (State state : states) {
                state.serialize(saveFile);
            }
            // save current state
            State currentState = new State("current");
            currentState.save();
            currentState.serialize(saveFile);
            System.out.println("Everything saved to " + SAVE_FILENAME);
        } finally {
            saveFile.close();
        }
    }

    private void loadFromFile() throws FileNotFoundException {
        System.out.println("Reading saved home from " + SAVE_FILENAME);
        Scanner fileReader = new Scanner(new File(SAVE_FILENAME));
        Room currentRoom = null;
        while (fileReader.hasNext()) {
            String what = fileReader.next();
            try {
                if (!what.endsWith(":")) {
                    throw new RuntimeException("Missing colon");
                }
                switch(what.toLowerCase(Locale.ROOT)) {
                    case "room:" -> {
                        addRoom(fileReader.nextLine().trim());
                        currentRoom = rooms.getLast();
                        System.out.println("Room '" + currentRoom.getName() + "' added.");
                    }
                    case "model:" -> {
                        String modelKind = SafeInput.readBetweenBraces(fileReader);
                        String modelName = fileReader.nextLine().trim();
                        DeviceKind theKind = DeviceKind.getDeviceKind(modelKind);
                        if (theKind == null) {
                            System.out.println("Unknown device kind '" + modelKind + "'.");
                            break;
                        }
                        SmartDeviceModel newModel = new SmartDeviceModel(modelName, theKind);
                        addDeviceModel(newModel);
                        System.out.println("Device model '" + modelName + "' added.");
                    }
                    case "device:" -> {
                        if (currentRoom == null) {
                            throw new RuntimeException("Device defined outside of a room");
                        }
                        String deviceModel = SafeInput.readBetweenBraces(fileReader);
                        Optional<SmartDeviceModel> model = models.stream().filter(m -> m.name.equals(deviceModel)).findFirst();
                        if (model.isEmpty()) {
                            throw new RuntimeException("Device model not defined");
                        }
                        String deviceName = fileReader.nextLine().trim();
                        SmartDevice newDevice = SmartDevice.createNewDevice(deviceName, model.get());
                        currentRoom.addDevice(newDevice);
                        System.out.println("Device '" + deviceName + "' added to the room.");
                    }
                    case "state:" -> {
                        String stateName = SafeInput.readBetweenBraces(fileReader);
                        State newState = State.deserialize(stateName, fileReader);
                        if(stateName.toLowerCase(Locale.ROOT).equals("current")) {
                            newState.apply();
                        } else {
                            states.add(newState);
                        }
                    }
                    default ->  throw new RuntimeException("Unknown statement");
                }
            } catch (Exception e) {
                String restOfLine = fileReader.hasNextLine() ? fileReader.nextLine() : "";
                System.out.println("Error in '" + what + " " + restOfLine + "' : " + e);
            }
        }
        System.out.println("Loading complete.");
        fileReader.close();
    }

    public void initialConfig() throws FileNotFoundException {
        File f = new File(SAVE_FILENAME);
        if (f.exists()) {
            loadFromFile();
        } else {
            addRoom("Room 1");
            addRoom("Room 2");
            addDeviceModel(new SmartDeviceModel("Camera", CameraDevice.deviceKind));
            addDeviceModel(new SmartDeviceModel("Lamp", LampDevice.deviceKind));
            saveToFile();
        }
    }
    //endregion

    //region Main Menu
    public void menu() {
        MenuHelper.menuLoop("Choose an action:", MENU_OPTIONS, new Runnable[] {
            this::addRoomMenu, this::removeRoomMenu, this::roomMenu, this::newDeviceModelMenu, this::listAllDeviceModels,
            this::listAllDevices, this::switchStateMenu, this::addStateMenu, this::removeStateMenu, this::saveToFile
        }, false);
    }
    //endregion

    //region Getters and Setters
    public ArrayList<SmartDeviceModel> getDeviceModels() {
        return models;
    }

    Room[] getRooms() {
        return rooms.toArray(Room[]::new);
    }

    public static SmartHome getInstance() {
        if (instance == null) {
            instance = new SmartHome();
        }
        return instance;
    }

    //endregion

    //region Menus
    private void removeStateMenu() {
        MenuHelper.listMenuLoop("Select state to remove:", "Cancel", "No saved states found.", states, this::removeState, true);
    }

    private void addStateMenu() {
        SafeInput si = new SafeInput(new Scanner(System.in));
        si.nameInputLoop("Please enter the new state name (empty to cancel):", "State added.", "Failed to add state", this::addState);
    }

    private void switchStateMenu() {
        MenuHelper.listMenuLoop("Select the state by number:", "Cancel", "No saved states found.", states, State::apply, true);
    }

    private void newDeviceModelMenu() {
        SafeInput si = new SafeInput(new Scanner(System.in));
        si.nameInputLoop("Enter new device model name: ","Added device model.", "Failed to add new model.", modelName -> {
            AtomicBoolean success = new AtomicBoolean(false);
            MenuHelper.listMenuLoop("What kind of device is it:", "Cancel", "No known device kinds.", DeviceKind.AVAILABLE_KINDS, kind -> success.set(addDeviceModel(new SmartDeviceModel(modelName, kind))), true);
            return success.get();
        });
    }

    private void removeRoomMenu() {
        MenuHelper.listMenuLoop("Select the room to remove:", "Cancel", "No rooms found.", rooms, room -> System.out.println(rooms.remove(room) ? "Room removed." : "Failed to remove room."), true);
    }

    private void addRoomMenu() {
        SafeInput si = new SafeInput(new Scanner(System.in));
        si.nameInputLoop("Give a name for the new room (empty to cancel):", "Room added.", "Failed to add room.", this::addRoom);
    }

    void roomMenu() {
        String[] roomList = new String[rooms.size() + 1];
        roomList[0] = "Back";
        for (int i=0; i < rooms.size(); i++) {
            roomList[i+1] = rooms.get(i).getName();
        }
        int choice = MenuHelper.menu("Select room", roomList);
        if (choice > 0) {
            rooms.get(choice-1).menu();
        }
    }

    //endregion

    //region Actions
    boolean addRoom(String roomName) {
        if (rooms.stream().anyMatch(r -> r.getName().equals(roomName))) {
            System.out.println("Room '" + roomName + "' already exists");
            return false;
        }
        return rooms.add(new Room(roomName));
    }

    boolean addDeviceModel(SmartDeviceModel deviceModel) {
        if (models.contains(deviceModel)) {
            return false;
        }
        return models.add(deviceModel);
    }

    void listAllDevices() {
        for (Room room : rooms) {
            System.out.println("* Room '" + room.getName() + "':");
            room.listDevices();
        }
    }

    void listAllDeviceModels() {
        for (SmartDeviceModel model : models) {
            System.out.println("* " + model.name + " (" + model.kind + ")");
            if (!model.elements.isEmpty()) {
                System.out.println("  Elements:");
                for(SmartElement el : model.elements) {
                    System.out.println("  - " + el.name);
                }
            }
        }
    }

    boolean addState(String stateName) {
        if (states.stream().anyMatch(s -> s.name.equals(stateName))) {
            System.out.println("State " + stateName + " already exists.");
            return false;
        }
        State state = new State(stateName);
        state.save();
        states.add(state);
        return true;
    }

    void removeState(State state) {
        System.out.println(states.contains(state) && states.remove(state) ? "State removed." : "Failed to remove state.");
    }

    //endregion
}
