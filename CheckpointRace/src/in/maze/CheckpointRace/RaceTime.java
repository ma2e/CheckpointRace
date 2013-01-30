package in.maze.CheckpointRace;

import java.util.Date;

public class RaceTime implements Comparable<RaceTime>
{
	private long startTime = 0;
	private long finishTime = 0;
	private long raceTime = 0;

	private long[] splits;
	
	
	public RaceTime(Race race)
	{
		this.splits = new long[race.getCheckpointCount()];
	}
	
	public RaceTime(int checkpoints)
	{
		this.splits = new long[checkpoints];
	}
	
	public static RaceTime Create(int checkpoints)
	{
		RaceTime r = new RaceTime(checkpoints);
		r.setStartTime(new Date().getTime());
		
		return r;
	}
	
	@Override
	public int compareTo(RaceTime rt)
	{
		if(Long.compare(this.raceTime, rt.getRaceTime()) == 0)
			return Long.compare(this.finishTime, rt.getTimeStamp());

		return Long.compare(this.raceTime, rt.getRaceTime());
	}
	
	@Override
	public String toString() 
	{
		// MMMM:SS.m

		// Sign: 4x15
		// Player name length: 16

		// in milliseconds
		int mils = (int)(raceTime % 1000);
		int secs = (int)(raceTime / 1000 % 60);
		int mins = (int)(raceTime / 1000 / 60);
		
		if(mins > 9999)
			return "*** ERROR ***";
				
		return mins + ":" + secs + "." + mils;
	}
	
	public long getRaceTime()
	{
		return ((finishTime < startTime || finishTime == 0) ? 0 : finishTime - startTime);
	}
	
	public long getTimeStamp()
	{
		return finishTime;
	}
	
	public long getStartTime()
	{
		return startTime;
	}
	
	public long getFinishTime()
	{
		return finishTime;
	}
	
	public long[] getSplitTimes()
	{
		return splits;
	}
	
	public long getSplitTime(int checkpoint)
	{
		if (checkpoint >= 0 && checkpoint < splits.length)
			return splits[checkpoint];
		
		return 0;
	}
	
	// use carefully
	public void setStartTime(long time)
	{
		startTime = time;
		updateRaceTime();
	}
	
	public void setFinishTime(long time)
	{
		finishTime = time;
		updateRaceTime();
	}
	
	public void updateStartTime()
	{
		startTime = new Date().getTime();
		updateRaceTime();
	}
	
	private void updateRaceTime()
	{
		raceTime = finishTime - startTime;
		
		if(finishTime == 0 || finishTime < startTime)
			raceTime = 0;
	}
}