import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class SafeInput {
    Scanner scanner;

    public  SafeInput(Scanner sc) {
        scanner = sc;
    }

    public static String readBetweenBraces(Scanner sc) {
        Pattern delim = sc.delimiter();
        sc.useDelimiter("\\(");
        sc.next();
        sc.useDelimiter("[()]");
        String result = sc.next();
        sc.useDelimiter(delim);
        sc.next("\\)");
        return result;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public <T extends Comparable> Comparable getValue(String prompt, Supplier<T> getMethod) {
        T result = null;
        do {
            try {
                System.out.println(prompt);
                result = getMethod.get();
            } catch (Exception e) {
                System.out.println("Wrong input: (" + e + "). Try again.");
                scanner.nextLine();
            }
        } while(result == null);

        return result;
    }

    public <T extends Comparable> Comparable getValueWithLimits(String prompt, Supplier<T> getMethod, String tryAgain, T min, T max) {
        while(true) {
            T result = (T) getValue(prompt, () -> scanner.nextInt());
            if (result.compareTo(max) <= 0 && result.compareTo(min) >= 0)
                return result;
            System.out.println(tryAgain);
        }
    }

    public int nextInt(String prompt) {
        return (int) getValue(prompt, () -> scanner.nextInt());
    }

    public int nextInt(String prompt, String tryAgain, int min, int max) {
        return (int) this.getValueWithLimits(prompt, () -> scanner.nextInt(), tryAgain, min, max);
    }

    public double nextDouble(String prompt) {
        return (double) getValue(prompt, () -> scanner.nextDouble());
    }

    public short nextShort(String prompt) {
        return (short) getValue(prompt, () -> scanner.nextShort());
    }

    public long nextLong(String prompt) {
        return (long) getValue(prompt, () -> scanner.nextLong());
    }

    public String nextLine(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    public String nameInputLoop(String prompt, String success, String failure, Predicate<String> actionCallback) {
        while(true) {
            String inputLine = nextLine(prompt);
            if (inputLine.isEmpty()) {
                return inputLine;
            }
            if(actionCallback.test(inputLine)) {
                System.out.println(success);
                return inputLine;
            } else {
                System.out.println(failure);
            }
        }
    }
}
