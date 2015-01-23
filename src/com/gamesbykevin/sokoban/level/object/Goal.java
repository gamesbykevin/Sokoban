/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gamesbykevin.sokoban.level.object;

import com.gamesbykevin.sokoban.engine.Engine;
import java.util.Random;

/**
 * This is the goal in the level
 * @author GOD
 */
public final class Goal extends LevelObject 
{
    //all the different types of goals
    public static final Type[] GOALS = {
        Type.GOAL_BLACK, Type.GOAL_GRAY, Type.GOAL_BROWN, Type.GOAL_BEIGE, 
        Type.GOAL_RED, Type.GOAL_YELLOW, Type.GOAL_BLUE, Type.GOAL_PURPLE
    };
    
    //dimensions of this level object
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;
    
    //location on spritesheet where the goals start
    private static final int START_X = 0;
    private static final int START_Y = 384;
    
    public Goal(final Random random)
    {
        //pick random wall type
        this(GOALS[random.nextInt(GOALS.length)]);
    }
    
    public Goal(final Type type)
    {
        super(type);
    }
    
    /**
     * Setup the wall animation
     */
    @Override
    protected final void setup() throws Exception
    {
        //location of goal
        final int col;
        final int row = 0;
        
        //add default animations for the goal
        switch (getType())
        {
            case GOAL_BLACK:
                col = 5;
                break;
                
            case GOAL_GRAY:
                col = 2;
                break;
                
            case GOAL_BROWN:
                col = 0;
                break;
                
            case GOAL_BEIGE:
                col = 1;
                break;
                
            case GOAL_RED:
                col = 6;
                break;
                
            case GOAL_YELLOW:
                col = 7;
                break;
                
            case GOAL_BLUE:
                col = 4;
                break;
                
            case GOAL_PURPLE:
                col = 3;
                break;
                
            default:
                throw new Exception("Level Object Type not found = " + getType());
        }
        
        //add animation
        addAnimation(START_X + (WIDTH * col), START_Y + (HEIGHT * row), WIDTH, HEIGHT);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update animation
        super.updateAnimation(engine.getMain().getTime());
    }
}