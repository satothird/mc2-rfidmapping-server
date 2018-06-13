/**
 * 
 */
package net._3tas.em.rfidmapping.core.localization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import net._3tas.em.rfidmapping.core.LocationUpdater;
import net._3tas.em.rfidmapping.core.dao.CrossPointDao;
import net._3tas.em.rfidmapping.core.dao.CurrentTagLocationDao;
import net._3tas.em.rfidmapping.core.dao.TagReadLogDao;
import net._3tas.em.rfidmapping.core.exception.NoSuchTagException;
import net._3tas.em.rfidmapping.core.model.Position3D;
import net._3tas.em.rfidmapping.core.model.TagReadLog;
import net._3tas.em.rfidmapping.core.model.WeightedPosition3D;

/**
 * @author sat3
 *
 */
public class FixedLocationUpdater implements LocationUpdater{
	private final TagReadLogDao tagReadLogDao;
	private final CurrentTagLocationDao currentTagLocationDao;
	private final CrossPointDao crossPointDao;
	private final double crossingThreshold;
	private final double distanceLimit;
	private final BiFunction<TagReadLog,TagReadLog,Double> weightCalculator;

	public FixedLocationUpdater(TagReadLogDao tagReadLogDao,CurrentTagLocationDao currentTagLocationDao,CrossPointDao crossPointDao){
		this.tagReadLogDao=tagReadLogDao;
		this.currentTagLocationDao=currentTagLocationDao;
		this.crossPointDao=crossPointDao;
		crossingThreshold=0.1;
		distanceLimit=Double.MAX_VALUE;
		weightCalculator=(TagReadLog a,TagReadLog b)->1.0;
	}

	@Override
	public void accept(TagReadLog tagReadLog){
		try{
			List<TagReadLog> existingLogs;
			try{
				existingLogs=tagReadLogDao.find(tagReadLog.getTagId());
			}catch(NoSuchTagException e){
				existingLogs=new ArrayList<TagReadLog>();
			}
			if(existingLogs.size()!=0){
				boolean pointAdded=false;
				for(TagReadLog existingLog:existingLogs){
					Position3D crossPoint=tagReadLog.getReaderLocation().getCrossPointWith(existingLog.getReaderLocation(),crossingThreshold,distanceLimit);
					if(crossPoint!=null){
						crossPointDao.append(tagReadLog.getTagId(),new WeightedPosition3D(crossPoint,weightCalculator.apply(existingLog,tagReadLog)));
						pointAdded=true;
					}
				}
				if(pointAdded){
					currentTagLocationDao.update(tagReadLog.getTagId(),crossPointDao.getWeightedCentroid(tagReadLog.getTagId()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		tagReadLogDao.append(tagReadLog);
	}
	
	@Override
	public void accept(List<TagReadLog> tagReadLogs){
		for(TagReadLog tagReadLog:tagReadLogs){
			try{
				List<TagReadLog> existingLogs;
				try{
					existingLogs=tagReadLogDao.find(tagReadLog.getTagId());
				}catch(NoSuchTagException e){
					existingLogs=new ArrayList<TagReadLog>();
				}
				if(existingLogs.size()!=0){
					for(TagReadLog existingLog:existingLogs){
						Position3D crossPoint=tagReadLog.getReaderLocation().getCrossPointWith(existingLog.getReaderLocation(),crossingThreshold,distanceLimit);
						if(crossPoint!=null){
							crossPointDao.append(tagReadLog.getTagId(),new WeightedPosition3D(crossPoint,weightCalculator.apply(existingLog,tagReadLog)));
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			tagReadLogDao.append(tagReadLog);
		}
		for(String tagId:tagReadLogDao.listLoggedTagIDs()){
			Position3D position=crossPointDao.getWeightedCentroid(tagId);
			if(position!=null){
				currentTagLocationDao.update(tagId,position);
			}
		}
	}
}
