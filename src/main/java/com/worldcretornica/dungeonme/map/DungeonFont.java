package com.worldcretornica.dungeonme.map;

import org.bukkit.map.MapFont;

public class DungeonFont extends MapFont {

    private static final int spaceSize = 2;
    private static final int spaceHeight = 5;

    private static final String fontChars =
        " 0123456789F";

    private static final int[][] fontData = new int[][] {
        /* null */  {0,0,0,0,0},
        /* 0 */  {7,5,5,5,7},
        /* 1 */  {2,3,2,2,2},
        /* 2 */  {7,4,7,1,7},
        /* 3 */  {7,4,7,4,7},
        /* 4 */  {5,5,7,4,4},
        /* 5 */  {7,1,7,4,7},
        /* 6 */  {7,1,7,5,7},
        /* 7 */  {7,4,4,4,4},
        /* 8 */  {7,5,7,5,7},
        /* 9 */  {7,5,7,4,4},
        /* F */  {7,1,3,1,1}
    };

    public static final DungeonFont Font = new DungeonFont(false);

    public DungeonFont() {
        this(true);
    }

    private DungeonFont(boolean malleable) {
        for (int i = 1; i < fontData.length; ++i) {
            char ch = fontChars.charAt(i);

            if (ch == ' ') {
                setChar(ch, new CharacterSprite(spaceSize, spaceHeight, new boolean[spaceSize * spaceHeight]));
                continue;
            }

            int[] rows = fontData[i];
            int width = 0;
            for (int r = 0; r < spaceHeight; ++r) {
                for (int c = 0; c < spaceHeight; ++c) {
                    if ((rows[r] & (1 << c)) != 0 && c > width) {
                        width = c;
                    }
                }
            }
            ++width;

            boolean[] data = new boolean[width * spaceHeight];
            for (int r = 0; r < spaceHeight; ++r) {
                for (int c = 0; c < width; ++c) {
                    data[r * width + c] = (rows[r] & (1 << c)) != 0;
                }
            }

            setChar(ch, new CharacterSprite(width, spaceHeight, data));
        }

        this.malleable = malleable;
    }

}
