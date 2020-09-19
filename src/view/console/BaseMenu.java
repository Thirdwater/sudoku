package view.console;

public enum BaseMenu implements Menu {

    GAME {
        private BoardStringBuilder boardStringBuilder;
        private GameInputState currentInputState;

        @Override
        public void init () {
            boardStringBuilder = new BoardStringBuilder();
            boardStringBuilder.print();
            currentInputState = GameInputState.SUBGRID;
        }

        @Override
        public Menu processInput (int input) {
            int numpadInput = convertInputToNumpad(input);
            if (numpadInput != -1) {
                switch (currentInputState) {
                    case SUBGRID:
                        boardStringBuilder.selectSubgrid(numpadInput);
                        currentInputState = GameInputState.CELL;
                        break;
                    case CELL:
                        boardStringBuilder.selectCell(numpadInput);
                        currentInputState = GameInputState.NUMBER;
                        break;
                    case NUMBER:
                        boardStringBuilder.setNum(numpadInput);
                        currentInputState = GameInputState.SUBGRID;
                        break;
                }
            }
            boardStringBuilder.print();
            return this;
        }

        private int convertInputToNumpad (int input) {
            switch (input) {
                case 113: case 81:
                    return 7;
                case 119: case 87:
                    return 8;
                case 101: case 69:
                    return 9;
                case 97: case 65:
                    return 4;
                case 115: case 83:
                    return 5;
                case 100: case 68:
                    return 6;
                case 122: case 90:
                    return 1;
                case 120: case 88:
                    return 2;
                case 99: case 67:
                    return 3;
                default:
                    return -1;
            }
        }
    }

}
