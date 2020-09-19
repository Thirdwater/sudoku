package view.console;

public enum UnitInstanceType {

    TOP_LEFT_CORNER('┌', '┏', '╔', true),
    TOP_RIGHT_CORNER( '┐', '┓', '╗', true),
    BOTTOM_LEFT_CORNER( '└', '┗', '╚', true),
    BOTTOM_RIGHT_CORNER( '┘', '┛', '╝', true),

    HORIZONTAL_EDGE( '─', '━', '╌', '┅', '═', true),
    VERTICAL_EDGE( '│', '┃', '┊', '┋', '║', true),
    HORIZONTAL_LINE( '─', '━', '╌', '┅', '═', false),
    VERTICAL_LINE( '│', '┃', '┊', '┋', '║', false),

    TOP_INTERSECT_CELL( '┬', '┯', '╤', true),
    BOTTOM_INTERSECT_CELL( '┴', '┷', '╧', true),
    LEFT_INTERSECT_CELL( '├', '┠', '╟', true),
    RIGHT_INTERSECT_CELL( '┤', '┨', '╢', true),
    TOP_INTERSECT_SUBGRID('┰', '┳', '╦', true),
    BOTTOM_INTERSECT_SUBGRID( '┸', '┻', '╩', true),
    LEFT_INTERSECT_SUBGRID( '┝', '┣', '╠', true),
    RIGHT_INTERSECT_SUBGRID( '┥', '┫', '╣', true),

    VERTICAL_CELL_HORIZONTAL_CELL('┼', '┼', '╬', false),
    VERTICAL_CELL_HORIZONTAL_SUBGRID('┼', '┿', '╪', true),
    VERTICAL_SUBGRID_HORIZONTAL_CELL('┼', '╂', '╫', true),
    VERTICAL_SUBGRID_HORIZONTAL_SUBGRID('┼', '╋', '╬', true),

    SPACE(UnitType.SPACE),
    NUMBER(UnitType.NUMBER);

    public char lightNormal;
    public char heavyNormal;
    public char lightDotted;
    public char heavyDotted;
    public char doubleLined;
    public boolean isSubgrid;
    public UnitType unitType;
    
    UnitInstanceType(UnitType unitType) {
        this.isSubgrid = false;
        this.unitType = unitType;
    }

    UnitInstanceType(
            char lightNormalDotted, char heavyNormalDotted,
            char doubleLined, boolean isSubgrid) {
        this.lightNormal = lightNormalDotted;
        this.heavyNormal = heavyNormalDotted;
        this.lightDotted = lightNormalDotted;
        this.heavyDotted = heavyNormalDotted;
        this.doubleLined = doubleLined;
        this.isSubgrid = isSubgrid;
        this.unitType = UnitType.LINE;
    }

    UnitInstanceType(
            char lightNormal, char heavyNormal,
            char lightDotted, char heavyDotted,
            char doubleLined,
            boolean isSubgrid) {
        this.lightNormal = lightNormal;
        this.heavyNormal = heavyNormal;
        this.lightDotted = lightDotted;
        this.heavyDotted = heavyDotted;
        this.doubleLined = doubleLined;
        this.isSubgrid = isSubgrid;
        this.unitType = UnitType.LINE;
    }

}
