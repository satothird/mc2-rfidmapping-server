/**
 * 
 */
package net._3tas.em.rfidmapping.webapp.dao_core;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net._3tas.em.rfidmapping.core.dao.CurrentTagLocationDao;
import net._3tas.em.rfidmapping.core.exception.NoSuchTagException;
import net._3tas.em.rfidmapping.core.model.Position3D;

/**
 * @author sat3
 *
 */
@Repository
public class CurrentTagLocationDaoImpl implements CurrentTagLocationDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Position3D find(String tagId) throws NoSuchTagException{
		Position3D[] sqlres=jdbcTemplate.query("select x,y,z from current_tag_location where tag_id=?;",
				new Object[]{tagId},new int[]{Types.VARCHAR},
				(ResultSet res)->{
					if(res.next()){
						Position3D location=new Position3D(res.getDouble("x"),res.getDouble("y"),res.getDouble("z"));
						if(res.wasNull()){
							location=null;
						}
						return new Position3D[]{location};
					}else{
						return null;
					}
				});
		if(sqlres!=null){
			return sqlres[0];
		}else{
			throw new NoSuchTagException();
		}
	}

	@Override
	public Map<String,Position3D> findAll(){
		return jdbcTemplate.query("select tag_id,x,y,z from current_tag_location;",
				(ResultSet res)->{
					Map<String,Position3D> map=new HashMap<String,Position3D>();
					while(res.next()){
						Position3D location=new Position3D(res.getDouble("x"),res.getDouble("y"),res.getDouble("z"));
						if(res.wasNull()){
							location=null;
						}
						map.put(res.getString("tag_id"),location);
					}
					return map;
				});
	}

	@Override
	public void update(String tagId, Position3D currentLocation){
		Object[] param;
		int[] paramType;
		if(currentLocation!=null){
			param=new Object[]{currentLocation.getX(),currentLocation.getY(),currentLocation.getZ(),tagId};
			paramType=new int[]{Types.DOUBLE,Types.DOUBLE,Types.DOUBLE,Types.VARCHAR};
		}else{
			param=new Object[]{null,null,null,tagId};
			paramType=new int[]{Types.NULL,Types.NULL,Types.NULL,Types.VARCHAR};
		}
		try{
			find(tagId);
			jdbcTemplate.update("update current_tag_location set x=?,y=?,z=? where tag_id=?;",param,paramType);
		}catch(NoSuchTagException e){
			jdbcTemplate.update("insert into current_tag_location (x,y,z,tag_id) values (?,?,?,?);",param,paramType);
		}
	}
	
	@Override
	public void clear(){
		jdbcTemplate.update("truncate table current_tag_location;");
	}
}
