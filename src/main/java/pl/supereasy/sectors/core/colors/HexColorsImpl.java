package pl.supereasy.sectors.core.colors;

import java.util.regex.Pattern;

public interface HexColorsImpl {
    String DARK_ORANGE = HexColor.DARK_ORANGE.getAsHex();
    String MEDIUM_ORANGE = HexColor.MEDIUM_ORANGE.getAsHex();
    String LIGHT_ORANGE = HexColor.LIGHT_ORANGE.getAsHex();
    String DARK_YELLOW = HexColor.DARK_YELLOW.getAsHex();
    String MEDIUM_YELLOW = HexColor.MEDIUM_YELLOW.getAsHex();
    String LIGHT_YELLOW = HexColor.LIGHT_YELLOW.getAsHex();
    String DARK_GREEN1 = HexColor.DARK_GREEN1.getAsHex();
    String MEDIUM_GREEN1 = HexColor.MEDIUM_GREEN1.getAsHex();
    String LIGHT_GREEN1 = HexColor.LIGHT_GREEN1.getAsHex();
    String DARK_GREEN2 = HexColor.DARK_GREEN2.getAsHex();
    String MEDIUM_GREEN2 = HexColor.MEDIUM_GREEN2.getAsHex();
    String LIGHT_GREEN2 = HexColor.LIGHT_GREEN2.getAsHex();
    String DARK_BLUE1 = HexColor.DARK_BLUE1.getAsHex();
    String MEDIUM_BLUE1 = HexColor.MEDIUM_BLUE1.getAsHex();
    String LIGHT_BLUE1 = HexColor.LIGHT_BLUE1.getAsHex();
    String DARK_BLUE2 = HexColor.DARK_BLUE2.getAsHex();
    String MEDIUM_BLUE2 = HexColor.MEDIUM_BLUE2.getAsHex();
    String LIGHT_BLUE2 = HexColor.LIGHT_BLUE2.getAsHex();
    String WHITE = HexColor.WHITE.getAsHex();
    String WHITE2 = HexColor.WHITE2.getAsHex();
    String WHITE3 = HexColor.WHITE3.getAsHex();
    String GRAY = HexColor.GRAY.getAsHex();
    String MEDIUM_GRAY = HexColor.MEDIUM_GRAY.getAsHex();
    String DARK_GRAY = HexColor.DARK_GRAY.getAsHex();
    String LIGHT_BLACK = HexColor.LIGHT_BLACK.getAsHex();
    String MEDIUM_BLACK = HexColor.MEDIUM_BLACK.getAsHex();
    String DARK_BLACK = HexColor.DARK_BLACK.getAsHex();
    String LIGHT_RED = HexColor.LIGHT_RED.getAsHex();
    String MEDIUM_RED = HexColor.MEDIUM_RED.getAsHex();
    String FULL_RED = HexColor.FULL_RED.getAsHex();
    String DARK_RED = HexColor.DARK_RED.getAsHex();
    Pattern hexColorPattern = Pattern.compile("#[0-9a-fA-F]{6}");
}
