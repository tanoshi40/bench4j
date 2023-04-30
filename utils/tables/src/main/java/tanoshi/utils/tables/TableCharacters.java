package tanoshi.utils.tables;

public class TableCharacters {

    // https://www.w3.org/TR/xml-entity-names/025.html - characters reference
    static final char cornerTopLeft = '┌';
    static final char cornerTopRight = '┐';
    static final char cornerBottomLeft = '└';
    static final char cornerBottomRight = '┘';

    static final char dCornerTopLeft = '╔';
    static final char dCornerTopRight = '╗';
    static final char dCornerBottomLeft = '╚';
    static final char dCornerBottomRight = '╝';

    static final char dVCornerTopLeft = '╓';
    static final char dVCornerTopRight = '╖';
    static final char dVCornerBottomLeft = '╙';
    static final char dVCornerBottomRight = '╜';

    static final char dHCornerTopLeft = '╒';
    static final char dHCornerTopRight = '╕';
    static final char dHCornerBottomLeft = '╘';
    static final char dHCornerBottomRight = '╛';
    
    

    static final char crossTop = '┬';
    static final char crossBottom = '┴';
    static final char crossLeft = '├';
    static final char crossRight = '┤';
    static final char crossCenter = '┼';
    
    static final char dCrossTop = '╦';
    static final char dCrossBottom = '╩';
    static final char dCrossLeft = '╠';
    static final char dCrossRight = '╣';
    static final char dCrossCenter = '╬';
    
    static final char dVCrossTop = '╥';
    static final char dVCrossBottom = '╨';
    static final char dVCrossLeft = '╟';
    static final char dVCrossRight = '╢';
    static final char dVCrossCenter = '╫';

    static final char dHCrossTop = '╤';
    static final char dHCrossBottom = '╧';
    static final char dHCrossLeft = '╞';
    static final char dHCrossRight = '╡';
    static final char dHCrossCenter = '╪';


    static final char lineVertical = '│';
    static final char lineHorizontal = '─';
    
    static final char dLineVertical = '║';
    static final char dLineHorizontal = '═';

    public enum CharacterType {
        CORNER_TOP_LEFT(cornerTopLeft, dCornerTopLeft, dVCornerTopLeft, dHCornerTopLeft),
        CORNER_TOP_RIGHT(cornerTopRight, dCornerTopRight, dVCornerTopRight, dHCornerTopRight),
        CORNER_BOTTOM_LEFT(cornerBottomLeft, dCornerBottomLeft, dVCornerBottomLeft, dHCornerBottomLeft),
        CORNER_BOTTOM_RIGHT(cornerBottomRight, dCornerBottomRight, dVCornerBottomRight, dHCornerBottomRight),

        CROSS_TOP(crossTop, dCrossTop, dVCrossTop, dHCrossTop),
        CROSS_BOTTOM(crossBottom, dCrossBottom, dVCrossBottom, dHCrossBottom),
        CROSS_LEFT(crossLeft, dCrossLeft, dVCrossLeft, dHCrossLeft),
        CROSS_RIGHT(crossRight, dCrossRight, dVCrossRight, dHCrossRight),
        CROSS_CENTER(crossCenter, dCrossCenter, dVCrossCenter, dHCrossCenter),

        LINE_VERTICAL(lineVertical, dLineVertical, dLineVertical, dLineVertical),
        LINE_HORIZONTAL(lineHorizontal, dLineHorizontal, dLineHorizontal, dLineHorizontal);

        final char normal;
        final char _double;
        final char doubleVertical;
        final char doubleHorizontal;

        CharacterType(char normal, char _double, char doubleVertical, char doubleHorizontal) {
            this.normal = normal;
            this._double = _double;
            this.doubleVertical = doubleVertical;
            this.doubleHorizontal = doubleHorizontal;
        }

        // TODO: 30.04.2023 add styleOptions here
        public char getChar() { return getChar(LineType.NORMAL); }
        public char getChar(LineType type) {
            return switch (type) {
                case NORMAL -> normal;
                case DOUBLE -> _double;
                case DOUBLE_VERTICAL -> doubleVertical;
                case DOUBLE_HORIZONTAL -> doubleHorizontal;
            };
        }
    }

    public enum LineType {
        NORMAL,
        DOUBLE,
        DOUBLE_VERTICAL,
        DOUBLE_HORIZONTAL
    }
}
