/**
 * 
 */
package net._3tas.em.rfidmapping.core.dao;

import java.util.List;

import net._3tas.em.rfidmapping.core.model.Position3D;
import net._3tas.em.rfidmapping.core.model.WeightedPosition3D;

/**
 * @author sat3
 *
 */
public interface CrossPointDao{
	public List<WeightedPosition3D> find(String tagId);
	public Position3D getWeightedCentroid(String tagId);
	public void append(String tagId,WeightedPosition3D point);
	public void delete(String tagId);
	public void clear();
}
