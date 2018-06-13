/**
 * 
 */
package net._3tas.em.rfidmapping.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net._3tas.em.rfidmapping.core.dao.CrossPointDao;
import net._3tas.em.rfidmapping.core.dao.CurrentTagLocationDao;
import net._3tas.em.rfidmapping.core.dao.ReferenceTagDao;
import net._3tas.em.rfidmapping.core.dao.TagReadLogDao;
import net._3tas.em.rfidmapping.core.exception.NoSuchTagException;
import net._3tas.em.rfidmapping.core.localization.FixedLocationUpdater;
import net._3tas.em.rfidmapping.core.model.Position3D;
import net._3tas.em.rfidmapping.core.model.ReferencePosition3D;
import net._3tas.em.rfidmapping.core.model.TagReadLog;

/**
 * @author sat3
 *
 */
public abstract class RFIDMappingCore{
	private LocationUpdater locationUpdater=null;
	
	public void acceptTagReadLog(TagReadLog tagReadLog){
		getLocationUpdater().accept(tagReadLog);
	}
	
	public void redoLocalizationFromRecordedLog(){
		getCurrentTagLocationDao().clear();
		getCrossPointDao().clear();
		List<TagReadLog> logs=getTagReadLogDao().findAll();
		getTagReadLogDao().clear();
		getLocationUpdater().accept(logs);
	}
	
	public List<String> getLoggedTagIDs(){
		return getTagReadLogDao().listLoggedTagIDs();
	}
	
	public List<TagReadLog> getTagReadLogs(String tagId) throws NoSuchTagException{
		return getTagReadLogDao().find(tagId);
	}
	
	public Position3D getCurrentTagLocation(String tagId) throws NoSuchTagException{
		Position3D estimatedPosition=getCurrentTagLocationDao().find(tagId);
		Map<String,ReferencePosition3D> referenceTags=getReferenceTagDao().findAll();
		if(referenceTags.keySet().contains(tagId)){
			throw new NoSuchTagException();
		}else{
			Map<Position3D,ReferencePosition3D> references=new HashMap<Position3D,ReferencePosition3D>();
			for(Map.Entry<String,ReferencePosition3D> referenceTag:referenceTags.entrySet()){
				try{
					references.put(getCurrentTagLocationDao().find(referenceTag.getKey()),referenceTag.getValue());
				}catch(NoSuchTagException e){}
			}
			return getBiasRemovalResult(estimatedPosition,references);
		}
	}
	
	public Map<String,Position3D> getAvailableTagsLocation(){
		Map<String,Position3D> estimatedPositions=getCurrentTagLocationDao().findAll();
		Map<String,ReferencePosition3D> referenceTags=getReferenceTagDao().findAll();
		Map<Position3D,ReferencePosition3D> references=new HashMap<Position3D,ReferencePosition3D>();
		for(Map.Entry<String,ReferencePosition3D> referenceTag:referenceTags.entrySet()){
			try{
				references.put(getCurrentTagLocationDao().find(referenceTag.getKey()),referenceTag.getValue());
			}catch(NoSuchTagException e){}
		}
		Map<String,Position3D> biasRemovalResults=new HashMap<String,Position3D>();
		for(Map.Entry<String,Position3D> estimatedPosition:estimatedPositions.entrySet()){
			if(!referenceTags.keySet().contains(estimatedPosition.getKey())){
				biasRemovalResults.put(estimatedPosition.getKey(),getBiasRemovalResult(estimatedPosition.getValue(),references));
			}
		}
		return biasRemovalResults;
	}
	
	private Position3D getBiasRemovalResult(Position3D estimatedPosition,Map<Position3D,ReferencePosition3D> references){
		double xBias=0,yBias=0,zBias=0;
		int count=0;
		for(Map.Entry<Position3D,ReferencePosition3D> reference:references.entrySet()){
			if(reference.getValue().isCovering(estimatedPosition)){
				xBias+=reference.getKey().getX()-reference.getValue().getX();
				yBias+=reference.getKey().getY()-reference.getValue().getY();
				zBias+=reference.getKey().getZ()-reference.getValue().getZ();
				count++;
			}
		}
		xBias/=(double)count;
		yBias/=(double)count;
		zBias/=(double)count;
		return new Position3D(estimatedPosition.getX()-xBias,estimatedPosition.getY()-yBias,estimatedPosition.getZ()-zBias);
	}
	
	public abstract TagReadLogDao getTagReadLogDao();
	public abstract CurrentTagLocationDao getCurrentTagLocationDao();
	public abstract CrossPointDao getCrossPointDao();
	public abstract ReferenceTagDao getReferenceTagDao();
	
	private LocationUpdater getLocationUpdater(){
		if(locationUpdater==null){
			locationUpdater=new FixedLocationUpdater(getTagReadLogDao(),getCurrentTagLocationDao(),getCrossPointDao());
		}
		return locationUpdater;
	}
}
