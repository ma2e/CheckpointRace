package in.maze.CheckpointRace.Events;

import in.maze.CheckpointRace.Race;
import in.maze.CheckpointRace.RaceTime;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RaceCheckpointEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Race race;
    private RaceTime time;
    private int checkpoint;

    public RaceCheckpointEvent(Player p, Race r, RaceTime rt, int cp)
    {
    	player = p;
    	race = r;
    	time = rt;
    	checkpoint = cp;
    }

    public Race getRace()
    {
    	return race;
    }
    
    public Player getPlayer()
    {
    	return player;
    }
    
    public RaceTime getRaceTime()
    {
    	return time;
    }
    
    public int getCheckpoint()
    {
    	return checkpoint;
    }
    
	public HandlerList getHandlers()
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
