import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

public final class SmartHome {
    //region Header
    private final String SAVE_FILENAME = "SavedState.txt";

    private final ArrayList<Room> rooms = new ArrayList<>();
    private final ArrayList<SmartDeviceModel> models = new ArrayList<>();
    private final ArrayList<State> states = new ArrayList<>();

    private static final String[] MENU_OPTIONS = { "Exit", "Add room", "Remove Room", "Room Menu", "New device model", "List device models", "List all devices in the house", "Switch state", "Add state", "Remove state", "Save to file" };
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
                        SmartDeviceModel newModel = new SmartDeviceModel();
                        newModel.name = modelName;
                        newModel.kind = modelKind;
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
            SmartDeviceModel camera = new SmartDeviceModel();
            camera.name = "Camera";
            addDeviceModel(camera);
            SmartDeviceModel thermo = new SmartDeviceModel();
            thermo.name = "Lamp";
            addDeviceModel(thermo);
            saveToFile();
        }
    }
    //endregion

    //region Main Menu
    public void menu() {
        MenuHelper.menuLoop("Choose an action:", MENU_OPTIONS, new Runnable[] {
            this::addRoomMenu, this::removeRoomMenu, this::roomMenu, this::newDeviceModelMenu, this::listAllDeviceModels,
            this::listAllDevices, this::switchStateMenu, this::addStateMenu, this::removeStateMenu, this::saveToFile
        });
    }
    //endregion

    private void removeStateMenu() {
        if (states.isEmpty()) {
            System.out.println("No saved states found.");
        }
        String[] menuOptions = new String[states.size()+1];
        Runnable[] actions = new Runnable[states.size()];
        menuOptions[0] = "Cancel";
        for (int i = 0; i < states.size(); i++) {
            State theState = states.get(i);
            menuOptions[i+1] = theState.name;
            actions[i] = () -> {
                if (states.remove(theState)) {
                    System.out.println("Device removed.");
                } else {
                    System.out.println("Failed to remove device");
                }
            };
        }
        MenuHelper.menuLoop("Select state to remove:", menuOptions, actions);
    }

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

    private void addStateMenu() {
        // input new state name
        SafeInput si = new SafeInput(new Scanner(System.in));
        boolean noNameYet = true;
        do {
            String stateName = si.nextLine("Please enter the new state name (empty to cancel):");
            if (stateName.isEmpty()) {
                noNameYet = false;
            } else if (states.stream().anyMatch(s -> s.name.equals(stateName))) {
                System.out.println("This state already exists");
            } else {
                State newState = new State(stateName);
                states.add(newState);
                noNameYet = false;
            }
        } while (noNameYet);
        // create the state if not exists
    }

    private void switchStateMenu() {
        //
    }

    private void newDeviceModelMenu() {
        SafeInput si = new SafeInput(new Scanner(System.in));
    }

    private void removeRoomMenu() {
    }

    private void addRoomMenu() {
        SafeInput si = new SafeInput(new Scanner(System.in));
        si.nameInputLoop("Give a name for the new room (empty to cancel):", "Room added.", "Failed to add room.", this::addRoom);
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
            return false;
        }
        State state = new State(stateName);
        state.save();
        states.add(state);
        return true;
    }

    boolean switchState(State state) {
        state.apply();
        return true;
    }

    boolean removeState(State state) {
        if (states.contains(state)) {
            states.remove(state);
            return true;
        }
        return false;
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

}
