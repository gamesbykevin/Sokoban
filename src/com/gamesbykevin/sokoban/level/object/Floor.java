package com.gamesbykevin.sokoban.level.object;

import com.gamesbykevin.sokoban.engine.Engine;

import java.util.Random;

/**
 * This is for the floor
 * @author GOD
 */
public final class Floor extends LevelObject 
{
    //all the different types of floors
    public static final Type[] FLOORS = {
        Type.FLOOR_GRAY1, Type.FLOOR_GRAY2, 
        Type.FLOOR_BROWN1, Type.FLOOR_BROWN2, 
        Type.FLOOR_BEIGE1, Type.FLOOR_BEIGE2, 
        Type.FLOOR_GREEN1, Type.FLOOR_GREEN2};
    
    //dimensions of this level object
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    
    public Floor(final Random random)
    {
        //pick random floor type
        this(FLOORS[random.nextInt(FLOORS.length)]);
    }
    
    public Floor(final Type type)
    {
        super(type);
    }
    
    /**
     * Setup the floor animation
     */
    @Override
    protected final void setup() throws Exception
    {
        //location of the animation
        final int col;
        final int row;
        
        //add default animations for the walls
        switch (getType())
        {
            case FLOOR_GRAY1:
                col = 2;
                row = 3;
                break;
                
            case FLOOR_GRAY2:
                col = 1;
                row = 5;
                break;
                
            case FLOOR_BROWN1:
                col = 2;
                row = 2;
                break;
                
            case FLOOR_BROWN2:
                col = 1;
                row = 4;
                break;
                
            case FLOOR_BEIGE1:
                col = 2;
                row = 0;
                break;
                
            case FLOOR_BEIGE2:
                col = 1;
                row = 2;
                break;
                
            case FLOOR_GREEN1:
                col = 2;
                row = 1;
                break;
                
            case FLOOR_GREEN2:
                col = 1;
                row = 3;
                break;
                
            default:
                throw new Exception("Level Object Type not found = " + getType());
        }
        
        //add animation
        super.addAnimation(col * WIDTH, row * HEIGHT, WIDTH, HEIGHT);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update animation
        super.updateAnimation(engine.getMain().getTime());
    }
}