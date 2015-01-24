package com.gamesbykevin.sokoban.level;

import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.resources.Progress;

import com.gamesbykevin.sokoban.engine.Engine;
import com.gamesbykevin.sokoban.level.object.*;
import com.gamesbykevin.sokoban.resources.GameText.Keys;
import com.gamesbykevin.sokoban.shared.IElement;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class will load/create the levels from the text file
 * @author GOD
 */
public final class Levels implements Disposable, IElement
{
    //the list containing the levels
    private List<Level> levels;
    
    //the index of the current level
    private int index = 0;
    
    //the largest size of level we want
    private static final int MAX_DIMENSIONS = 16;
    
    //track our progress towards loading the levels
    private Progress progress;
    
    //the most recet line accessed while loading the levels
    private int recentLine = 0;
    
    public Levels()
    {
        //create new empty list of levels
        this.levels = new ArrayList<>();
    }
    
    /**
     * Get the current Level
     * @return The current level
     */
    public Level getLevel()
    {
        return this.levels.get(index);
    }
    
    @Override
    public void dispose()
    {
        if (levels != null)
        {
            for (int i = 0; i < levels.size(); i++)
            {
                levels.get(i).dispose();
                levels.set(i, null);
            }
            
            levels.clear();
            levels = null;
        }
    }
    
    /**
     * Add level to the list
     * @param level The level we want to add
     */
    private void add(final Level level)
    {
        this.levels.add(level);
    }
    
    /**
     * Create the levels
     * @param lines All of the lines in the text file
     * @param random Object used to make random decisions
     */
    private void createLevels(final List<String> lines, final Random random) throws Exception
    {        
        //did we locate the first line of the level
        boolean began = false;
        
        //did we find the ending of a level
        boolean ended = false;
        
        //the maximum number of columns in a row
        int maxCols = 0;
        
        //line where the current level starts
        int start = 0;
        
        //check every line
        for (int i = recentLine; i < lines.size(); i++)
        {
            //get the current line
            String line = lines.get(i);
            
            if (line.length() > 0)
            {
                //check the first character of the current line
                switch (line.substring(0, 1))
                {
                    //check if the first character starts with one of the level keys below
                    case Level.KEY_WALL:
                    case Level.KEY_PLAYER:
                    case Level.KEY_PLAYER_ON_GOAL:
                    case Level.KEY_BOX:
                    case Level.KEY_BOX_ON_GOAL:
                    case Level.KEY_GOAL:
                    case Level.KEY_FLOOR:
                        //if we did not find the level start yet mark the start position
                        if (!began)
                        {
                            //store start line
                            start = i;

                            //flag we have found the first line of the level
                            began = true;
                        }

                        //find the row with the most columns
                        if (line.length() > maxCols)
                            maxCols = line.length();
                        break;

                    //the line does not start with a level key, check if the level is to be created
                    default:

                        //if we already found the start, this is the end
                        if (began)
                            ended = true;
                        break;
                }
            }
            
            //if we found the beginning and the end, create level
            if (began && ended)
            {
                //only create level if within the allowed dimension size
                if (maxCols <= MAX_DIMENSIONS && (i - start) <= MAX_DIMENSIONS)
                {
                    //create the level
                    createLevel(start, i - 1, maxCols, lines, random);
                }

                //store recent line, for next check
                recentLine = i;
                
                //exit loop so we can update progress
                break;
            }
            
            //update progress count
            progress.setCount(i + 1);
        }
    }
    
    public boolean isLoading()
    {
        //if the progress is not complete, we are still loading
        return (!progress.isComplete());
    }
    
