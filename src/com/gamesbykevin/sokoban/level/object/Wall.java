package com.gamesbykevin.sokoban.level.object;

import com.gamesbykevin.sokoban.engine.Engine;

import java.util.Random;

public final class Wall extends LevelObject 
{
    
    //all the different types of walls
    public static final Type[] WALLS = {
        Type.WALL_BLACK_FLAT, Type.WALL_BLACK_ROUNDED, 
        Type.WALL_GRAY_FLAT,  Type.WALL_GRAY_ROUNDED, 
        Type.WALL_BROWN_FLAT, Type.WALL_BROWN_ROUNDED, 
        Type.WALL_BEIGE_FLAT, Type.WALL_BEIGE_ROUNDED
    };
    
    //dimensions of this level object
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    
    public Wall(final Random random) throws Exception
    {
        //pick random wall type
        this(WALLS[random.nextInt(WALLS.length)]);
    }
    
    public Wall(final Type type) throws Exception
    {
        super(type);
        
        //setup animations
        setup();
    }
    
    /**
     * Setup the wall animation
     */
    @Override
    protected final void setup() throws Exception
    {
        //add default animations for the walls
        switch (getType())
        {
            case WALL_BLACK_FLAT:
                addAnimation(WIDTH * 1, HEIGHT * 0, WIDTH, HEIGHT);
                break;
                
            case WALL_BLACK_ROUNDED:
                addAnimation(WIDTH * 0, HEIGHT * 2, WIDTH, HEIGHT);
                break;
                
            case WALL_GRAY_FLAT:
                addAnimation(WIDTH * 0, HEIGHT * 4, WIDTH, HEIGHT);
                break;
                
            case WALL_GRAY_ROUNDED:
                addAnimation(WIDTH * 0, HEIGHT * 0, WIDTH, HEIGHT);
                break;
                
            case WALL_BROWN_FLAT:
                addAnimation(WIDTH * 0, HEIGHT * 5, WIDTH, HEIGHT);
                break;
                
            case WALL_BROWN_ROUNDED:
                addAnimation(WIDTH * 0, HEIGHT * 1, WIDTH, HEIGHT);
                break;
                
            case WALL_BEIGE_FLAT:
                addAnimation(WIDTH * 1, HEIGHT * 1, WIDTH, HEIGHT);
                break;
                
            case WALL_BEIGE_ROUNDED:
                addAnimation(WIDTH * 0, HEIGHT * 3, WIDTH, HEIGHT);
                break;
                
            default:
                throw new Exception("Level Object Type not found = " + getType());
        }
        
        //default the animation for now
        super.getSpriteSheet().setCurrent(DEFAULT_ANIMATION_KEY);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update animation
        super.updateAnimation(engine.getMain().getTime());
    }
}