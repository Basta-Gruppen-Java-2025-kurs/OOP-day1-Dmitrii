import java.util.Scanner;

public class MenuHelper {
    public static int menu(String header, String[] options) {
        if (options.length < 1) {
            return -1;
        }
        StringBuilder menuText = new StringBuilder(header + "\n");
        for (int i = 1; i < options.length; i++) {
            menuText.append(i + ". " + options[i] + "\n");
        }
        menuText.append("0. " + options[0]);
        SafeInput si = new SafeInput(new Scanner(System.in));
        return si.nextInt(menuText.toString(), "No such option in the menu", 0, options.length-1);
    }
}
