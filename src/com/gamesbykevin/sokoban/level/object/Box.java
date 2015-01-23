package com.gamesbykevin.sokoban.level.object;

import com.gamesbykevin.sokoban.engine.Engine;

import java.util.Random;

/**
 * This will be the box that has to be pushed
 * @author GOD
 */
public final class Box extends LevelObject 
{
    //all the different types of boxes
    public static final Type[] BOXES = {
        Type.BOX_BLACK, Type.BOX_GRAY, Type.BOX_BROWN, Type.BOX_BEIGE, 
        Type.BOX_RED, Type.BOX_YELLOW, Type.BOX_BLUE, Type.BOX_PURPLE
    };
    
    //dimensions of this level object
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    
    //the different images for the box
    private static final String NORMAL = "NORMAL";
    private static final String ON_GOAL = "ON_GOAL";
    
    public Box(final Random random)
    {
        //pick random box type
        this(BOXES[random.nextInt(BOXES.length)]);
    }
    
    public Box(final Type type)
    {
        super(type);
    }
    
    /**
     * Setup the box animation
     */
    @Override
    protected final void setup() throws Exception
    {
        //add default animations for the box
        switch (getType())
        {
            case BOX_BLACK:
                addAnimation(WIDTH * 3, HEIGHT * 2, WIDTH, HEIGHT, NORMAL);
                addAnimation(WIDTH * 5, HEIGHT * 0, WIDTH, HEIGHT, ON_GOAL);
                break;
                
            case BOX_GRAY:
                addAnimation(WIDTH * 3, HEIGHT * 5, WIDTH, HEIGHT, NORMAL);
                addAnimation(WIDTH * 4, HEIGHT * 3, WIDTH, HEIGHT, ON_GOAL);
                break;
                
            case BOX_BROWN:
                addAnimation(WIDTH * 3, HEIGHT * 4, WIDTH, HEIGHT, NORMAL);
                addAnimation(WIDTH * 4, HEIGHT * 4, WIDTH, HEIGHT, ON_GOAL);
                break;
                
            case BOX_BEIGE:
                addAnimation(WIDTH * 3, HEIGHT * 1, WIDTH, HEIGHT, NORMAL);
                addAnimation(WIDTH * 5, HEIGHT * 1, WIDTH, HEIGHT, ON_GOAL);
                break;
                
            case BOX_RED:
                addAnimation(WIDTH * 2, HEIGHT * 5, WIDTH, HEIGHT, NORMAL);
                addAnimation(WIDTH * 4, HEIGHT * 1, WIDTH, HEIGHT, ON_GOAL);
                break;
                
            case BOX_YELLOW:
                addAnimation(WIDTH * 3, HEIGHT * 0, WIDTH, HEIGHT, NORMAL);
                addAnimation(WIDTH * 2, HEIGHT * 4, WIDTH, HEIGHT, ON_GOAL);
                break;
                
            case BOX_BLUE:
                addAnimation(WIDTH * 3, HEIGHT * 3, WIDTH, HEIGHT, NORMAL);
                addAnimation(WIDTH * 4, HEIGHT * 5, WIDTH, HEIGHT, ON_GOAL);
                break;
                
            case BOX_PURPLE:
                addAnimation(WIDTH * 4, HEIGHT * 2, WIDTH, HEIGHT, NORMAL);
                addAnimation(WIDTH * 4, HEIGHT * 0, WIDTH, HEIGHT, ON_GOAL);
                break;
                
            default:
                throw new Exception("Level Object Type not found = " + getType());
        }
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update animation
        super.updateAnimation(engine.getMain().getTime());
    }
}