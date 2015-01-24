package com.gamesbykevin.sokoban.level.object;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.sokoban.engine.Engine;

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
    protected static final long DEFAULT_DELAY = Timers.toNanoSeconds(250L);
    
    //the default key for animations
    protected static final String DEFAULT_ANIMATION_KEY = "DEFAULT";
    
    protected LevelObject(final Type type)
    {
        this.type = type;
        
        //create the spritesheet
        createSpriteSheet();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
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
    
    public Type getType()
    {
        return this.type;
    }
    
    public void render(final Graphics graphics, final Image image)
    {
        super.draw(graphics, image);
    }
}