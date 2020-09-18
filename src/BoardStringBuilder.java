import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 *  ┏━━━━━┯━━━━━┯━━━━━┳━━━━━┯━━━━━┯━━━━━┳━━━━━┯━━━━━┯━━━━━┓
 *  ┃  1  │  2  │  3  ┃  4  │  5  │  6  ┃  7  │  8  │  9  ┃
 *  ┠─────┼─────┼─────╂─────┼─────┼─────╂─────┼─────┼─────┨
 *  ┃  2  │  2  │     ┃     │     │     ┃     │     │     ┃
 *  ┠─────┼─────┼─────╂─────┼─────┼─────╂─────┼─────┼─────┨
 *  ┃  3  │     │  3  ┃     │     │     ┃     │     │     ┃
 *  ┣━━━━━┿━━━━━┿━━━━━╋━━━━━┿━━━━━┿━━━━━╋━━━━━┿━━━━━┿━━━━━┫
 *  ┃  4  │     │     ┃  4  │     │     ┃     │     │     ┃
 *  ┠─────┼─────┼─────╂─────┼─────┼─────╂─────┼─────┼─────┨
 *  ┃  5  │     │     ┃     │  5  │     ┃     │     │     ┃
 *  ┠─────┼─────┼─────╂─────┼─────┼─────╂─────┼─────┼─────┨
 *  ┃  6  │     │     ┃     │     │  6  ┃     │     │     ┃
 *  ┣━━━━━┿━━━━━┿━━━━━╋━━━━━┿━━━━━┿━━━━━╋━━━━━┿━━━━━┿━━━━━┫
 *  ┃  7  │     │     ┃     │     │     ┃  7  │     │     ┃
 *  ┠─────┼─────┼─────╂─────┼─────┼─────╂─────┼─────┼─────┨
 *  ┃  8  │     │     ┃     │     │     ┃     │  8  │     ┃
 *  ┠─────┼─────┼─────╂─────┼─────┼─────╂─────┼─────┼─────┨
 *  ┃  9  │     │     ┃     │     │     ┃     │     │  9  ┃
 *  ┗━━━━━┷━━━━━┷━━━━━┻━━━━━┷━━━━━┷━━━━━┻━━━━━┷━━━━━┷━━━━━┛
 */
public class BoardStringBuilder {

    /**
     *  ┼───┼  ┼────┼  ┼────┼  ┼─────┼
     *  │ 1 │  │ 1  │  │  1 │  │  1  │
     *  ┼───┼  ┼────┼  ┼────┼  ┼─────┼
     */
    public enum CellWidth {
        THIN, SQUARE_LEFT, SQUARE_RIGHT, THICK;
    }

    private LineType cellLineType = LineType.NORMAL;
    private LineWeight cellLineWeight = LineWeight.LIGHT;
    private LineType subgridLineType = LineType.NORMAL;
    private LineWeight subgridLineWeight = LineWeight.HEAVY;
    private LineType selectionLineType = LineType.DOUBLE;
    private LineWeight selectionLineWeight = LineWeight.LIGHT;
    private CellWidth cellWidth = CellWidth.THIN;

    private List<List<Unit>> units;
    private List<StringBuilder> rowStrings;
    private List<RowType> rowsOrdering;
    private List<ColumnType> columnsOrdering;
    private Map<RowType, List<Integer>> rowTypeIndicesMap;
    private Map<ColumnType, List<Integer>> columnTypeIndicesMap;
    private boolean hasSelection;
    private SelectionType lastSelectionType;
    private int lastSelectionSubgridNum;
    private int lastSelectionCellNum;

    private enum RowType {
        TOP, BOTTOM, NUMBER, CELL_BORDER, SUBGRID_BORDER;
    }

    private enum ColumnType {
        LEFT, RIGHT, NUMBER, SPACE, CELL_BORDER, SUBGRID_BORDER;
    }

    public BoardStringBuilder () {
        initRows();
        initColumns();
        initRowStrings();
        initUnits();
        hasSelection = false;
    }

