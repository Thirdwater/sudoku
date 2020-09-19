package view.console;

public interface Menu {

    default public void init () {
        return;
    }

    default public Menu processInput (int input) {
        return this;
    }

}
