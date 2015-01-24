package com.gamesbykevin.sokoban.player;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.sokoban.engine.Engine;
import com.gamesbykevin.sokoban.level.Level;
import com.gamesbykevin.sokoban.level.object.Character;
import com.gamesbykevin.sokoban.level.object.LevelObject;
import com.gamesbykevin.sokoban.resources.GameImages.Keys;
import com.gamesbykevin.sokoban.shared.IElement;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * The player who can interact with the game
 * @author GOD
 */
public final class Player implements Disposable, IElement
{
    //the character in the level
    private LevelObject character;
    
    //different input options for our player
    private static final int MOVE_WEST = KeyEvent.VK_LEFT;
    private static final int MOVE_EAST = KeyEvent.VK_RIGHT;
    private static final int MOVE_NORTH = KeyEvent.VK_UP;
    private static final int MOVE_SOUTH = KeyEvent.VK_DOWN;
    private static final int LEVEL_RESET = KeyEvent.VK_R;
    
    //the number of moves in the current level
    private int count = 0;
    
    public Player() throws Exception
    {
        //create the character
        this.character = new Character();
        this.character.setDimensions();
        this.character.setWidth(this.character.getWidth() /2);
        this.character.setHeight(this.character.getHeight() / 2);
    }
    
    /**
     * Increase the number of moves
     */
    public void increaseCount()
    {
        this.count++;
    }
    
    /**
     * Get the current total number of moves
     * @return The current total number of moves
     */
    public int getCount()
    {
        return this.count;
    }
    
    /**
     * Get the character
     * @return The character that solved the puzzle
     */
    public LevelObject getCharacter()
    {
        return this.character;
    }
    
    /**
     * Set the character's starting location
     * @param start The object containing column, row
     */
    public void setCharacterStart(final Cell start)
    {
        //set starting point
        this.character.setStart(start);
        
        //also set as current position
        this.character.setCol(start);
        this.character.setRow(start);
        
        //mark at destination as well
        this.character.setDestination(start);
    }
    
    @Override
    public void dispose()
    {
        character.dispose();
        character = null;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //don't continue if character does not exist
        if (getCharacter() == null)
            return;
        
        if (getCharacter().getImage() == null)
            getCharacter().setImage(engine.getResources().getGameImage(Keys.SpriteSheet));

        //get the current level
        Level level = engine.getManager().getLevels().getLevel();

        //set the height to half of the original
        getCharacter().setDimensions();
        getCharacter().setWidth(getCharacter().getWidth() / 2);
        getCharacter().setHeight(getCharacter().getHeight() / 2);

        //set coordinates based on col, row location
        getCharacter().setX(level.getStartX(getCharacter()) + (Level.DEFAULT_DIMENSION / 2) - (getCharacter().getWidth() / 2));
        getCharacter().setY(level.getStartY(getCharacter()) + (Level.DEFAULT_DIMENSION / 2) - (getCharacter().getHeight() / 2));
        
        //update character animation, etc....
        getCharacter().update(engine);
        
        //if the character is at their destination they can be moved
        if (getCharacter().hasDestination())
        {
            //check if the human user moved the character
            checkInput(engine.getKeyboard(), level);
        }
    }
    
