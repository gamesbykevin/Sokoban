package com.gamesbykevin.sokoban.level.object;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.sokoban.engine.Engine;
import com.gamesbykevin.sokoban.shared.Shared;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Every object in the level is a level object
 * @author GOD
 */
public abstract class LevelObject extends Sprite implements Disposable
{
    public enum Type
    {
        WALL_BLACK_FLAT, WALL_BLACK_ROUNDED, 
        WALL_GRAY_FLAT,  WALL_GRAY_ROUNDED, 
        WALL_BROWN_FLAT, WALL_BROWN_ROUNDED, 
        WALL_BEIGE_FLAT, WALL_BEIGE_ROUNDED,
        BOX_BLACK, BOX_GRAY, BOX_BROWN, BOX_BEIGE, BOX_RED, BOX_YELLOW, BOX_BLUE, BOX_PURPLE,
        GOAL_BLACK, GOAL_GRAY, GOAL_BROWN, GOAL_BEIGE, GOAL_RED, GOAL_YELLOW, GOAL_BLUE, GOAL_PURPLE,
        FLOOR_GRAY1,  FLOOR_GRAY2, FLOOR_BROWN1, FLOOR_BROWN2, FLOOR_BEIGE1, FLOOR_BEIGE2, FLOOR_GREEN1, FLOOR_GREEN2, 
        CHARACTER
    }
    
    //the type of object this is
    private final Type type;
    
    //default animation time delay between frames
    protected static final long DEFAULT_DELAY = Timers.toNanoSeconds(125L);
    
    //the default key for animations
    protected static final String DEFAULT_ANIMATION_KEY = "DEFAULT";
    
    //the speed at which the level objects can move
    protected static final double VELOCITY = (1.0d / (double)(Shared.DEFAULT_UPS/4));
    
    //the starting location of this level object
    private Cell start;
    
    //the destination location of this level object
    private Cell destination;
    
    protected LevelObject(final Type type)
    {
        //store the tye of level object
        this.type = type;
        
        //create the spritesheet
        createSpriteSheet();
    }
    
    /**
     * Set the destination location of this level object.
     * @param start Object containing column, row
     */
    public void setDestination(final Cell destination)
    {
        setDestination((int)destination.getCol(), (int)destination.getRow());
    }
    
    /**
     * Set the destination location of this level object.
     * @param col Column 
     * @param row Row
     */
    public void setDestination(final int col, final int row)
    {
        if (destination == null)
            destination = new Cell();
        
        destination.setCol(col);
        destination.setRow(row);
    }
    
    /**
     * Get the destination location
     * @return The destination location of this level object
     */
    public Cell getDestination()
    {
        return this.destination;
    }

    /**
     * Is the level object located at the destination
     * @return true if the object col, row is equal to the destination col, row. false otheriwse
     */
    public boolean hasDestination()
    {
        return equals(getDestination());
    }
    
    /**
     * Set the starting location of this level object.
     * @param start Object containing column, row
     */
    public void setStart(final Cell start)
    {
        setStart(start.getCol(), start.getRow());
    }
    
    /**
     * Place the level object back at the start location
     */
    public void reset()
    {
        //move back to start
        this.setCol(getStart());
        this.setRow(getStart());
        
        //set destination at start as well
        this.setDestination(getStart());
    }
    
    /**
     * Set the starting location of this level object.
     * @param col Column 
     * @param row Row
     */
    public void setStart(final double col, final double row)
    {
        if (start == null)
            start = new Cell();
        
        start.setCol(col);
        start.setRow(row);
    }
    
    /**
     * Get the start location
     * @return The original starting location of this level object
     */
    public Cell getStart()
    {
        return this.start;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        start = null;
        destination = null;
    }
    
    public boolean isWall()
    {
        switch (getType())
        {
            case WALL_BLACK_FLAT:
            case WALL_BLACK_ROUNDED:
            case WALL_GRAY_FLAT:
            case WALL_GRAY_ROUNDED:
            case WALL_BROWN_FLAT:
            case WALL_BROWN_ROUNDED:
            case WALL_BEIGE_FLAT:
            case WALL_BEIGE_ROUNDED:
                return true;
                
            default:
                return false;
        }
    }
    
