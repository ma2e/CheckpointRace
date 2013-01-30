package in.maze.CheckpointRace;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Portal 
{
	private static final Material DEF_START_MAT = Material.IRON_BLOCK;
	private static final Material DEF_CHECKPOINT_MAT = Material.SNOW_BLOCK;
	private static final Material DEF_FINISH_MAT = Material.WOOL;
	
	private static final Material[] ALLOWED_FRAME_MATERIALS = { DEF_START_MAT, DEF_CHECKPOINT_MAT, DEF_FINISH_MAT };
	private static final BlockFace[] CHECK_FACES = { BlockFace.EAST, BlockFace.NORTH, BlockFace.DOWN }; 
	
	private static final int DEF_MAX_SIZE = 6;

	// DEPRECATED
	private Block block1;
	private Block block2;
	
	private Material material;
	private PortalType type;
	private boolean active;
	
	private Set<Block> frameBlocks = new HashSet<Block>();
	private Set<Block> portalBlocks = new HashSet<Block>();
	
	private Portal()
	{
	}
	
	public static Portal Create(Block block, BlockFace face)
	{
		// get block adjendent to clicked block
		Block tmp = block.getRelative(face);

		if(!tmp.isEmpty())
			return null;

		Portal p = new Portal();
		
		if(block.getType() == DEF_START_MAT)
			p.setPortalType(PortalType.START);
		else if(block.getType() == DEF_CHECKPOINT_MAT)
			p.setPortalType(PortalType.CHECKPOINT);
		else if(block.getType() == DEF_FINISH_MAT)
			p.setPortalType(PortalType.FINISH);
		else
			return null;
		
		int x = 0;
		while(tmp.getRelative(face, ++x).isEmpty())
		{
			if(x == DEF_MAX_SIZE)
				return null;
		}
		
		if(tmp.getRelative(face, x).getTypeId() != block.getTypeId())
			return null;
		
		for(BlockFace cFace : CHECK_FACES)
		{
			if(block.getRelative(cFace).getTypeId() == block.getTypeId() && block.getRelative(cFace.getOppositeFace()).getTypeId() == block.getTypeId())
			{
				int y = 0;
				while(tmp.getRelative(cFace, ++y).isEmpty())
				{
					if(y == DEF_MAX_SIZE)
						return null;
				}
				
				p.setLowerLeft(tmp.getRelative(cFace, --y));
				
				y = 0;
				while(p.getLowerLeft().getRelative(cFace.getOppositeFace(), ++y).isEmpty())
				{
					if(y == DEF_MAX_SIZE)
						return null;
				}
	
				p.setUpperRight(p.getLowerLeft().getRelative(cFace.getOppositeFace(), --y).getRelative(face, --x));

				for(int i = -1; i <= x + 1; i++)
					for(int j = -1; j <= y + 1; j++)
					{
						Block b = p.getLowerLeft().getRelative(cFace.getOppositeFace(), j).getRelative(face, i);

						// check if block is empty; frame may not be empty so make an exception for it
						if(b.isEmpty() && i != -1 && i != x + 1 && j != -1 && j != y + 1)
							p.portalBlocks.add(b);
						
						else
							switch(p.getPortalType())
							{
								case START		: 	if(b.getTypeId() == DEF_START_MAT.getId())
													{
														p.frameBlocks.add(b); 
														break;
													}
													else return null; 
								case CHECKPOINT	: 	if(b.getTypeId() == DEF_CHECKPOINT_MAT.getId())
													{
														p.frameBlocks.add(b); 
														break;
													}
													else return null; 
								case FINISH		: 	if(b.getTypeId() == DEF_FINISH_MAT.getId())
													{
														p.frameBlocks.add(b); 
														break;
													}
													else return null;
								default			:	return null;
							}
					}
				
				return p;
			}
		}

		return null;
	}
	
	public Block getLowerLeft()
	{
		return this.block1;
	}
	
	public Block getUpperRight()
	{
		return this.block2;
	}
	
	public PortalType getPortalType()
	{
		return this.type;
	}
	
	public Material getFrameMaterial()
	{
		return this.material;
	}
	
	public boolean isActive()
	{
		return this.active;
	}
	
	public void setLowerLeft(Block lowerLeft)
	{
		this.block1 = lowerLeft;
	}
	
	public void setUpperRight(Block upperRight)
	{
		this.block2 = upperRight;
	}
	
	public void setPortalType(PortalType pType)
	{
		this.type = pType;

		// make sure material is also changed accordingly 
		switch(pType)
		{
			case START 		: this.material = DEF_START_MAT; break;
			case CHECKPOINT	: this.material = DEF_CHECKPOINT_MAT; break;
			case FINISH		: this.material = DEF_FINISH_MAT; break;
		}
	}
	
	public void setActive(boolean value)
	{
		this.active = value;
	}
	
	public boolean isValid()
	{
		for(Block b : this.portalBlocks)
		{
			if(!b.isEmpty())
				return false;
		}
		for(Block b : this.frameBlocks)
		{
			if(b.getTypeId() != this.material.getId())
				return false;
		}
		return true;
	}
	
	
	/**
	 * @param block
	 * @return
	 */
	public boolean isInFrame(Block block)
	{
		return this.frameBlocks.contains(block);
	}
	
	
	/**
	 * @param loc Location to check
	 * @return If provided location is inside the portal (excluding frame)
	 */
	public boolean isInPortal(Location loc)
	{
		return this.portalBlocks.contains(loc.getBlock());
	}
	
	public boolean isInPortal(Player player)
	{
		return this.portalBlocks.contains(player.getLocation().getBlock());
	}

	/**
	 * Lag proof implementation for checking if a player moved through this portal
	 * @param from Last known location before the lag
	 * @param to First known location after the lag
	 * @return If the interpolated, direct way from first to second location went through the portal
	 */
	public boolean movedThroughPortal(Location from, Location to)
	{
		// get portal plane
		// check if normal coords are on different sides
		// check if cutting point is in BlockSpan
		
		if(isInPortal(from) || isInPortal(to))
			return true;
		
		double factor;
		
		if(block1.getX() == block2.getX())
		{
			// Plane X
			int x = block1.getX();
			
			if((from.getX() < x) && (to.getX() > x) ||
			   (from.getX() > x) && (to.getX() < x))
			{
				// get vector
				Vector v = to.getDirection().subtract(from.getDirection());
				v.normalize();
				
				factor = x/v.getX(); 
			}
			else
				return false;
		}
		else 
		if(block1.getY() == block2.getY())
		{
			// Plane Y
			int y = block1.getY();
		}
		else 
		if(block1.getZ() == block2.getZ())
		{
			// Plane Z
			int z = block1.getZ();
		}
		
		return false;
	}
}
