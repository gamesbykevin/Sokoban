package com.gamesbykevin.sokoban.manager;

import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.menu.Menu;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.sokoban.engine.Engine;
import com.gamesbykevin.sokoban.level.Levels;
import com.gamesbykevin.sokoban.menu.CustomMenu;
import com.gamesbykevin.sokoban.menu.CustomMenu.*;
import com.gamesbykevin.sokoban.player.Player;
import com.gamesbykevin.sokoban.resources.GameAudio;
import com.gamesbykevin.sokoban.resources.GameFont;
import com.gamesbykevin.sokoban.resources.GameImages;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements IManager
{
    //where gameplay occurs
    private Rectangle window;
    
    //object containing all the levels
    private Levels levels;
    
    //the object representing the player in play
    private Player player;
    
    //background image
    private Image background;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine Engine for our game that contains all objects needed
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //set the audio depending on menu setting
        engine.getResources().setAudioEnabled(engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Sound) == CustomMenu.SOUND_ENABLED);
        
        //set the game window where game play will occur
        setWindow(engine.getMain().getScreen());
    }
    
    @Override
    public void reset(final Engine engine) throws Exception
    {
        //create object that will contain our levels
        if (levels == null)
            levels = new Levels(engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Difficulty));
        
        //create player if not exists
        if (player == null)
            player = new Player();
        
        //get background image if not already stored
        if (background == null)
            background = engine.getResources().getGameImage(GameImages.Keys.Background);
        
        //play music
        engine.getResources().playGameAudio(GameAudio.Keys.Music, true);
    }
    
    /**
     * Object containing all of the levels in the game
     * @return Object containing all of the levels in the game
     */
    public Levels getLevels()
    {
        return this.levels;
    }
    
    public Player getPlayer()
    {
        return this.player;
    }
            
    
    @Override
    public Rectangle getWindow()
    {
        return this.window;
    }
    
    @Override
    public void setWindow(final Rectangle window)
    {
        this.window = new Rectangle(window);
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        if (window != null)
            window = null;
        
        if (levels != null)
        {
            levels.dispose();
            levels = null;
        }
        
        if (player != null)
        {
            player.dispose();
            player = null;
        }
        
        try
        {
            //recycle objects
            super.finalize();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Update all elements
     * @param engine Our game engine
     * @throws Exception 
     */
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (getLevels() != null)
        {
            //update the levels
            getLevels().update(engine);
            
            //if we are done loading the levels
            if (!getLevels().isLoading())
            {
                if (getPlayer() != null)
                    getPlayer().update(engine);
            }
        }
        
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        if (background != null)
            graphics.drawImage(background, 0, 0, null);
        
        if (getLevels() != null)
        {
            getLevels().render(graphics);
            
            if (!getLevels().isLoading())
            {
                if (getPlayer() != null)
                    getPlayer().render(graphics);
            }
        }
    }
}