package in.maze.CheckpointRace.Events;

import in.maze.CheckpointRace.Race;
import in.maze.CheckpointRace.RaceTime;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RaceStartEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Race race;
    private RaceTime time;

    public RaceStartEvent(Player p, Race r, RaceTime rt)
    {
    	player = p;
    	race = r;
    	time = rt;
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
    
	public HandlerList getHandlers()
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
