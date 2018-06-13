/**
 * 
 */
package net._3tas.em.rfidmapping.core.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sat3
 *
 */
public class TagReadLog implements Serializable{
	private static final long serialVersionUID=1L;
	private final int logId;
	private final String tagId;
	private final double rssi;
	private final InfiniteVector3D readerLocation;
	private final Date readDate;
	private final String readingSession;
	private final boolean recorded;
	
	public TagReadLog(int logId,String tagId,double rssi,InfiniteVector3D readerLocation,Date readDate,String readingSession){
		if(tagId==null || readerLocation==null || readDate==null || readingSession==null){
			throw new IllegalArgumentException();
		}
		this.logId=logId;
		this.tagId=tagId;
		this.rssi=rssi;
		this.readerLocation=readerLocation;
		this.readDate=readDate;
		this.readingSession=readingSession;
		this.recorded=true;
	}
	
	public TagReadLog(int logId,String tagId,double rssi,double readerX,double readerY,double readerZ,double readerAlpha,double readerDelta,Date readDate,String readingSession){
		this(logId,tagId,rssi,new InfiniteVector3D(new Position3D(readerX,readerY,readerZ),new Direction3D(readerAlpha,readerDelta)),readDate,readingSession);
	}
	
	public TagReadLog(String tagId,double rssi,InfiniteVector3D readerLocation,Date readDate,String readingSession){
		if(tagId==null || readerLocation==null || readDate==null || readingSession==null){
			throw new IllegalArgumentException();
		}
		this.logId=-1;
		this.tagId=tagId;
		this.rssi=rssi;
		this.readerLocation=readerLocation;
		this.readDate=readDate;
		this.readingSession=readingSession;
		this.recorded=false;
	}

	public TagReadLog(String tagId,double rssi,double readerX,double readerY,double readerZ,double readerAlpha,double readerDelta,Date readDate,String readingSession){
		this(tagId,rssi,new InfiniteVector3D(new Position3D(readerX,readerY,readerZ),new Direction3D(readerAlpha,readerDelta)),readDate,readingSession);
	}
	
	public int getLogId(){
		if(recorded){
			return logId;
		}else{
			throw new IllegalStateException();
		}
	}

	public String getTagId(){
		return tagId;
	}
	
	public double getRSSI(){
		return rssi;
	}

	public InfiniteVector3D getReaderLocation(){
		return readerLocation;
	}

	public Date getReadDate(){
		return readDate;
	}
	
	public String getReadingSession(){
		return readingSession;
	}

	public boolean isRecorded(){
		return recorded;
	}
}
