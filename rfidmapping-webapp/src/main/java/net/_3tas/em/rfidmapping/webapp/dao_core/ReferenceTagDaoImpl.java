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

import net._3tas.em.rfidmapping.core.dao.ReferenceTagDao;
import net._3tas.em.rfidmapping.core.model.ReferencePosition3D;

/**
 * @author sat3
 *
 */
@Repository
public class ReferenceTagDaoImpl implements ReferenceTagDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Map<String, ReferencePosition3D> findAll(){
		return jdbcTemplate.query("select tag_id,x,y,z,range_to_apply from reference_tags;",
				(ResultSet res)->{
					Map<String,ReferencePosition3D> map=new HashMap<String,ReferencePosition3D>();
					while(res.next()){
						map.put(res.getString("tag_id"),
								new ReferencePosition3D(
										res.getDouble("x"),
										res.getDouble("y"),
										res.getDouble("z"),
										res.getDouble("range_to_apply")));
					}
					return map;
				});
	}

	@Override
	public void update(String tagId, ReferencePosition3D referencePosition){
		boolean exist=jdbcTemplate.query("select x from reference_tags where tag_id=?;",
				new Object[]{tagId},new int[]{Types.VARCHAR},
				(ResultSet res)->res.next());
		Object[] param=new Object[]{
				referencePosition.getX(),
				referencePosition.getY(),
				referencePosition.getZ(),
				referencePosition.getRangeToApply(),
				tagId};
		int[] paramType=new int[]{Types.DOUBLE,Types.DOUBLE,Types.DOUBLE,Types.DOUBLE,Types.VARCHAR};
		if(exist){
			jdbcTemplate.update("update reference_tags set x=?,y=?,z=?,range_to_apply=? where tag_id=?;",param,paramType);
		}else{
			jdbcTemplate.update("insert into reference_tags (x,y,z,range_to_apply,tag_id) values (?,?,?,?,?);",param,paramType);
		}
	}

	@Override
	public void clear(){
		jdbcTemplate.update("truncate table reference_tags;");
	}
}