    private void initRows () {
        rowsOrdering = new ArrayList<>(Arrays.asList(
                RowType.TOP,
                RowType.NUMBER, RowType.CELL_BORDER,
                RowType.NUMBER, RowType.CELL_BORDER,
                RowType.NUMBER, RowType.SUBGRID_BORDER,
                RowType.NUMBER, RowType.CELL_BORDER,
                RowType.NUMBER, RowType.CELL_BORDER,
                RowType.NUMBER, RowType.SUBGRID_BORDER,
                RowType.NUMBER, RowType.CELL_BORDER,
                RowType.NUMBER, RowType.CELL_BORDER,
                RowType.NUMBER, RowType.BOTTOM
        ));
        rowTypeIndicesMap = new HashMap<>();
        for (int i = 0; i < rowsOrdering.size(); i++) {
            RowType rowType = rowsOrdering.get(i);
            if (!rowTypeIndicesMap.containsKey(rowType)) {
                rowTypeIndicesMap.put(rowType, new ArrayList<>());
            }
            rowTypeIndicesMap.get(rowType).add(i);
        }
    }

    private void initColumns () {
        columnsOrdering = new ArrayList<>(Arrays.asList(
                ColumnType.LEFT,
                ColumnType.SPACE, ColumnType.NUMBER, ColumnType.SPACE, ColumnType.CELL_BORDER,
                ColumnType.SPACE, ColumnType.NUMBER, ColumnType.SPACE, ColumnType.CELL_BORDER,
                ColumnType.SPACE, ColumnType.NUMBER, ColumnType.SPACE, ColumnType.SUBGRID_BORDER,
                ColumnType.SPACE, ColumnType.NUMBER, ColumnType.SPACE, ColumnType.CELL_BORDER,
                ColumnType.SPACE, ColumnType.NUMBER, ColumnType.SPACE, ColumnType.CELL_BORDER,
                ColumnType.SPACE, ColumnType.NUMBER, ColumnType.SPACE, ColumnType.SUBGRID_BORDER,
                ColumnType.SPACE, ColumnType.NUMBER, ColumnType.SPACE, ColumnType.CELL_BORDER,
                ColumnType.SPACE, ColumnType.NUMBER, ColumnType.SPACE, ColumnType.CELL_BORDER,
                ColumnType.SPACE, ColumnType.NUMBER, ColumnType.SPACE, ColumnType.RIGHT
        ));
        columnTypeIndicesMap = new HashMap<>();
        for (int i = 0; i < columnsOrdering.size(); i++) {
            ColumnType columnType = columnsOrdering.get(i);
            if (!columnTypeIndicesMap.containsKey(columnType)) {
                columnTypeIndicesMap.put(columnType, new ArrayList<>());
            }
            columnTypeIndicesMap.get(columnType).add(i);
        }
    }

    private void initRowStrings () {
        rowStrings = new ArrayList<>();
        for (RowType rowType: rowsOrdering) {
            StringBuilder rowString = new StringBuilder();
            switch (rowType) {
                case TOP:
                    rowString.append("┏━━━┯━━━┯━━━┳━━━┯━━━┯━━━┳━━━┯━━━┯━━━┓");
                    break;
                case SUBGRID_BORDER:
                    rowString.append("┣━━━┿━━━┿━━━╋━━━┿━━━┿━━━╋━━━┿━━━┿━━━┫");
                    break;
                case BOTTOM:
                    rowString.append("┗━━━┷━━━┷━━━┻━━━┷━━━┷━━━┻━━━┷━━━┷━━━┛");
                    break;
                case CELL_BORDER:
                    rowString.append("┠───┼───┼───╂───┼───┼───╂───┼───┼───┨");
                    break;
                case NUMBER:
                    rowString.append("┃   │   │   ┃   │   │   ┃   │   │   ┃");
                    break;
            }
            rowStrings.add(rowString);
        }
    }

