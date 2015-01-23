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
    
    protected LevelObject(final Type type)
    {
        this.type = type;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    /**
     * Update the current assigned animation
     * @param time Time delay for each game update, in nanoseconds
     * @throws Exception will be thrown if there is no delay provided
     */
    protected void updateAnimation(final long time) throws Exception
    {
        //if the sprite sheet does not exist create and setup animation
        if (getSpriteSheet() == null)
        {
            //create the spritesheet
            createSpriteSheet();
            
            //setup animation
            setup();
        }
        
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
     * Add a single frame animation with no unique key to access
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     */
    protected void addAnimation(final int x, final int y, final int w, final int h)
    {
        addAnimation(x, y, w, h, "");
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
    
    public Type getType()
    {
        return this.type;
    }
    
    public void render(final Graphics graphics, final Image image)
    {
        super.draw(graphics, image);
    }
}