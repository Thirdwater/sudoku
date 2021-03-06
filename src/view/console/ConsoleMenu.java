package view.console;

import java.io.PrintStream;
import biz.source_code.utils.RawConsoleInput;

public class ConsoleMenu {

    // Singleton
    private static final ConsoleMenu INSTANCE = new ConsoleMenu();
    private ConsoleMenu () {
        String operatingSystem = System.getProperty("os.name");
        isWindows = operatingSystem.contains("Windows");
    }
    public static ConsoleMenu getInstance () {
        return INSTANCE;
    }

    private boolean isWindows;
    private boolean isRunning;
    private Menu currentMenu;

    public void start () throws Exception {
        init();
        run();
    }

    public void init () throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        clearConsole();
        currentMenu = BaseMenu.GAME;
        currentMenu.init();
    }

    public void clearConsole () throws Exception {
        if (isWindows) {
            // Runtime.getRuntime().exec("cls") didn't work
            // See https://stackoverflow.com/a/33525703/
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            Runtime.getRuntime().exec("clear");
        }
    }

    public void run () throws Exception {
        isRunning = true;
        RawConsoleInput consoleInput = new RawConsoleInput();
        while (isRunning) {
            int input = consoleInput.read(true);
            clearConsole();
            if (isExitSequence(input)) {
                stopRunning();
            }
            processInput(input);
            System.out.println(input);
        }
    }

    public void stopRunning () {
        isRunning = false;
    }

    private boolean isExitSequence (int input) {
        return input == 3;  // Ctrl + c
    }

    private void processInput (int input) {
        Menu targetMenu = currentMenu.processInput(input);
        if (targetMenu != currentMenu) {
            targetMenu.init();
            currentMenu = targetMenu;
        }
    }

}