    /**
     * Create the level from the array list of lines from the text file
     * @param start Starting line position of level
     * @param finish Finish line position of level
     * @param maxCols The maximum column dimension for this level
     * @param lines Arraylist containing the line text to create a level
     * @param random Object used to make random decisions
     * @throws Exception If unable to create level
     */
    private void createLevel(final int start, final int finish, final int maxCols, final List<String> lines, final Random random) throws Exception
    {
        //calculate the number of rows in the level
        final int rows = (finish + 1) - start;
        
        //object representing the level
        Level level = new Level();
        
        //pick random floor
        LevelObject.Type floorType = Floor.FLOORS[random.nextInt(Floor.FLOORS.length)];
        
        //pick random walls
        LevelObject.Type wallType = Wall.WALLS[random.nextInt(Wall.WALLS.length)];
        
        //pick random box
        LevelObject.Type boxType = Box.BOXES[random.nextInt(Box.BOXES.length)];
        
        //pick random goal
        LevelObject.Type goalType = Goal.GOALS[random.nextInt(Goal.GOALS.length)];
        
        //for now start coordinates are (0,0)
        level.setLocation(0, 0);
        
        for (int row = 0; row < rows; row++)
        {
            //get the current line
            String line = lines.get(start + row);
            
            //check every character in this line
            for (int col = 0; col < maxCols; col++)
            {
                if (col >= line.length())
                {
                    //if we past the current line length, this will be a floor
                    addObject(new Floor(floorType), col, row, level);
                }
                else
                {
                    switch (line.substring(col, col + 1))
                    {
                        case Level.KEY_WALL:
                            addObject(new Wall(wallType), col, row, level);
                            break;

                        case Level.KEY_BOX:
                            addObject(new Box(boxType), col, row, level);
                            break;

                        case Level.KEY_BOX_ON_GOAL:
                            //add goal first
                            addObject(new Goal(goalType), col, row, level);
                            
                            //then add box on top
                            LevelObject object = new Box(boxType);
                            object.setAnimation(Box.ON_GOAL);
                            addObject(object, col, row, level);
                            break;

                        case Level.KEY_GOAL:
                            addObject(new Goal(goalType), col, row, level);
                            break;
                            
                        case Level.KEY_PLAYER_ON_GOAL:
                            addObject(new Goal(goalType), col, row, level);
                            
                            //set start location for character
                            level.setStart(col, row);
                            break;

                        case Level.KEY_FLOOR:
                            addObject(new Floor(floorType), col, row, level);
                            break;
                            
                        case Level.KEY_PLAYER:
                            addObject(new Floor(floorType), col, row, level);
                            
                            //set start location for character
                            level.setStart(col, row);
                            break;

                        default:
                            throw new Exception("Character not found '" + line.substring(col, col + 1) + "', line = " + (start+row) + ", column = " + col);
                    }
                }
            }
        }
        
        //create random floor for the level
        LevelObject floor = new Floor(floorType);
        
        //set the dimensions of the object
        floor.setWidth(Level.DEFAULT_DIMENSION);
        floor.setHeight(Level.DEFAULT_DIMENSION);
        
        //assign floor to level
        level.setFloor(floor);
        
        //add level to list
        add(level);
    }
    
    /**
     * Add level object to the level
     * @param object Object we want to add
     * @param col Column location
     * @param row Row location
     * @param level The level we want to add the level object to
     */
    private void addObject(final LevelObject object, final int col, final int row, final Level level)
    {
        //set (col, row) location
        object.setCol(col);
        object.setRow(row);

        //the goal is half the size of the default dimensions
        if (object.isGoal())
        {
            //set the dimensions of the object
            object.setWidth(Level.DEFAULT_DIMENSION / 2);
            object.setHeight(Level.DEFAULT_DIMENSION / 2);
        }
        else
        {
            //set the dimensions of the object
            object.setWidth(Level.DEFAULT_DIMENSION);
            object.setHeight(Level.DEFAULT_DIMENSION);
        }

        //set coordinates
        object.setX(level.getStartX(object) + (Level.DEFAULT_DIMENSION / 2) - (object.getWidth() / 2));
        object.setY(level.getStartX(object) + (Level.DEFAULT_DIMENSION / 2) - (object.getHeight() / 2));

        //add object to level
        level.add(object);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //setup progress bar if not set
        if (progress == null)
        {
            //create new progress bar of specified size
            progress = new Progress(engine.getResources().getGameText(Keys.Levels).getLines().size());
            
            //set the description
            progress.setDescription("Loading levels...");
            
            //set window screen
            progress.setScreen(engine.getManager().getWindow());
        }
        
        //continue until we have completed our progress
        if (isLoading())
        {
            //if the levels do not exist we will create them
            createLevels(engine.getResources().getGameText(Keys.Levels).getLines(), engine.getRandom());
            
            //if no longer loading set a random level for now
            if (!isLoading())
            {
                //pick random level for now
                this.index = engine.getRandom().nextInt(levels.size());
            }
        }
        else
        {
            //update the level
            getLevel().update(engine);
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        if (progress != null)
        {
            if (isLoading())
            {
                //if we are still loading levels draw progress
                progress.render(graphics);
            }
            else
            {
                if (!levels.isEmpty())
                {
                    //draw the level
                    getLevel().render(graphics);
                }
            }
        }
    }
}