    public boolean isBox()
    {
        switch (getType())
        {
            case BOX_BLACK:
            case BOX_GRAY:
            case BOX_BROWN:
            case BOX_BEIGE:
            case BOX_RED:
            case BOX_YELLOW:
            case BOX_BLUE:
            case BOX_PURPLE:
                return true;
                
            default:
                return false;
        }
    }
    
    public boolean isGoal()
    {
        switch (getType())
        {
            case GOAL_BLACK:
            case GOAL_GRAY:
            case GOAL_BROWN:
            case GOAL_BEIGE:
            case GOAL_RED:
            case GOAL_YELLOW:
            case GOAL_BLUE:
            case GOAL_PURPLE:
                return true;
                
            default:
                return false;
        }
    }
    
    public boolean isFloor()
    {
        switch (getType())
        {
            case FLOOR_GRAY1:
            case FLOOR_GRAY2:
            case FLOOR_BROWN1:
            case FLOOR_BROWN2:
            case FLOOR_BEIGE1:
            case FLOOR_BEIGE2:
            case FLOOR_GREEN1:
            case FLOOR_GREEN2:
                return true;
                
            default:
                return false;
        }
    }
    
    public boolean isCharacter()
    {
        switch (getType())
        {
            case CHARACTER:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Update the current assigned animation
     * @param time Time delay for each game update, in nanoseconds
     * @throws Exception will be thrown if there is no delay provided
     */
    protected void updateAnimation(final long time) throws Exception
    {
        super.getSpriteSheet().update(time);
    }
    
    /**
     * Each child will have a method to update itself
     * @param engine 
     */
    public abstract void update(final Engine engine) throws Exception;
    
    /**
     * Each child class will need to setup an animation
     * @throws Exception will be thrown if there is problem with animaion setup
     */
    protected abstract void setup() throws Exception;
    
    /**
     * Add a single frame animation with default unique key to access
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     */
    protected void addAnimation(final int x, final int y, final int w, final int h)
    {
        addAnimation(x, y, w, h, DEFAULT_ANIMATION_KEY);
    }
    
    /**
     * Add a single frame animation
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     * @param key unique key to access this animation
     */
    protected void addAnimation(final int x, final int y, final int w, final int h, final String key)
    {
        Animation animation = new Animation();
        animation.add(x, y, w, h, DEFAULT_DELAY);
        animation.setLoop(false);
        
        addAnimation(animation, key);
    }
    
    protected void addAnimation(final Animation animation, final String key)
    {
        super.getSpriteSheet().add(animation, key);
    }
    
    public void setAnimation(final String key)
    {
        super.getSpriteSheet().setCurrent(key);
    }
    
    /**
     * Is this the current animation
     * @param key The unique key of the animation we want to check
     * @return true if the current animation is the parameter specified, false otherwise
     */
    public boolean hasAnimation(final String key)
    {
        return (super.getSpriteSheet().getCurrent() == key);
    }
    
    public Type getType()
    {
        return this.type;
    }
    
    /**
     * Update the location, if we are not at the destination
     */
    protected void updateLocation()
    {
        //if the object is not at the destination, move it
        if (!hasDestination())
        {
            //if we are close enough to the destination
            if (Cell.getDistance(getDestination(), this) < VELOCITY)
            {
                //place at destination
                super.setCol(getDestination());
                super.setRow(getDestination());
            }
            
            if (getCol() < getDestination().getCol())
            {
                super.setVelocityX(VELOCITY);
            }
            else if (getCol() > getDestination().getCol())
            {
                super.setVelocityX(-VELOCITY);
            }
            else
            {
                super.resetVelocityX();
            }
            
            if (getRow() < getDestination().getRow())
            {
                super.setVelocityY(VELOCITY);
            }
            else if (getRow() > getDestination().getRow())
            {
                super.setVelocityY(-VELOCITY);
            }
            else
            {
                super.resetVelocityY();
            }
            
            //update location
            super.setCol(getCol() + getVelocityX());
            super.setRow(getRow() + getVelocityY());
        }
    }
    
    public void render(final Graphics graphics, final Image image)
    {
        //draw the object
        super.draw(graphics, image);
    }
}