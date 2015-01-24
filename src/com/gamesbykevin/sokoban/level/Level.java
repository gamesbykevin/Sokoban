package com.gamesbykevin.sokoban.level;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.sokoban.engine.Engine;
import com.gamesbykevin.sokoban.level.object.LevelObject;
import com.gamesbykevin.sokoban.resources.GameImages.Keys;
import com.gamesbykevin.sokoban.shared.IElement;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * Object representing the level
 * @author GOD
 */
public class Level extends Sprite implements Disposable, IElement
{
    public static final String KEY_WALL = "#";
    public static final String KEY_PLAYER = "@";
    public static final String KEY_PLAYER_ON_GOAL = "+";
    public static final String KEY_BOX = "$";
    public static final String KEY_BOX_ON_GOAL = "*";
    public static final String KEY_GOAL = ".";
    public static final String KEY_FLOOR = " ";
    
    //the list of objects that make the level
    private List<LevelObject> levelObjects;
    
    //the default dimension of each col, row in the level
    protected static final int DEFAULT_DIMENSION = 32;
    
    //the floor for the level
    private LevelObject floor;
    
    //starting location for the character
    private Cell start;
    
    protected Level()
    {
        super();
        
        //create list
        this.levelObjects = new ArrayList<>();
    }
    
    /**
     * Set the character starting location
     * @param col Column
     * @param row Row
     */
    protected void setStart(final int col, final int row)
    {
        if (start == null)
            start = new Cell();
        
        //set the start location for the player
        start.setCol(col);
        start.setRow(row);
    }
    
    /**
     * Get the start
     * @return The starting location of the character
     */
    public Cell getStart()
    {
        return this.start;
    }
    
    /**
     * Set the floor of the level
     * @param floor LevelObject representing the floor
     */
    protected void setFloor(final LevelObject floor)
    {
        this.floor = floor;
    }
    
    /**
     * Add level object to list
     * @param object Level object we want to add
     */
    public void add(final LevelObject object)
    {
        //add object to list
        this.levelObjects.add(object);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (levelObjects != null)
        {
            for (int i = 0; i < levelObjects.size(); i++)
            {
                levelObjects.get(i).dispose();
                levelObjects.set(i, null);
            }
            
            levelObjects.clear();
            levelObjects = null;
        }
    }
    
    /**
     * Get the starting y-coordinate 
     * @param object containing the row
     * @return the upper left y-coordinate for the specified row
     */
    public int getStartY(final LevelObject object)
    {
        return getStartY((int)object.getRow());
    }
    
    /**
     * Get the starting y-coordinate 
     * @param row Row
     * @return the upper left y-coordinate for the specified row
     */
    public int getStartY(final int row)
    {
        return (int)(getY() + (Level.DEFAULT_DIMENSION * row));
    }
    
    /**
     * Get the starting x-coordinate 
     * @param object containing the column
     * @return the upper left x-coordinate for the specified column
     */
    public int getStartX(final LevelObject object)
    {
        return getStartX((int)object.getCol());
    }
    
    /**
     * Get the starting x-coordinate 
     * @param col Column
     * @return the upper left x-coordinate for the specified column
     */
    public int getStartX(final int col)
    {
        return (int)(getX() + (Level.DEFAULT_DIMENSION * col));
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (super.getImage() == null)
            super.setImage(engine.getResources().getGameImage(Keys.SpriteSheet));
        
        //update the level objects
        for (int i = 0; i < levelObjects.size(); i++)
        {
            LevelObject object = levelObjects.get(i);
            
            //update object
            object.update(engine);
            
            //set coordinates
            object.setX(getStartX(object) + (Level.DEFAULT_DIMENSION / 2) - (object.getWidth() / 2));
            object.setY(getStartY(object) + (Level.DEFAULT_DIMENSION / 2) - (object.getHeight() / 2));
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        for (int i = 0; i < levelObjects.size(); i++)
        {
            LevelObject object = levelObjects.get(i);
            
            //if object isn't floor, draw the floor
            if (!object.isFloor())
            {
                //set floor location to current level object
                floor.setLocation(getStartX(object), getStartY(object));

                //draw floor first 
                floor.render(graphics, getImage());
            }
            
            //now draw level
            object.render(graphics, getImage());
        }
    }
}