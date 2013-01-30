package in.maze.CheckpointRace;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.permissions.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CheckpointRace extends JavaPlugin implements Listener
{
	private final static String CPR_PATH = "CheckpointRace";
	
	private Logger log;
	private Map<Player, Boolean> playerInCreationMode = new HashMap<Player, Boolean>();
	private HashSet<Race> races = new HashSet<Race>();

	// This is a test
	public void onEnable()
	{
		log = this.getLogger();
		this.getServer().getPluginManager().registerEvents(this, this);
		
		try
		{
			if(new File(CPR_PATH + "races.bin").exists()) 
				races = SLAPI.load(CPR_PATH + File.separator + "races.bin");
			else
			{
				if(!new File(CPR_PATH).isDirectory())
				{
					new File(CPR_PATH).mkdir();
				
					log.info("First Start: Creating \"" + CPR_PATH + "\" directory");
				}
			}
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO: Read options file
		
		for(Player p : this.getServer().getOnlinePlayers())
			playerInCreationMode.put(p, false);
		
	}
	
	public void onDisable()
	{
		// TODO: restore Inventories
		try
		{
			SLAPI.save(races, CPR_PATH + File.separator + "races.bin");
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(event.isCancelled() || event.getFrom().getBlock() == event.getTo().getBlock())
			return;
		
		if(event.getPlayer().hasPermission(new Permission("checkpointrace.allowed")))
		{
			for(Race r : races)
			{
				if(r.getStart().isInPortal(event.getPlayer()))
					event.getPlayer().sendMessage("Race \""+r.getName()+"\" started");
				else if(r.getFinish().isInPortal(event.getPlayer()))
					event.getPlayer().sendMessage("Race \""+r.getName()+"\" finished");
				else for(int i = 0; i < r.getCheckpointCount(); i++)
				{
					if(r.getCheckpoint(i).isInPortal(event.getPlayer()))
						event.getPlayer().sendMessage("Race \""+r.getName()+"\" started");
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEvent event)
	{
		if(event.isCancelled())
			return;
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && playerInCreationMode.get(event.getPlayer()) && event.getMaterial() == Material.WATCH)
		{
			//log.info("Compass: "+event.getPlayer().getName());
			
			//event.getPlayer().sendMessage("Start set:" + event.getBlockFace().toString());
			Portal portal = Portal.Create(event.getClickedBlock(), event.getBlockFace());
			
			if(portal != null)
			{
				Player p = event.getPlayer();
				Race race;
				
				p.sendMessage("Portal "+portal.getPortalType().toString()+" successfully created");
				
				switch(portal.getPortalType())
				{
					case START :
						race = new Race();
						race.setStart(portal);
					
						p.setMetadata("current", new FixedMetadataValue(this, race));
						break;
					
					case CHECKPOINT :
						race = (Race)getMetadata(p, "current", this);
						
						// TODO: Add error message
						if(race == null || race.getStart() == null)
							return;
						
						race.addCheckpoint(portal);
						
						p.setMetadata("current", new FixedMetadataValue(this, race));
						break;
						
					case FINISH :
						race = (Race)getMetadata(p, "current", this);
						
						// TODO: Add error message
						if(race == null || race.getStart() == null)
							return;
						
						race.setFinish(portal);
						race.setActive(true);
						
						p.setMetadata("current", new FixedMetadataValue(this, null));
						
						races.add(race);
						
						p.sendMessage("Race track successfully created");
						break;
						
					default: 
						p.sendMessage("Error: Invalid race track (wrong portal order?");
				}
			}
			else
				event.getPlayer().sendMessage("Error creating portal: invalid frame");
		}
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		playerInCreationMode.put(event.getPlayer(), false);
	}

	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event)
	{
		playerInCreationMode.remove(event.getPlayer());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			
			if(cmd.getName().equalsIgnoreCase("cprace") || cmd.getName().equalsIgnoreCase("cpr"))
			{
				if(args.length > 0)
				{
					if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c"))
					{
						playerInCreationMode.put(p, !playerInCreationMode.get(p));
						p.sendMessage(playerInCreationMode.get(p) ? "Checkpoint Race: construction mode activated" : "Checkpoint Race: construction mode deactivated");
					}
					else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("d"))
					{
						
					}
					else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?"))
					{
						showHelp(p);
					}
					else if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i"))
					{
						
					}
					else if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l"))
					{
						
					}
					else if(args[0].equalsIgnoreCase("name") || args[0].equalsIgnoreCase("n"))
					{
						
					}
					else if(args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("r"))
					{
						
					}
					else if(args[0].equalsIgnoreCase("times") || args[0].equalsIgnoreCase("t"))
					{
						
					}
					else
						sender.sendMessage("Error: Unknown command");
				}
				else
					showCommands(p);
			}
		}
		else
			sender.sendMessage("Error: This command cannot be run via console");
		
		return true;
	}

	private void showHelp(Player p)
	{
		
	}
	
	private void showCommands(Player p)
	{
		p.sendMessage(ChatColor.YELLOW + "--------- " +  ChatColor.WHITE + "Checkpoint Race Help" + ChatColor.YELLOW + " ------------------");
		p.sendMessage(ChatColor.GOLD + "Usage:" + ChatColor.WHITE + " /cprace <command> [<id>] [<option>]");
		p.sendMessage("");
		p.sendMessage("Available Commands:");
		p.sendMessage(ChatColor.GOLD + "create:" + ChatColor.WHITE + " toggles creation mode");
		p.sendMessage(ChatColor.GOLD + "delete:" + ChatColor.WHITE + " deletes specified race");
		p.sendMessage(ChatColor.GOLD + "info:" + ChatColor.WHITE + " shows race information");
		p.sendMessage(ChatColor.GOLD + "list:" + ChatColor.WHITE + " shows all registered races");
		p.sendMessage(ChatColor.GOLD + "name:" + ChatColor.WHITE + " names current race");
		p.sendMessage(ChatColor.GOLD + "reset:" + ChatColor.WHITE + " reset times on given race");
	}
	
	private Object getMetadata(Player player, String key, Plugin plugin)
	{
		List<MetadataValue> values = player.getMetadata(key);
		
		for(MetadataValue value : values)
		{
			if(value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName()))
				return value.value();
		}
		
		return null;
	}
	
//	@EventHandler
//	public void onStartRace(RaceStartEvent event)
//	{
//		
//	}
	
	// StartEvent
	// FinishEvent
	// SplitEvent
	
}
