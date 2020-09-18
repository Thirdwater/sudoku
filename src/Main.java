public class Main {

    public static void main (String[] args) {
        ConsoleMenu menu = ConsoleMenu.getInstance();
        try {
            menu.init();
            menu.clearConsole();
            BoardStringBuilder bs = new BoardStringBuilder();
            bs.test();
        } catch (Exception e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

}
