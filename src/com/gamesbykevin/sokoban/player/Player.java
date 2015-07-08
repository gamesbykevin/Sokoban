package com.gamesbykevin.sokoban.player;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.sokoban.engine.Engine;
import com.gamesbykevin.sokoban.level.Level;
import com.gamesbykevin.sokoban.level.Levels;
import com.gamesbykevin.sokoban.level.object.Character;
import com.gamesbykevin.sokoban.level.object.LevelObject;
import com.gamesbykevin.sokoban.resources.GameAudio;
import com.gamesbykevin.sokoban.resources.GameImages.Keys;
import com.gamesbykevin.sokoban.shared.IElement;
import com.gamesbykevin.sokoban.shared.Shared;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

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
    private static final int LEVEL_NEW = KeyEvent.VK_N;
    
    //display the notification message for this time
    private static final long NOTIFICATION_DELAY = Timers.toNanoSeconds(5000L);
    
    //timer to show notification message
    private Timer timer;
    
    //the number of moves in the current level
    private int count = 0;
    
    //the image we render the total number of moves
    private BufferedImage countImage;
    
    //image to be displayed to user for a few seconds
    private BufferedImage notificationImage;
    
    //image to be dislayed when a level is solved
    private BufferedImage victoryImage;
    
    //location of countImage
    private Point locationCount;
    
    //location of notificationImage
    private Point locationNotification;
    
    //location of victory image
    private Point locationVictory;
    
    //does the player have the level solved
    private boolean victory = false;
    
    //image dimensions
    private static final int IMAGE_WIDTH = 35;
    private static final int IMAGE_HEIGHT = 15;
    
    //the pixel offset to draw the stat image(s)
    private static final int OFFSET_X = 3;
    private static final int OFFSET_Y = 3;
    
    public Player() throws Exception
    {
        //create the character
        this.character = new Character();
        this.character.setDimensions();
        this.character.setWidth(this.character.getWidth() /2);
        this.character.setHeight(this.character.getHeight() / 2);
        
        //create timer
        this.timer = new Timer(NOTIFICATION_DELAY);
    }
    
    /**
     * Set the number of moves performed by the player
     * @param count The total number of moves made
     */
    private void setCount(final int count)
    {
        this.count = count;
        
        //draw image
        renderCountImage();
    }
    
    /**
     * Increase the number of moves
     */
    public void increaseCount()
    {
        //increase count
        setCount(getCount() + 1);
    }
    
    /**
     * Set the number of moves to 0
     */
    public void resetCount()
    {
        setCount(0);
        
        //reset notification display timer
        this.timer.reset();
        
        //flag victory false
        this.victory = false;
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
     * Set the character's starting location, this is performed when starting a level
     * @param start The object containing column, row
     */
    public void setCharacterStart(final Cell start) throws Exception
    {
        //set starting point
        this.character.setStart(start);
        
        //also set as current position
        this.character.reset();
        
        //reset the counter as well
        this.resetCount();
    }
    
    @Override
    public void dispose()
    {
        if (character != null)
        {
            character.dispose();
            character = null;
        }
        
        if (countImage != null)
        {
            countImage.flush();
            countImage = null;
        }
        
        if (notificationImage != null)
        {
            notificationImage.flush();
            notificationImage = null;
        }
        
        if (victoryImage != null)
        {
            victoryImage.flush();
            victoryImage = null;
        }
        
        locationCount = null;
        locationNotification = null;
        locationVictory = null;
    }
    
    /**
     * Draw notification image
     */
    private void renderNotificationImage()
    {
        if (notificationImage != null)
        {
            //get graphics object to write image
            Graphics2D g2d = this.notificationImage.createGraphics();
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, Shared.ORIGINAL_WIDTH, IMAGE_HEIGHT * 3);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Press 'R' to reset the current level.", (OFFSET_X * 5), IMAGE_HEIGHT - OFFSET_Y);
            g2d.drawString("Press 'N' to choose a random new level.", (OFFSET_X * 5), (IMAGE_HEIGHT * 2) - OFFSET_Y);
            g2d.drawString("Press 'Esc' to access the menu.", (OFFSET_X * 5), (IMAGE_HEIGHT * 3) - OFFSET_Y);
        }
    }
    
    /**
     * Draw player moves count image
     */
    private void renderCountImage()
    {
        if (countImage != null)
        {
            //get graphics object to write image
            Graphics2D g2d = this.countImage.createGraphics();
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
            g2d.setColor(Color.WHITE);
            g2d.drawString("" + getCount(), OFFSET_X, IMAGE_HEIGHT - OFFSET_Y);
        }
    }
    
    /**
     * Draw victory image
     */
    private void renderVictoryImage()
    {
        if (victoryImage != null)
        {
            //get graphics object to write image
            Graphics2D g2d = this.victoryImage.createGraphics();
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, Shared.ORIGINAL_WIDTH, IMAGE_HEIGHT);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Congratulations You Win!!", (OFFSET_X * 5), IMAGE_HEIGHT - OFFSET_Y);
        }
    }
    
    private void renderImages(final Rectangle screen)
    {
        if (this.countImage == null)
        {
            //create count image
            this.countImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            
            //the place to draw our image
            this.locationCount = new Point(
                screen.x + screen.width - IMAGE_WIDTH - OFFSET_X,
                screen.y + IMAGE_HEIGHT + OFFSET_Y
            );
            
            //draw image
            renderCountImage();
        }
        
        if (this.victoryImage == null)
        {
            //create victory image
            this.victoryImage = new BufferedImage(Shared.ORIGINAL_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            
            //the place to draw our victory image
            this.locationVictory = new Point(
                screen.x,
                screen.y + (IMAGE_HEIGHT * 3) + OFFSET_Y
            );
            
            //draw image
            renderVictoryImage();
        }
        
        if (this.notificationImage == null)
        {
            //create notification image
            this.notificationImage = new BufferedImage(Shared.ORIGINAL_WIDTH, IMAGE_HEIGHT * 3, BufferedImage.TYPE_INT_ARGB);
            
            //the place to draw our notification image
            this.locationNotification = new Point(
                screen.x,
                screen.y + (IMAGE_HEIGHT * 3) + OFFSET_Y
            );
            
            //draw image
            renderNotificationImage();
        }
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //don't continue if character does not exist
        if (getCharacter() == null)
            return;
        
        //get the current level
        Level level = engine.getManager().getLevels().getLevel();
        
        //if the level has been solved, flag victory message for display
        if (level.hasCompleted() && !victory)
        {
            //flag victory
            victory = true;
            
            //reset timer so notification message will show
            timer.reset();
            
            //play victory sound effect
            engine.getResources().playGameAudio(GameAudio.Keys.Win);
        }
        
        //create, draw images as needed
        renderImages(engine.getManager().getWindow());
        
        //update timer if not complete and we haven't solved level
        if (!timer.hasTimePassed() && !victory)
            timer.update(engine.getMain().getTime());
        
        if (getCharacter().getImage() == null)
            getCharacter().setImage(engine.getResources().getGameImage(Keys.SpriteSheet));

        //set the height to half of the original
        getCharacter().setDimensions();
        getCharacter().setWidth(getCharacter().getWidth() / 2);
        getCharacter().setHeight(getCharacter().getHeight() / 2);

        //set coordinates based on col, row location
        getCharacter().setX(level.getStartX(getCharacter()) + (Level.DEFAULT_DIMENSION / 2) - (getCharacter().getWidth() / 2));
        getCharacter().setY(level.getStartY(getCharacter()) + (Level.DEFAULT_DIMENSION / 2) - (getCharacter().getHeight() / 2));

        //update character animation, etc....
        getCharacter().update(engine);
        
        //if the character is at their destination and all other objects are, the character can be moved
        if (getCharacter().hasDestination() && !level.hasMobileObjects())
            checkInput(engine.getKeyboard(), engine.getManager().getLevels());
    }
    
    /**
     * Check human input to change the board
     * @param keyboard Object containing human keyboard input
     * @param levels Object containing all the levels
     */
    private void checkInput(final Keyboard keyboard, final Levels levels) throws Exception
    {
        //get the current level
        Level level = levels.getLevel();
        
        //has the level been completed
        final boolean completed = level.hasCompleted();
        
        //objects we will need to check to determine movement
        final LevelObject object1;
        final LevelObject object2;

        if (keyboard.hasKeyReleased(LEVEL_RESET) && !completed)
        {
            //reset character
            getCharacter().reset();
            
            //reset level
            level.reset();
            
            //reset moves count
            this.resetCount();
            
            //remove key pressed
            keyboard.removeKeyReleased(LEVEL_RESET);
        }
        else if (keyboard.hasKeyReleased(LEVEL_NEW))
        {
            //set a new random level
            levels.assignNewLevel();
            
            //remove key pressed
            keyboard.removeKeyReleased(LEVEL_NEW);
        }
        else if (keyboard.hasKeyPressed(MOVE_WEST) && !completed)
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
        else if (keyboard.hasKeyPressed(MOVE_EAST) && !completed)
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
        else if (keyboard.hasKeyPressed(MOVE_NORTH) && !completed)
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
        else if (keyboard.hasKeyPressed(MOVE_SOUTH) && !completed)
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
    public void render(final Graphics graphics) throws Exception
    {
        //draw character
        if (getCharacter() != null)
            getCharacter().render(graphics, getCharacter().getImage());
        
        //draw player moves count
        if (countImage != null)
            graphics.drawImage(countImage, locationCount.x, locationCount.y, null);
        
        //draw notification message
        if (notificationImage != null && !timer.hasTimePassed())
            graphics.drawImage(notificationImage, locationNotification.x, locationNotification.y, null);
        
        //draw victory message
        if (victoryImage != null && victory)
            graphics.drawImage(victoryImage, locationVictory.x, locationVictory.y, null);
    }
}