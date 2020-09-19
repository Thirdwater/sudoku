package model;

public class Cell {

    private int value;
    private boolean isFixed;

    public Cell () {
        value = 0;
        isFixed = false;
    }

    public boolean isEmpty () {
        return value == 0;
    }

    public int getNumber () {
        return value;
    }

    public boolean isFixed () {
        return isFixed;
    }

    public void setFixed (int n) {
        if (n > 0) {
            value = n;
            isFixed = true;
        }
    }

    public void resetProgress () {
        if (!isFixed()) {
            value = 0;
        }
    }

    public void fillNumber (int n) {
        if (!isFixed()) {
            value = n;
        }
    }

}