    private void initUnits () {
        units = new ArrayList<>();
        for (RowType rowType: rowsOrdering) {
            List<Unit> unitRow = new ArrayList<>();
            switch (rowType) {
                case TOP:
                    for (ColumnType columnType: columnsOrdering) {
                        Unit unit;
                        switch (columnType) {
                            case LEFT:
                                unit = new Unit(UnitInstanceType.TOP_LEFT_CORNER);
                                break;
                            case RIGHT:
                                unit = new Unit(UnitInstanceType.TOP_RIGHT_CORNER);
                                break;
                            case NUMBER:
                            case SPACE:
                                unit = new Unit(UnitInstanceType.HORIZONTAL_EDGE);
                                break;
                            case CELL_BORDER:
                                unit = new Unit(UnitInstanceType.TOP_INTERSECT_CELL);
                                break;
                            case SUBGRID_BORDER:
                            default:
                                unit = new Unit(UnitInstanceType.TOP_INTERSECT_SUBGRID);
                                break;
                        }
                        unit.setLineType(LineType.NORMAL);
                        unit.setLineWeight(LineWeight.HEAVY);
                        unitRow.add(unit);
                    }
                    break;
                case BOTTOM:
                    for (ColumnType columnType: columnsOrdering) {
                        Unit unit;
                        switch (columnType) {
                            case LEFT:
                                unit = new Unit(UnitInstanceType.BOTTOM_LEFT_CORNER);
                                break;
                            case RIGHT:
                                unit = new Unit(UnitInstanceType.BOTTOM_RIGHT_CORNER);
                                break;
                            case NUMBER:
                            case SPACE:
                                unit = new Unit(UnitInstanceType.HORIZONTAL_EDGE);
                                break;
                            case CELL_BORDER:
                                unit = new Unit(UnitInstanceType.BOTTOM_INTERSECT_CELL);
                                break;
                            case SUBGRID_BORDER:
                            default:
                                unit = new Unit(UnitInstanceType.BOTTOM_INTERSECT_SUBGRID);
                                break;
                        }
                        unit.setLineType(LineType.NORMAL);
                        unit.setLineWeight(LineWeight.HEAVY);
                        unitRow.add(unit);
                    }
                    break;
                case NUMBER:
                    for (ColumnType columnType: columnsOrdering) {
                        Unit unit;
                        switch (columnType) {
                            case LEFT:
                            case RIGHT:
                            case SUBGRID_BORDER:
                                unit = new Unit(UnitInstanceType.VERTICAL_EDGE);
                                unit.setLineWeight(LineWeight.HEAVY);
                                break;
                            case NUMBER:
                                unit = new Unit(UnitInstanceType.NUMBER);
                                break;
                            case SPACE:
                                unit = new Unit(UnitInstanceType.SPACE);
                                break;
                            case CELL_BORDER:
                            default:
                                unit = new Unit(UnitInstanceType.VERTICAL_LINE);
                                break;
                        }
                        unit.setLineType(LineType.NORMAL);
                        unitRow.add(unit);
                    }
                    break;
                case CELL_BORDER:
                    for (ColumnType columnType: columnsOrdering) {
                        Unit unit;
                        switch (columnType) {
                            case LEFT:
                                unit = new Unit(UnitInstanceType.LEFT_INTERSECT_CELL);
                                unit.setLineWeight(LineWeight.HEAVY);
                                break;
                            case RIGHT:
                                unit = new Unit(UnitInstanceType.RIGHT_INTERSECT_CELL);
                                unit.setLineWeight(LineWeight.HEAVY);
                                break;
                            case NUMBER:
                            case SPACE:
                                unit = new Unit(UnitInstanceType.HORIZONTAL_LINE);
                                unit.setLineWeight(LineWeight.LIGHT);
                                break;
                            case CELL_BORDER:
                                unit = new Unit(UnitInstanceType.VERTICAL_CELL_HORIZONTAL_CELL);
                                unit.setLineWeight(LineWeight.LIGHT);
                                break;
                            case SUBGRID_BORDER:
                            default:
                                unit = new Unit(UnitInstanceType.VERTICAL_SUBGRID_HORIZONTAL_CELL);
                                unit.setLineWeight(LineWeight.HEAVY);
                                break;
                        }
                        unit.setLineType(LineType.NORMAL);
                        unitRow.add(unit);
                    }
                    break;
                case SUBGRID_BORDER:
                    for (ColumnType columnType: columnsOrdering) {
                        Unit unit;
                        switch (columnType) {
                            case LEFT:
                                unit = new Unit(UnitInstanceType.LEFT_INTERSECT_SUBGRID);
                                break;
                            case RIGHT:
                                unit = new Unit(UnitInstanceType.RIGHT_INTERSECT_SUBGRID);
                                break;
                            case NUMBER:
                            case SPACE:
                                unit = new Unit(UnitInstanceType.HORIZONTAL_EDGE);
                                break;
                            case CELL_BORDER:
                                unit = new Unit(UnitInstanceType.VERTICAL_CELL_HORIZONTAL_SUBGRID);
                                break;
                            case SUBGRID_BORDER:
                            default:
                                unit = new Unit(UnitInstanceType.VERTICAL_SUBGRID_HORIZONTAL_SUBGRID);
                                break;
                        }
                        unit.setLineType(LineType.NORMAL);
                        unit.setLineWeight(LineWeight.HEAVY);
                        unitRow.add(unit);
                    }
                    break;
            }
            units.add(unitRow);
        }
    }

