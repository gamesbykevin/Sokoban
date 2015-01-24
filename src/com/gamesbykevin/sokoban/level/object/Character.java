package com.gamesbykevin.sokoban.level.object;

import com.gamesbykevin.framework.base.Animation;

import com.gamesbykevin.sokoban.engine.Engine;

/**
 * This represents the character
 * @author GOD
 */
public final class Character extends LevelObject
{
    //the different animations for the character
    public static final String NORTH = "N";
    public static final String SOUTH = "S";
    public static final String EAST = "E";
    public static final String WEST = "W";
    
    public Character() throws Exception
    {
        super(Type.CHARACTER);
        
        //setup animations
        setup();
    }
    
    @Override
    public final void reset()
    {
        //call parent reset
        super.reset();
        
        //default facing south
        super.setAnimation(SOUTH);
    }
    
    /**
     * Setup the box animation
     */
    @Override
    protected final void setup() throws Exception
    {
        //character facing north
        Animation north = new Animation();
        north.add(384, 0, 37, 60, DEFAULT_DELAY);
        north.add(362, 128, 37, 60, DEFAULT_DELAY);
        north.add(362, 188, 37, 60, DEFAULT_DELAY);
        north.setLoop(true);
        
        //character facing south
        Animation south = new Animation();
        south.add(362, 248, 37, 59, DEFAULT_DELAY);
        south.add(357, 362, 37, 59, DEFAULT_DELAY);
        south.add(320, 362, 37, 59, DEFAULT_DELAY);
        south.setLoop(true);
        
        //character facing east
        Animation east = new Animation();
        east.add(320, 128, 41, 58, DEFAULT_DELAY);
        east.add(320, 245, 42, 58, DEFAULT_DELAY);
        east.setLoop(true);
        
        //character facing west
        Animation west = new Animation();
        west.add(320, 186, 42, 58, DEFAULT_DELAY);
        west.add(320, 304, 42, 58, DEFAULT_DELAY);
        west.setLoop(true);
        
        //add character animations
        super.addAnimation(north, NORTH);
        super.addAnimation(south, SOUTH);
        super.addAnimation(east, EAST);
        super.addAnimation(west, WEST);
        
        //set default animation
        super.setAnimation(SOUTH);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update animation
        super.updateAnimation(engine.getMain().getTime());
        
        //if the player is at the destination we don't want a live animation
        super.getSpriteSheet().getSpriteSheetAnimation().setLoop(!hasDestination());
        
        //update the character's location
        super.updateLocation();
    }
}