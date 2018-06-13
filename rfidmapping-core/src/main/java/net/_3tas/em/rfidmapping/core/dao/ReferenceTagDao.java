/**
 * 
 */
package net._3tas.em.rfidmapping.core.dao;

import java.util.Map;

import net._3tas.em.rfidmapping.core.model.ReferencePosition3D;

/**
 * @author sat3
 *
 */
public interface ReferenceTagDao{
	public Map<String,ReferencePosition3D> findAll();
	public void update(String tagId,ReferencePosition3D referencePosition);
	public void clear();
}
