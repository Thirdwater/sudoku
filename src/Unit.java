public class Unit {

    private UnitType unitType;
    private LineType lineType;
    private LineWeight lineWeight;
    private boolean isSubgrid;
    private char lightNormal;
    private char heavyNormal;
    private char lightDotted;
    private char heavyDotted;
    private char doubleLined;
    private char currentChar;
    private int currentNum;

    public Unit (UnitInstanceType instanceType) {
        unitType = instanceType.unitType;
        lineType = LineType.NORMAL;
        lineWeight = LineWeight.LIGHT;
        isSubgrid = instanceType.isSubgrid;
        lightNormal = instanceType.lightNormal;
        heavyNormal = instanceType.heavyNormal;
        lightDotted = instanceType.lightDotted;
        heavyDotted = instanceType.heavyDotted;
        doubleLined = instanceType.doubleLined;
        currentChar = lightNormal;
        currentNum = 0;
    }

    public UnitType getType () {
        return unitType;
    }

    public boolean isSubgrid () {
        return isSubgrid;
    }

    public void setNum (int num) {
        if (unitType != UnitType.NUMBER) {
            return;
        }
        currentNum = num;
    }

    public void setLineType (LineType lineType) {
        if (unitType != UnitType.LINE || this.lineType == lineType) {
            return;
        }
        this.lineType = lineType;
        updateCurrentCharacter();
    }

    public void setLineWeight (LineWeight lineWeight) {
        if (unitType != UnitType.LINE || this.lineWeight == lineWeight) {
            return;
        }
        this.lineWeight = lineWeight;
        updateCurrentCharacter();
    }

    private void updateCurrentCharacter () {
        if (unitType != UnitType.LINE) {
            return;
        }
        switch (lineType) {
            case NORMAL:
                switch (lineWeight) {
                    case LIGHT:
                        currentChar = lightNormal;
                        break;
                    case HEAVY:
                        currentChar = heavyNormal;
                        break;
                }
                break;
            case DOTTED:
                switch (lineWeight) {
                    case LIGHT:
                        currentChar = lightDotted;
                        break;
                    case HEAVY:
                        currentChar = heavyDotted;
                        break;
                }
                break;
            case DOUBLE:
                currentChar = doubleLined;
                break;
            case BLANK:
                currentChar = ' ';
                break;
        }
    }

    @Override
    public String toString () {
        switch (unitType) {
            case SPACE:
                return " ";
            case NUMBER:
                if (currentNum == 0) {
                    return " ";
                }
                return String.valueOf(currentNum);
            case LINE:
            default:
                return Character.toString(currentChar);
        }
    }

}
