/**
 * 
 */
package net._3tas.em.rfidmapping.core;

import java.util.List;

import net._3tas.em.rfidmapping.core.model.TagReadLog;

/**
 * @author sat3
 *
 */
public interface LocationUpdater{
	public void accept(TagReadLog tagReadLog);
	
	public default void accept(List<TagReadLog> tagReadLogs){
		for(TagReadLog tagReadLog:tagReadLogs){
			accept(tagReadLog);
		}
	}
}
