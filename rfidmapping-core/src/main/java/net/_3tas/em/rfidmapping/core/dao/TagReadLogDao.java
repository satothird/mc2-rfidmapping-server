/**
 * 
 */
package net._3tas.em.rfidmapping.core.dao;

import java.util.Date;
import java.util.List;

import net._3tas.em.rfidmapping.core.exception.NoSuchTagException;
import net._3tas.em.rfidmapping.core.model.TagReadLog;

/**
 * @author sat3
 *
 */
public interface TagReadLogDao{
	public List<TagReadLog> find(String tagId) throws NoSuchTagException;
	public List<TagReadLog> find(String tagId,Date startDate) throws NoSuchTagException;
	public List<TagReadLog> findByReadingSession(String readingSession);
	public List<TagReadLog> findAll();
	public List<String> listLoggedTagIDs();
	public void append(TagReadLog log);
	public void clear();
}