    public List<StringBuilder> getRowBuilders () {
        return rowStrings;
    }

    public void setNum (int num, int row, int column) {
        assert 1 <= num && num <= 9;
        assert 1 <= row && row <= 9;
        assert 1 <= column && column <= 9;

        int rowIndex = rowTypeIndicesMap.get(RowType.NUMBER).get(row - 1);
        int columnIndex = columnTypeIndicesMap.get(ColumnType.NUMBER).get(column - 1);
        rowStrings.get(rowIndex).setCharAt(columnIndex, Character.forDigit(num, 10));
    }

    public void select (SelectionType selectionType, int num) {
        assert 1 <= num && num <= 9;

        switch (selectionType) {
            case SUBGRID:
                selectSubgrid(num);
            case CELL:
                selectCell(num);
        }
    }

    public void selectSubgrid (int num) {
        assert 1 <= num && num <= 9;

        if (hasSelection) {
            if (lastSelectionType == SelectionType.SUBGRID && lastSelectionSubgridNum == num) {
                return ;
            } else {
                deselect();
            }
        }

        BoxIndices box = getSubgridIndices(num);
        for (int i = box.startI; i <= box.endI; i++) {
            if (i == rowTypeIndicesMap.get(RowType.TOP).get(0)) {
                rowStrings.get(i).replace(box.startJ, box.endJ, "");
            } else {

            }
        }

        hasSelection = true;
        lastSelectionType = SelectionType.SUBGRID;
        lastSelectionSubgridNum = num;
    }

    public void selectCell (int num) {
        assert 1 <= num && num <= 9;

        if (!hasSelection || lastSelectionType != SelectionType.SUBGRID) {
            return;
        }
        deselect();

        BoxIndices box = getCellIndices(lastSelectionSubgridNum, num);

        hasSelection = true;
        lastSelectionType = SelectionType.CELL;
        lastSelectionCellNum = num;
    }

    public void deselect () {
        if (!hasSelection) {
            return;
        }

        hasSelection = false;
    }

    private class BoxIndices {
        public int startI;
        public int startJ;
        public int endI;
        public int endJ;
    }

    private BoxIndices getSubgridIndices (int num) {
        BoxIndices box = new BoxIndices();

        if ((num - 1) / 3 == 2) {
            box.startI = rowTypeIndicesMap.get(RowType.TOP).get(0);
            box.endI = rowTypeIndicesMap.get(RowType.SUBGRID_BORDER).get(0);
        } else {
            int index = (6 - num) / 3;
            box.startI = rowTypeIndicesMap.get(RowType.SUBGRID_BORDER).get(index);
            if (index == 0) {
                box.endI = rowTypeIndicesMap.get(RowType.SUBGRID_BORDER).get(index + 1);
            } else {
                box.endI = rowTypeIndicesMap.get(RowType.BOTTOM).get(0);
            }
        }

        if (num % 3 == 1) {
            box.startJ = columnTypeIndicesMap.get(ColumnType.LEFT).get(0);
            box.endJ = columnTypeIndicesMap.get(ColumnType.SUBGRID_BORDER).get(0);
        } else {
            int index = (num + 1) % 3;
            box.startJ = columnTypeIndicesMap.get(ColumnType.SUBGRID_BORDER).get(index);
            if (index == 0) {
                box.endJ = columnTypeIndicesMap.get(ColumnType.SUBGRID_BORDER).get(index + 1);
            } else {
                box.endJ = columnTypeIndicesMap.get(ColumnType.RIGHT).get(0);
            }
        }

        return box;
    }

    private BoxIndices getCellIndices (int subgridNum, int cellNum) {
        BoxIndices box = new BoxIndices();



        return box;
    }

    public void test () {
        for (int i = 1; i <= 9; i++) {
            setNum(i, i, i);
        }
        for (StringBuilder rowString: rowStrings) {
            System.out.println(rowString);
        }
        System.out.println();
        units.get(0).get(4).setLineType(LineType.DOUBLE);
        for (List<Unit> unitRow: units) {
            for (Unit unit: unitRow) {
                System.out.print(unit);
            }
            System.out.println();
        }
    }

}