    /**
     * Check human input to change the board
     * @param keyboard Object containing human keyboard input
     * @param level The current level
     */
    private void checkInput(final Keyboard keyboard, final Level level)
    {
        //objects we will need to check to determine movement
        final LevelObject object1;
        final LevelObject object2;

        if (keyboard.hasKeyPressed(LEVEL_RESET))
        {
            //reset character
            getCharacter().reset();
            getCharacter().setAnimation(Character.SOUTH);
            
            //reset level
            level.reset();
        }
        else if (keyboard.hasKeyPressed(MOVE_WEST))
        {
            object1 = level.getPhysicalLevelObject((int)getCharacter().getCol() - 1, (int)getCharacter().getRow());

            //if the object to the west does not exist or isn't a wall
            if (object1 == null || !object1.isWall())
            {
                //if the object is a box check the object 1 column over
                if (object1 != null && object1.isBox())
                {
                    //check the object 2 columns over
                    object2 = level.getPhysicalLevelObject((int)getCharacter().getCol() - 2, (int)getCharacter().getRow());

                    //if the next object is not a box or wall, it is a valid move
                    if (object2 == null || !object2.isBox() && !object2.isWall())
                    {
                        //move box over
                        object1.getDestination().decreaseCol();

                        //move character over
                        getCharacter().getDestination().decreaseCol();
                        
                        //increase move count
                        increaseCount();
                    }
                }
                else
                {
                    //any other object should be good for us to move
                    getCharacter().getDestination().decreaseCol();
                    
                    //increase move count
                    increaseCount();
                }
            }

            //set the animation of the character
            getCharacter().setAnimation(Character.WEST);

            //remove key pressed
            keyboard.removeKeyPressed(MOVE_WEST);
        }
        else if (keyboard.hasKeyPressed(MOVE_EAST))
        {
            object1 = level.getPhysicalLevelObject((int)getCharacter().getCol() + 1, (int)getCharacter().getRow());

            //if the object to the east does not exist or isn't a wall
            if (object1 == null || !object1.isWall())
            {
                //if the object is a box check the object 1 column over
                if (object1 != null && object1.isBox())
                {
                    //check the object 2 columns over
                    object2 = level.getPhysicalLevelObject((int)getCharacter().getCol() + 2, (int)getCharacter().getRow());

                    //if the next object is not a box or wall, it is a valid move
                    if (object2 == null || !object2.isBox() && !object2.isWall())
                    {
                        //move box over
                        object1.getDestination().increaseCol();

                        //move character over
                        getCharacter().getDestination().increaseCol();
                        
                        //increase move count
                        increaseCount();
                    }
                }
                else
                {
                    //any other object should be good for us to move
                    getCharacter().getDestination().increaseCol();
                    
                    //increase move count
                    increaseCount();
                }
            }

            //set the animation of the character
            getCharacter().setAnimation(Character.EAST);

            //remove key pressed
            keyboard.removeKeyPressed(MOVE_EAST);
        }
        else if (keyboard.hasKeyPressed(MOVE_NORTH))
        {
            object1 = level.getPhysicalLevelObject((int)getCharacter().getCol(), (int)getCharacter().getRow() - 1);

            //if the object to the north does not exist or isn't a wall
            if (object1 == null || !object1.isWall())
            {
                //if the object is a box check the object 1 row over
                if (object1 != null && object1.isBox())
                {
                    //check the object 2 rows over
                    object2 = level.getPhysicalLevelObject((int)getCharacter().getCol(), (int)getCharacter().getRow() - 2);

                    //if the next object is not a box or wall, it is a valid move
                    if (object2 == null || !object2.isBox() && !object2.isWall())
                    {
                        //move box over
                        object1.getDestination().decreaseRow();

                        //move character over
                        getCharacter().getDestination().decreaseRow();
                        
                        //increase move count
                        increaseCount();
                    }
                }
                else
                {
                    //any other object should be good for us to move
                    getCharacter().getDestination().decreaseRow();
                    
                    //increase move count
                    increaseCount();
                }
            }

            //set the animation of the character
            getCharacter().setAnimation(Character.NORTH);

            //remove key pressed
            keyboard.removeKeyPressed(MOVE_NORTH);
        }
        else if (keyboard.hasKeyPressed(MOVE_SOUTH))
        {
            object1 = level.getPhysicalLevelObject((int)getCharacter().getCol(), (int)getCharacter().getRow() + 1);

            //if the object to the south does not exist or isn't a wall
            if (object1 == null || !object1.isWall())
            {
                //if the object is a box check the object 1 row over
                if (object1 != null && object1.isBox())
                {
                    //check the object 2 rows over
                    object2 = level.getPhysicalLevelObject((int)getCharacter().getCol(), (int)getCharacter().getRow() + 2);

                    //if the next object is not a box or wall, it is a valid move
                    if (object2 == null || !object2.isBox() && !object2.isWall())
                    {
                        //move box over
                        object1.getDestination().increaseRow();

                        //move character over
                        getCharacter().getDestination().increaseRow();
                        
                        //increase move count
                        increaseCount();
                    }
                }
                else
                {
                    //any other object should be good for us to move
                    getCharacter().getDestination().increaseRow();
                    
                    //increase move count
                    increaseCount();
                }
            }

            //set the animation of the character
            getCharacter().setAnimation(Character.SOUTH);

            //remove key pressed
            keyboard.removeKeyPressed(MOVE_SOUTH);
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        if (getCharacter() != null)
        {
            getCharacter().render(graphics, getCharacter().getImage());
        }
    }
}