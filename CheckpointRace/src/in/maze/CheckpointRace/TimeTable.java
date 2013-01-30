package in.maze.CheckpointRace;

import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;

public class TimeTable
{
//	private HashMap<Player, Long> startTimes = new HashMap<Player, Long>();
//	private HashMap<Player, Long> finishTimes = new HashMap<Player, Long>();
//	private HashMap<Player, Long[]> splitTimes = new HashMap<Player, Long[]>();
//	private HashMap<Player, Date> timestamps = new HashMap<Player, Date>();
	
	private TreeMap<Player, RaceTime> times = new TreeMap<Player, RaceTime>();
	
	public TimeTable()
	{

	}
	
	public String[][] getRecordsForSigns(int count)
	{
		if(count < 1 || count > 999)
			return null;
		
		String[][] tmp = new String[count < times.size() ? count : times.size()][2];
		
		int i = 0;
		int namelength = 11; // Length - 1
		
		if(count > 9)
			--namelength;
		
		if(count > 99)
			--namelength;
		
		for(Player p : times.keySet())
		{
			tmp[i][0] = (i+1) + ") " + p.getName().substring(0, namelength < p.getName().length() ? namelength : p.getName().length());
			// MMMM:SS.m
			tmp[i][1] = times.get(p).toString(); 
			
			++i;
			
			if(i >= count)
				break;
		}
		
		return tmp;
	}
	
	public String[] getRecords()
	{
		return null;
	}
	
	public String[] getRecords(int count)
	{
		if(count < 1 || count > 999)
			return null;
		
		String[] tmp = new String[count < times.size() ? count : times.size()];
		
		int i = 0;
		
		for(Player p : times.keySet())
		{
			tmp[i] = times.get(p).toString();
					
			if(++i >= count)
				break;
		}
		
		return tmp;
	}
	
	public String getRecord(Player p)
	{
		return times.get(p).toString();
	}
	
	public long getRecordTime(Player p)
	{
		return times.get(p).getRaceTime();
	}
	
	public String[] getBestVirtual()
	{
		long best = times.firstEntry().getValue().getSplitTime(0);
		String name = "";
		
		long virtual = 0;
		
		// Race -> all split times -> get each best split time -> add up
		
		
		
		for(Player p : times.keySet())
		{
			for(long split : times.get(p).getSplitTimes())
			{
				if(split < best)
					;
			}
		}
		
		String[] tmp = {"Best Virtual", ""};
		
		return tmp;
	}
	
	public String getSplit(int i)
	{
		throw new NotImplementedException();
	}
	
}
