/**
 * 
 */
package net._3tas.em.rfidmapping.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net._3tas.em.rfidmapping.core.RFIDMappingCore;
import net._3tas.em.rfidmapping.core.dao.CrossPointDao;
import net._3tas.em.rfidmapping.core.dao.CurrentTagLocationDao;
import net._3tas.em.rfidmapping.core.dao.ReferenceTagDao;
import net._3tas.em.rfidmapping.core.dao.TagReadLogDao;

/**
 * @author sat3
 *
 */
@Service
public class RFIDMappingService extends RFIDMappingCore{
	@Autowired
	private TagReadLogDao tagReadLogDao;
	@Autowired
	private CurrentTagLocationDao currentTagLocationDao;
	@Autowired
	private CrossPointDao crossPointDao;
	@Autowired
	private ReferenceTagDao referenceTagDao;

	@Override
	public TagReadLogDao getTagReadLogDao(){
		return tagReadLogDao;
	}

	@Override
	public CurrentTagLocationDao getCurrentTagLocationDao(){
		return currentTagLocationDao;
	}

	@Override
	public CrossPointDao getCrossPointDao(){
		return crossPointDao;
	}
	
	@Override
	public ReferenceTagDao getReferenceTagDao(){
		return referenceTagDao;
	}
}
