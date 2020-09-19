public class Main {

    public static void main (String[] args) {
        ConsoleMenu menu = ConsoleMenu.getInstance();
        try {
            BoardStringBuilder bs = new BoardStringBuilder();
            menu.start();
            bs.test();
        } catch (Exception e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

}
