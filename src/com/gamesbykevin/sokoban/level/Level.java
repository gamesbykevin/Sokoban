package com.gamesbykevin.sokoban.level;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.sokoban.level.object.LevelObject;

/**
 *
 * @author GOD
 */
public class Level extends Sprite implements Disposable
{
    public static final String KEY_WALL = "#";
    public static final String KEY_PLAYER = "@";
    public static final String KEY_PLAYER_ON_GOAL = "+";
    public static final String KEY_BOX = "$";
    public static final String KEY_BOX_ON_GOAL = "*";
    public static final String KEY_GOAL = ".";
    public static final String KEY_FLOOR = " ";
    
    private LevelObject[][] board;
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
}