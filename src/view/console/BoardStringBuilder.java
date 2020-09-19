package view.console;

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

    private static Map<Integer, Integer> phoneToNumpadMap;

    static {
        phoneToNumpadMap = new HashMap<>();
        phoneToNumpadMap.put(1, 7);
        phoneToNumpadMap.put(2, 8);
        phoneToNumpadMap.put(3, 9);
        phoneToNumpadMap.put(4, 4);
        phoneToNumpadMap.put(5, 5);
        phoneToNumpadMap.put(6, 6);
        phoneToNumpadMap.put(7, 1);
        phoneToNumpadMap.put(8, 2);
        phoneToNumpadMap.put(9, 3);
    }

    private LineType cellLineType = LineType.NORMAL;
    private LineWeight cellLineWeight = LineWeight.LIGHT;
    private LineType subgridLineType = LineType.NORMAL;
    private LineWeight subgridLineWeight = LineWeight.HEAVY;
    private LineType selectionLineType = LineType.DOUBLE;
    private LineWeight selectionLineWeight = LineWeight.HEAVY;

    private List<List<Unit>> units;
    private List<List<UnitInstanceType>> unitTypesOrdering;
    private List<RowType> rowsOrdering;
    private List<ColumnType> columnsOrdering;
    private Map<Integer, List<Unit>> subgridBorderIndicesMap;
    private Map<Integer, Map<Integer, List<Unit>>> cellBorderIndicesMap;
    private Map<Integer, Map<Integer, Unit>> numUnitsIndexMap;
    private Map<Integer, Map<Integer, Unit>> numUnitsSubgridCellMap;
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
        initRowsOrdering();
        initColumnsOrdering();
        initUnitsOrdering();
        initUnits();
        setUnits();
        hasSelection = false;
    }

    private void initRowsOrdering () {
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
    }

    private void initColumnsOrdering () {
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
    }

    private void initUnitsOrdering () {
        unitTypesOrdering = new ArrayList<>();
        List<UnitInstanceType> topRow = new ArrayList<>(Arrays.asList(
                UnitInstanceType.TOP_LEFT_CORNER,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.TOP_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.TOP_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.TOP_INTERSECT_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.TOP_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.TOP_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.TOP_INTERSECT_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.TOP_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.TOP_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.TOP_RIGHT_CORNER
        ));
        List<UnitInstanceType> numRow = new ArrayList<>(Arrays.asList(
                UnitInstanceType.VERTICAL_EDGE,
                UnitInstanceType.SPACE, UnitInstanceType.NUMBER, UnitInstanceType.SPACE,
                UnitInstanceType.VERTICAL_LINE,
                UnitInstanceType.SPACE, UnitInstanceType.NUMBER, UnitInstanceType.SPACE,
                UnitInstanceType.VERTICAL_LINE,
                UnitInstanceType.SPACE, UnitInstanceType.NUMBER, UnitInstanceType.SPACE,
                UnitInstanceType.VERTICAL_EDGE,
                UnitInstanceType.SPACE, UnitInstanceType.NUMBER, UnitInstanceType.SPACE,
                UnitInstanceType.VERTICAL_LINE,
                UnitInstanceType.SPACE, UnitInstanceType.NUMBER, UnitInstanceType.SPACE,
                UnitInstanceType.VERTICAL_LINE,
                UnitInstanceType.SPACE, UnitInstanceType.NUMBER, UnitInstanceType.SPACE,
                UnitInstanceType.VERTICAL_EDGE,
                UnitInstanceType.SPACE, UnitInstanceType.NUMBER, UnitInstanceType.SPACE,
                UnitInstanceType.VERTICAL_LINE,
                UnitInstanceType.SPACE, UnitInstanceType.NUMBER, UnitInstanceType.SPACE,
                UnitInstanceType.VERTICAL_LINE,
                UnitInstanceType.SPACE, UnitInstanceType.NUMBER, UnitInstanceType.SPACE,
                UnitInstanceType.VERTICAL_EDGE
        ));
        List<UnitInstanceType> cellBorderRow = new ArrayList<>(Arrays.asList(
                UnitInstanceType.LEFT_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE,
                UnitInstanceType.VERTICAL_CELL_HORIZONTAL_CELL,
                UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE,
                UnitInstanceType.VERTICAL_CELL_HORIZONTAL_CELL,
                UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE,
                UnitInstanceType.VERTICAL_SUBGRID_HORIZONTAL_CELL,
                UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE,
                UnitInstanceType.VERTICAL_CELL_HORIZONTAL_CELL,
                UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE,
                UnitInstanceType.VERTICAL_CELL_HORIZONTAL_CELL,
                UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE,
                UnitInstanceType.VERTICAL_SUBGRID_HORIZONTAL_CELL,
                UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE,
                UnitInstanceType.VERTICAL_CELL_HORIZONTAL_CELL,
                UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE,
                UnitInstanceType.VERTICAL_CELL_HORIZONTAL_CELL,
                UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE, UnitInstanceType.HORIZONTAL_LINE,
                UnitInstanceType.RIGHT_INTERSECT_CELL
        ));
        List<UnitInstanceType> subgridBorderRow = new ArrayList<>(Arrays.asList(
                UnitInstanceType.LEFT_INTERSECT_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.VERTICAL_CELL_HORIZONTAL_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.VERTICAL_CELL_HORIZONTAL_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.VERTICAL_SUBGRID_HORIZONTAL_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.VERTICAL_CELL_HORIZONTAL_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.VERTICAL_CELL_HORIZONTAL_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.VERTICAL_SUBGRID_HORIZONTAL_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.VERTICAL_CELL_HORIZONTAL_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.VERTICAL_CELL_HORIZONTAL_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.RIGHT_INTERSECT_SUBGRID
        ));
        List<UnitInstanceType> bottomRow = new ArrayList<>(Arrays.asList(
                UnitInstanceType.BOTTOM_LEFT_CORNER,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.BOTTOM_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.BOTTOM_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.BOTTOM_INTERSECT_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.BOTTOM_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.BOTTOM_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.BOTTOM_INTERSECT_SUBGRID,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.BOTTOM_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.BOTTOM_INTERSECT_CELL,
                UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE, UnitInstanceType.HORIZONTAL_EDGE,
                UnitInstanceType.BOTTOM_RIGHT_CORNER
        ));
        for (RowType rowType: rowsOrdering) {
            switch (rowType) {
                case TOP:
                    unitTypesOrdering.add(topRow);
                    break;
                case BOTTOM:
                    unitTypesOrdering.add(bottomRow);
                    break;
                case NUMBER:
                    unitTypesOrdering.add(numRow);
                    break;
                case CELL_BORDER:
                    unitTypesOrdering.add(cellBorderRow);
                    break;
                case SUBGRID_BORDER:
                    unitTypesOrdering.add(subgridBorderRow);
                    break;
            }
        }
    }

    private void initUnits () {
        units = new ArrayList<>();
        subgridBorderIndicesMap = new HashMap<>();
        cellBorderIndicesMap = new HashMap<>();
        numUnitsIndexMap = new HashMap<>();
        numUnitsSubgridCellMap = new HashMap<>();

        for (int i = 0; i < rowsOrdering.size(); i++) {
            List<Unit> unitRow = new ArrayList<>();
            for (int j = 0; j < columnsOrdering.size(); j++) {
                UnitInstanceType instanceType = unitTypesOrdering.get(i).get(j);
                Unit unit = new Unit(instanceType);
                unitRow.add(unit);
            }
            units.add(unitRow);
        }

        int cellHeight = 3;
        int cellWidth = 5;
        int heightOverlap = 1;
        int widthOverlap = 1;
        int subgridHeight = (3 * cellHeight) - (2 * heightOverlap);
        int subgridWidth = (3 * cellWidth) - (2 * widthOverlap);
        int numVerticalOffset = 1;
        int numHorizontalOffset = 2;

        for (int phoneSubgrid = 1; phoneSubgrid <= 9; phoneSubgrid++) {
            int startI = ((phoneSubgrid - 1) / 3) * (subgridHeight - 1);
            int endI = startI + (subgridHeight - 1);
            int startJ = ((phoneSubgrid - 1) % 3) * (subgridWidth - 1);
            int endJ = startJ + (subgridWidth - 1);
            int numpadSubgrid = phoneToNumpadMap.get(phoneSubgrid);
            for (int i = startI; i <= endI; i++) {
                for (int j = startJ; j <= endJ; j++) {
                    Unit unit = units.get(i).get(j);
                    indexSubgridBorderUnit(unit, numpadSubgrid);
                }
            }

            for (int phoneCell = 1; phoneCell <= 9; phoneCell++) {
                int startCellI = startI + (((phoneCell - 1) / 3) * (cellHeight - 1));
                int endCellI = startCellI + (cellHeight - 1);
                int startCellJ = startJ + (((phoneCell - 1) % 3) * (cellWidth - 1));
                int endCellJ = startCellJ + (cellWidth - 1);
                int numpadCell = phoneToNumpadMap.get(phoneCell);
                for (int i = startCellI; i <= endCellI; i++) {
                    for (int j = startCellJ; j <= endCellJ; j++) {
                        Unit unit = units.get(i).get(j);
                        indexCellBorderUnit(unit, numpadSubgrid, numpadCell);
                        indexSubgridCellNumUnit(unit, numpadSubgrid, numpadCell);
                    }
                }
            }
        }

        for (int row = 1; row <= 9; row++) {
            for (int column = 1; column <= 9; column++) {
                int i = ((row - 1) * (cellHeight - 1)) + numVerticalOffset;
                int j = ((column - 1) * (cellWidth - 1)) + numHorizontalOffset;
                Unit unit = units.get(i).get(j);
                indexRowColumnNumUnit(unit, row, column);
            }
        }
    }

    private void indexSubgridBorderUnit (Unit unit, int numpadSubgrid) {
        if (unit.getType() != UnitType.LINE || !unit.isSubgrid()) {
            return;
        }
        if (!subgridBorderIndicesMap.containsKey(numpadSubgrid)) {
            subgridBorderIndicesMap.put(numpadSubgrid, new ArrayList<>());
        }
        subgridBorderIndicesMap.get(numpadSubgrid).add(unit);
    }

    private void indexCellBorderUnit (Unit unit, int numpadSubgrid, int numpadCell) {
        if (unit.getType() != UnitType.LINE) {
            return;
        }
        if (!cellBorderIndicesMap.containsKey(numpadSubgrid)) {
            cellBorderIndicesMap.put(numpadSubgrid, new HashMap<>());
        }
        if (!cellBorderIndicesMap.get(numpadSubgrid).containsKey(numpadCell)) {
            cellBorderIndicesMap.get(numpadSubgrid).put(numpadCell, new ArrayList<>());
        }
        cellBorderIndicesMap.get(numpadSubgrid).get(numpadCell).add(unit);
    }

    private void indexSubgridCellNumUnit (Unit unit, int numpadSubgrid, int numpadCell) {
        if (unit.getType() != UnitType.NUMBER) {
            return;
        }
        if (!numUnitsSubgridCellMap.containsKey(numpadSubgrid)) {
            numUnitsSubgridCellMap.put(numpadSubgrid, new HashMap<>());
        }
        numUnitsSubgridCellMap.get(numpadSubgrid).put(numpadCell, unit);
    }

    private void indexRowColumnNumUnit (Unit unit, int row, int column) {
        if (unit.getType() != UnitType.NUMBER) {
            return;
        }
        if (!numUnitsIndexMap.containsKey(row)) {
            numUnitsIndexMap.put(row, new HashMap<>());
        }
        numUnitsIndexMap.get(row).put(column, unit);
    }

    private void setUnits () {
        for (int subgrid = 1; subgrid <= 9; subgrid++) {
            for (int cell = 1; cell <= 9; cell++) {
                for (Unit unit: cellBorderIndicesMap.get(subgrid).get(cell)) {
                    unit.setLineType(cellLineType);
                    unit.setLineWeight(cellLineWeight);
                }
            }
        }
        for (int subgrid = 1; subgrid <= 9; subgrid++) {
            for (Unit unit: subgridBorderIndicesMap.get(subgrid)) {
                unit.setLineType(subgridLineType);
                unit.setLineWeight(subgridLineWeight);
            }
        }
    }

    public void setNum (int num) {
        setNumBySubgridCell(num, lastSelectionSubgridNum, lastSelectionCellNum);
        deselect();
    }

    public void setNumByIndex (int num, int row, int column) {
        assert 1 <= num && num <= 9;
        assert 1 <= row && row <= 9;
        assert 1 <= column && column <= 9;

        numUnitsIndexMap.get(row).get(column).setNum(num);
        deselect();
    }

    public void setNumBySubgridCell (int num, int subgrid, int cell) {
        assert 1 <= num && num <= 9;
        assert 1 <= subgrid && subgrid <= 9;
        assert 1 <= cell && cell <= 9;

        numUnitsSubgridCellMap.get(subgrid).get(cell).setNum(num);
        deselect();
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

    public void selectSubgrid (int subgrid) {
        assert 1 <= subgrid && subgrid <= 9;

        if (hasSelection) {
            if (lastSelectionType == SelectionType.SUBGRID && lastSelectionSubgridNum == subgrid) {
                return ;
            } else {
                deselect();
            }
        }

        for (Unit unit: subgridBorderIndicesMap.get(subgrid)) {
            unit.setLineType(selectionLineType);
            unit.setLineWeight(selectionLineWeight);
        }

        hasSelection = true;
        lastSelectionType = SelectionType.SUBGRID;
        lastSelectionSubgridNum = subgrid;
    }

    public void selectCell (int cell) {
        assert 1 <= cell && cell <= 9;

        if (!hasSelection || lastSelectionType != SelectionType.SUBGRID) {
            return;
        }
        deselect();

        int subgrid = lastSelectionSubgridNum;

        for (Unit unit: cellBorderIndicesMap.get(subgrid).get(cell)) {
            unit.setLineType(selectionLineType);
            unit.setLineWeight(selectionLineWeight);
        }

        hasSelection = true;
        lastSelectionType = SelectionType.CELL;
        lastSelectionCellNum = cell;
    }

    public void deselect () {
        if (!hasSelection) {
            return;
        }

        int subgrid = lastSelectionSubgridNum;
        if (lastSelectionType == SelectionType.CELL) {
            int cell = lastSelectionCellNum;
            for (Unit unit: cellBorderIndicesMap.get(subgrid).get(cell)) {
                unit.setLineType(cellLineType);
                unit.setLineWeight(cellLineWeight);
            }
        }
        for (Unit unit: subgridBorderIndicesMap.get(subgrid)) {
            unit.setLineType(subgridLineType);
            unit.setLineWeight(subgridLineWeight);
        }

        hasSelection = false;
    }

    public void clear () {
        deselect();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                numUnitsIndexMap.get(i).get(j).setNum(0);
            }
        }
    }

    public List<StringBuilder> getStringBuilder () {
        List<StringBuilder> list = new ArrayList<>();
        for (List<Unit> unitRow: units) {
            StringBuilder rowBuilder = new StringBuilder();
            for (Unit unit: unitRow) {
                rowBuilder.append(unit);
            }
            list.add(rowBuilder);
        }
        return list;
    }

    public void print () {
        List<StringBuilder> rows = getStringBuilder();
        for (StringBuilder row: rows) {
            System.out.println(row);
        }
    }

}
