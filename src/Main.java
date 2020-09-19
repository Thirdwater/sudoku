import view.console.ConsoleMenu;

public class Main {

    public static void main (String[] args) {
        ConsoleMenu menu = ConsoleMenu.getInstance();
        try {
            menu.start();
        } catch (Exception e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

}
