/**
 * 
 */
package net._3tas.em.rfidmapping.webapp.dao_core;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net._3tas.em.rfidmapping.core.dao.TagReadLogDao;
import net._3tas.em.rfidmapping.core.exception.NoSuchTagException;
import net._3tas.em.rfidmapping.core.model.TagReadLog;

/**
 * @author sat3
 *
 */
@Repository
public class TagReadLogDaoImpl implements TagReadLogDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<TagReadLog> find(String tagId) throws NoSuchTagException{
		List<TagReadLog> list=jdbcTemplate.query("select log_id,rssi,reader_x,reader_y,reader_z,reader_alpha,reader_delta,read_date,reading_session from tag_read_logs where tag_id=? order by read_date ASC;",
				new Object[]{tagId},new int[]{Types.VARCHAR},
				(ResultSet res,int count)->new TagReadLog(
						res.getInt("log_id"),
						tagId,
						res.getDouble("rssi"),
						res.getDouble("reader_x"),
						res.getDouble("reader_y"),
						res.getDouble("reader_z"),
						res.getDouble("reader_alpha"),
						res.getDouble("reader_delta"),
						res.getTimestamp("read_date"),
						res.getString("reading_session")));
		if(list.size()!=0){
			return list;
		}else{
			throw new NoSuchTagException();
		}
	}

	@Override
	public List<TagReadLog> find(String tagId,Date startDate) throws NoSuchTagException{
		List<TagReadLog> list=jdbcTemplate.query("select log_id,rssi,reader_x,reader_y,reader_z,reader_alpha,reader_delta,read_date,reading_session from tag_read_logs where tag_id=? and read_date>=? order by read_date ASC;",
				new Object[]{tagId,startDate},new int[]{Types.VARCHAR,Types.TIMESTAMP},
				(ResultSet res,int count)->new TagReadLog(
						res.getInt("log_id"),
						tagId,
						res.getDouble("rssi"),
						res.getDouble("reader_x"),
						res.getDouble("reader_y"),
						res.getDouble("reader_z"),
						res.getDouble("reader_alpha"),
						res.getDouble("reader_delta"),
						res.getTimestamp("read_date"),
						res.getString("reading_session")));
		if(list.size()!=0){
			return list;
		}else{
			if(find(tagId).size()!=0){
				return list;
			}else{
				throw new NoSuchTagException();
			}
		}
	}

	@Override
	public List<TagReadLog> findByReadingSession(String readingSession){
		return jdbcTemplate.query("select log_id,tag_id,rssi,reader_x,reader_y,reader_z,reader_alpha,reader_delta,read_date from tag_read_logs where reading_session=? order by read_date ASC;",
				new Object[]{readingSession},new int[]{Types.VARCHAR},
				(ResultSet res,int count)->new TagReadLog(
						res.getInt("log_id"),
						res.getString("tag_id"),
						res.getDouble("rssi"),
						res.getDouble("reader_x"),
						res.getDouble("reader_y"),
						res.getDouble("reader_z"),
						res.getDouble("reader_alpha"),
						res.getDouble("reader_delta"),
						res.getTimestamp("read_date"),
						readingSession));
	}

	@Override
	public List<TagReadLog> findAll(){
		return jdbcTemplate.query("select log_id,tag_id,rssi,reader_x,reader_y,reader_z,reader_alpha,reader_delta,read_date,reading_session from tag_read_logs order by read_date ASC;",
				(ResultSet res,int count)->new TagReadLog(
						res.getInt("log_id"),
						res.getString("tag_id"),
						res.getDouble("rssi"),
						res.getDouble("reader_x"),
						res.getDouble("reader_y"),
						res.getDouble("reader_z"),
						res.getDouble("reader_alpha"),
						res.getDouble("reader_delta"),
						res.getTimestamp("read_date"),
						res.getString("reading_session")));
	}

	@Override
	public List<String> listLoggedTagIDs(){
		return jdbcTemplate.query("select distinct tag_id from tag_read_logs;",
				(ResultSet res,int count)->res.getString("tag_id"));
	}

	@Override
	public void append(TagReadLog log){
		if(log.isRecorded()){
			try{
				jdbcTemplate.update("insert into tag_read_logs (log_id,tag_id,rssi,reader_x,reader_y,reader_z,reader_alpha,reader_delta,read_date,reading_session) values (?,?,?,?,?,?,?,?,?,?);",
						new Object[]{
								log.getLogId(),
								log.getTagId(),
								log.getRSSI(),
								log.getReaderLocation().getStartingPosition().getX(),
								log.getReaderLocation().getStartingPosition().getY(),
								log.getReaderLocation().getStartingPosition().getZ(),
								log.getReaderLocation().getDirection().getAlpha(),
								log.getReaderLocation().getDirection().getDelta(),
								log.getReadDate(),
								log.getReadingSession()},
						new int[]{
								Types.INTEGER,
								Types.VARCHAR,
								Types.DOUBLE,
								Types.DOUBLE,
								Types.DOUBLE,
								Types.DOUBLE,
								Types.DOUBLE,
								Types.DOUBLE,
								Types.TIMESTAMP,
								Types.VARCHAR});
			}catch(DataAccessException e){}
		}else{
			jdbcTemplate.update("insert into tag_read_logs (tag_id,rssi,reader_x,reader_y,reader_z,reader_alpha,reader_delta,read_date,reading_session) values (?,?,?,?,?,?,?,?,?);",
					new Object[]{
							log.getTagId(),
							log.getRSSI(),
							log.getReaderLocation().getStartingPosition().getX(),
							log.getReaderLocation().getStartingPosition().getY(),
							log.getReaderLocation().getStartingPosition().getZ(),
							log.getReaderLocation().getDirection().getAlpha(),
							log.getReaderLocation().getDirection().getDelta(),
							log.getReadDate(),
							log.getReadingSession()},
					new int[]{
							Types.VARCHAR,
							Types.DOUBLE,
							Types.DOUBLE,
							Types.DOUBLE,
							Types.DOUBLE,
							Types.DOUBLE,
							Types.DOUBLE,
							Types.TIMESTAMP,
							Types.VARCHAR});
		}
	}
	
	@Override
	public void clear(){
		jdbcTemplate.update("truncate table tag_read_logs;");
	}
}
