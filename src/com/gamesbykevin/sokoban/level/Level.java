package com.gamesbykevin.sokoban.level;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.sokoban.engine.Engine;
import com.gamesbykevin.sokoban.level.object.Box;
import com.gamesbykevin.sokoban.level.object.LevelObject;
import com.gamesbykevin.sokoban.resources.GameAudio;
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
    public static final int DEFAULT_DIMENSION = 32;
    
    //the floor for the level
    private LevelObject floor;
    
    //the entire dimensions of the level
    private final int cols, rows;
    
    //the starting location for the player
    private Cell start;
    
    protected Level(final int cols, final int rows)
    {
        super();
        
        //store overall level dimensions
        this.cols = cols;
        this.rows = rows;
        
        //create list
        this.levelObjects = new ArrayList<>();
    }
    
    /**
     * Get the columns
     * @return The total number of columns in this level
     */
    public int getColumns()
    {
        return this.cols;
    }
    
    /**
     * Get the rows
     * @return The total number of rows in this level
     */
    public int getRows()
    {
        return this.rows;
    }
    
    /**
     * Check if there are mobile objects
     * @return true if at least 1 object is not at its destination, false otherwise
     */
    public boolean hasMobileObjects()
    {
        for (int i = 0; i < levelObjects.size(); i++)
        {
            //if this object is not at the destinaton, it is mobile
            if (!levelObjects.get(i).hasDestination())
                return true;
        }
        
        //all objects are at destination
        return false;
    }
    
    private boolean hasPhysicalLevelObjectNorth(final int col, final int startRow)
    {
        for (int row = startRow; row >= 0; row--)
        {
            if (getPhysicalLevelObject(col, row) != null)
                return true;
        }
        
        return false;
    }
    
    private boolean hasPhysicalLevelObjectSouth(final int col, final int startRow)
    {
        for (int row = startRow; row < getRows(); row++)
        {
            if (getPhysicalLevelObject(col, row) != null)
                return true;
        }
        
        return false;
    }
    
    private boolean hasPhysicalLevelObjectEast(final int startCol, final int row)
    {
        for (int col = startCol; col < getColumns(); col++)
        {
            if (getPhysicalLevelObject(col, row) != null)
                return true;
        }
        
        return false;
    }
    
    private boolean hasPhysicalLevelObjectWest(final int startCol, final int row)
    {
        for (int col = startCol; col >= 0; col--)
        {
            if (getPhysicalLevelObject(col, row) != null)
                return true;
        }
        
        return false;
    }
    
    /**
     * Get the physical level object at the specified location.
     * @param col Column we want to look at
     * @param row Row we want to look at
     * @return level object found at the specified location that is a box or wall, if not found null is returned
     */
    public LevelObject getPhysicalLevelObject(final int col, final int row)
    {
        for (int i = 0; i < levelObjects.size(); i++)
        {
            //get the current level object
            LevelObject object = levelObjects.get(i);
            
            //if the object exists at this location
            if (object.equals(col, row))
            {
                //we only want the physical objects
                if (object.isBox() || object.isWall())
                    return object;
            }
        }
        
        //nothing was found
        return null;
    }
    
    /**
     * Get the level object
     * @param col Column
     * @param row Row
     * @return the level object at the specified location, if none found null is returned
     */
    public LevelObject getLevelObject(final int col, final int row)
    {
        for (int i = 0; i < levelObjects.size(); i++)
        {
            //get the current level object
            LevelObject object = levelObjects.get(i);
            
            //if this object is at the location, return it
            if (object.equals(col, row))
                return object;
        }
        
        //no object was found
        return null;
    }
    
    /**
     * Set the starting location of the character for this level
     * @param col Column
     * @param row Row
     */
    protected void setStart(final int col, final int row)
    {
        if (this.start == null)
            this.start = new Cell();
        
        this.start.setCol(col);
        this.start.setRow(row);
    }
    
    /**
     * Get the starting location of the player
     * @return The col, row where the player should start
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
     * Move all of the level objects back to their original starting position
     */
    public void reset()
    {
        for (int i = 0; i < levelObjects.size(); i++)
        {
            //reset the current level object
            levelObjects.get(i).reset();
        }
    }
    
    /**
     * Count the boxes
     * @return The total number of boxes in this level
     */
    public int getBoxCount()
    {
        int count = 0;
        
        for (int i = 0; i < levelObjects.size(); i++)
        {
            //count this if it is a box
            if (levelObjects.get(i).isBox())
                count++;
        }
        
        //return the total
        return count;
    }
    
    /**
     * Add level object to the level
     * @param object Object we want to add
     * @param col Column location
     * @param row Row location
     */
    public void add(final LevelObject object, final int col, final int row)
    {
        //set (col, row) location
        object.setCol(col);
        object.setRow(row);
        
        //assign the starting location as well
        object.setStart(col, row);
        
        //assign the destination
        object.setDestination(col, row);

        //the goal is half the size of the default dimensions
        if (object.isGoal())
        {
            //set the dimensions of the object
            object.setWidth(Level.DEFAULT_DIMENSION / 2);
            object.setHeight(Level.DEFAULT_DIMENSION / 2);
        }
        else
        {
            //set the dimensions of the object
            object.setWidth(Level.DEFAULT_DIMENSION);
            object.setHeight(Level.DEFAULT_DIMENSION);
        }

        //set coordinates
        object.setX(getStartX(object) + (Level.DEFAULT_DIMENSION / 2) - (object.getWidth() / 2));
        object.setY(getStartX(object) + (Level.DEFAULT_DIMENSION / 2) - (object.getHeight() / 2));
        
        //add to list
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
        
        if (floor != null)
        {
            floor.dispose();
            floor = null;
        }
    }
    
    /**
     * Get the starting y-coordinate 
     * @param object containing the row
     * @return the upper left y-coordinate for the specified row
     */
    public double getStartY(final LevelObject object)
    {
        return getStartY(object.getRow());
    }
    
    /**
     * Get the starting y-coordinate 
     * @param row Row
     * @return the upper left y-coordinate for the specified row
     */
    public double getStartY(final double row)
    {
        return (getY() + ((double)Level.DEFAULT_DIMENSION * row));
    }
    
    /**
     * Get the starting x-coordinate 
     * @param object containing the column
     * @return the upper left x-coordinate for the specified column
     */
    public double getStartX(final LevelObject object)
    {
        return getStartX(object.getCol());
    }
    
    /**
     * Get the starting x-coordinate 
     * @param col Column
     * @return the upper left x-coordinate for the specified column
     */
    public double getStartX(final double col)
    {
        return (getX() + ((double)Level.DEFAULT_DIMENSION * col));
    }
    
    /**
     * Is the level completed?
     * @return true if all boxes are placed above all goals, false otherwise
     */
    public boolean hasCompleted()
    {
        for (int i = 0; i < levelObjects.size(); i++)
        {
            LevelObject object = levelObjects.get(i);
            
            //we only want to check the boxes
            if (!object.isBox())
                continue;
            
            //the box is not assigned the 'on goal' animation, the level is not completed
            if (!object.hasAnimation(Box.ON_GOAL))
                return false;
        }
        
        //all boxes are on a goal
        return true;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (super.getImage() == null)
            super.setImage(engine.getResources().getGameImage(Keys.SpriteSheet));
        
        //did we just place a box on a goal
        boolean result = false;
        
        //update the level objects
        for (int i = 0; i < levelObjects.size(); i++)
        {
            LevelObject object = levelObjects.get(i);
            
            //update object
            object.update(engine);
            
            //if this object is a box, check for goal match
            if (object.isBox())
            {
                //if true, then a box was just placed on a goal
                if (checkBoxAnimation(object))
                    result = true;
            }
            
            //set coordinates
            object.setX(getStartX(object) + (Level.DEFAULT_DIMENSION / 2) - (object.getWidth() / 2));
            object.setY(getStartY(object) + (Level.DEFAULT_DIMENSION / 2) - (object.getHeight() / 2));
        }
        
        //if we just placed a box on the goal, play sound effect
        if (result)
            engine.getResources().playGameAudio(GameAudio.Keys.Place);
    }
    
    /**
     * Check the box, to set the correct animation.<br>
     * @param object The object representing the box
     * @return true if a box was placed onto a goal for the first time, false time
     */
    private boolean checkBoxAnimation(final LevelObject object)
    {
        //is there a goal that has the same location as the box
        boolean match = false;
        
        //check each level object
        for (int i = 0; i < levelObjects.size(); i++)
        {
            //if this is not a goal, skip
            if (!levelObjects.get(i).isGoal())
                continue;
                
            //if the location matches the box object, hide the goal
            if (levelObjects.get(i).equals(object))
            {
                //flag match
                match = true;
                
                //exit loop
                break;
            }
        }
        
        //set box animation accordingly
        if (match)
        {
            //if the current animation is not on a goal and now is, this is the first time
            if (object.getSpriteSheet().getCurrent() != Box.ON_GOAL)
            {
                //set on box animation
                object.setAnimation(Box.ON_GOAL);
                
                //just placed return true
                return true;
            }
            else
            {
                //set on box animation
                object.setAnimation(Box.ON_GOAL);
                
                //this was already placed
                return false;
            }
        }
        else
        {
            //set normal animation
            object.setAnimation(Box.NORMAL);
            
            //no match, this is not placed on a goal
            return false;
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //draw the floor first
        for (int col = 0; col < getColumns(); col++)
        {
            for (int row = 0; row < getRows(); row++)
            {
                //there must be a physical object in all directions in order for there to be a floor here
                if (!hasPhysicalLevelObjectEast(col, row))
                    continue;
                if (!hasPhysicalLevelObjectWest(col, row))
                    continue;
                if (!hasPhysicalLevelObjectNorth(col, row))
                    continue;
                if (!hasPhysicalLevelObjectSouth(col, row))
                    continue;
                
                //set floor location to current level object
                floor.setLocation(getStartX(col), getStartY(row));

                //draw floor first 
                floor.render(graphics, getImage());
            }
        }
        
        //now draw the goals
        for (int i = 0; i < levelObjects.size(); i++)
        {
            //get the current level object
            LevelObject object = levelObjects.get(i);
            
            //only draw the goal
            if (object.isGoal())
                object.render(graphics, getImage());
        }
        
        //now draw the rest
        for (int i = 0; i < levelObjects.size(); i++)
        {
            //get the current level object
            LevelObject object = levelObjects.get(i);
            
            //only draw if not the goal
            if (!object.isGoal())
                object.render(graphics, getImage());
        }
    }
}