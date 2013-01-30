package in.maze.CheckpointRace;

import java.util.HashSet;
import java.util.TreeSet;
import java.util.Vector;

import org.bukkit.entity.Player;

public class Race 
{
	private String name;
	
	private Portal start;
	private Portal finish;

	private Vector<Portal> checkpoints = new Vector<Portal>();
	private HashSet<Player> runners = new HashSet<Player>();
	private TreeSet<RaceTime> times = new TreeSet<RaceTime>();
	
	private boolean active;
	
	public Race()
	{

	}

	public Portal getStart()
	{
		return start;
	}

	public Portal getFinish()
	{
		return finish;
	}

	public int getRunnerCount()
	{
		return runners.size();
	}
	
	public Portal getCheckpoint(int i)
	{
		return checkpoints.get(i);
	}
	
	public int getCheckpointCount()
	{
		return checkpoints.size();
	}
	
	public int getCheckpointNumber(Portal P)
	{
		return checkpoints.indexOf(P);
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isActive()
	{
		return active;
	}

	public boolean isInUse()
	{
		return !runners.isEmpty();
	}
	
	public boolean isRunning(Player player)
	{
		return runners.contains(player);
	}
	
	public boolean isValid()
	{
		return (start != null && start.isValid() && finish != null && finish.isValid());
	}
	
	public void setStart(Portal start)
	{
		this.start = start;
	}

	public void setFinish(Portal finish)
	{
		this.finish = finish;
	}

	public boolean setActive(boolean active)
	{
		if(this.isValid())
		{
			runners.clear();
			this.active = active;
			// TODO: Fire RaceChangeEvent
			
			return true;
		}
		return false;
	}
	
	public void addCheckpoint(Portal p)
	{
		checkpoints.add(p);
	}
	
	public void removeCheckpoint(int i)
	{
		checkpoints.removeElementAt(i);
	}
	
	public boolean removeCheckpoint(Portal p)
	{
		return checkpoints.remove(p);
	}
	
	public void removeAllCheckpoints()
	{
		checkpoints.removeAllElements();
	}
	
	public void addRunner(Player player)
	{
		runners.add(player);
	}
	
	public boolean removeRunner(Player player)
	{
		return runners.remove(player);
	}

}
