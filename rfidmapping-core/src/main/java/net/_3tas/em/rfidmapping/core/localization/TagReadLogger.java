/**
 * 
 */
package net._3tas.em.rfidmapping.core.localization;

import net._3tas.em.rfidmapping.core.LocationUpdater;
import net._3tas.em.rfidmapping.core.dao.TagReadLogDao;
import net._3tas.em.rfidmapping.core.model.TagReadLog;

/**
 * @author sat3
 *
 */
public class TagReadLogger implements LocationUpdater{
	private final TagReadLogDao tagReadLogDao;
	
	public TagReadLogger(TagReadLogDao tagReadLogDao){
		this.tagReadLogDao=tagReadLogDao;
	}

	@Override
	public void accept(TagReadLog tagReadLog){
		tagReadLogDao.append(tagReadLog);
	}
}
