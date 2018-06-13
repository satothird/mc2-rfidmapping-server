/**
 * 
 */
package net._3tas.em.rfidmapping.webapp.dao_ext;

import java.sql.ResultSet;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net._3tas.em.rfidmapping.core.model.InfiniteVector3D;

/**
 * @author sat3
 *
 */
@Repository
public class ReaderLocationDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public InfiniteVector3D find(String readerId){
		return jdbcTemplate.query("select x,y,z,alpha,delta from reader_location where reader_id=?;",
				new Object[]{readerId},new int[]{Types.VARCHAR},
				(ResultSet res)->{
					if(res.next()){
						return new InfiniteVector3D(res.getDouble("x"),res.getDouble("y"),res.getDouble("z"),res.getDouble("alpha"),res.getDouble("delta"));
					}else{
						return null;
					}
				});
	}
	
	public void update(String readerId,InfiniteVector3D location){
		String sql;
		if(find(readerId)!=null){
			sql="update reader_location set x=?,y=?,z=?,alpha=?,delta=? where reader_id=?;";
		}else{
			sql="insert into reader_location (x,y,z,alpha,delta,reader_id) values (?,?,?,?,?,?);";
		}
		jdbcTemplate.update(sql,
				new Object[]{location.getStartingPosition().getX(),location.getStartingPosition().getY(),location.getStartingPosition().getZ(),location.getDirection().getAlpha(),location.getDirection().getDelta(),readerId},
				new int[]{Types.DOUBLE,Types.DOUBLE,Types.DOUBLE,Types.DOUBLE,Types.DOUBLE,Types.VARCHAR});
	}
	
	public void clear(){
		jdbcTemplate.update("truncate table reader_location;");
	}
}
