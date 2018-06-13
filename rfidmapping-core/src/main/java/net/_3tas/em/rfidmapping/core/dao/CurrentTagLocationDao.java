/**
 * 
 */
package net._3tas.em.rfidmapping.core.dao;

import java.util.Map;

import net._3tas.em.rfidmapping.core.exception.NoSuchTagException;
import net._3tas.em.rfidmapping.core.model.Position3D;

/**
 * @author sat3
 *
 */
public interface CurrentTagLocationDao{
	public Position3D find(String tagId) throws NoSuchTagException;
	public Map<String,Position3D> findAll();
	public void update(String tagId,Position3D currentLocation);
	public void clear();
}
