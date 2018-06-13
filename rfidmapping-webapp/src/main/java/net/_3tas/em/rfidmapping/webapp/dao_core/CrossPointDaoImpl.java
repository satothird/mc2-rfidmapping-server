package net._3tas.em.rfidmapping.webapp.dao_core;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net._3tas.em.rfidmapping.core.dao.CrossPointDao;
import net._3tas.em.rfidmapping.core.model.Position3D;
import net._3tas.em.rfidmapping.core.model.WeightedPosition3D;

@Repository
public class CrossPointDaoImpl implements CrossPointDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<WeightedPosition3D> find(String tagId){
		return jdbcTemplate.query("select x,y,z,weight from cross_points where tag_id=?;",
				new Object[]{tagId},new int[]{Types.VARCHAR},
				(ResultSet res,int count)->new WeightedPosition3D(res.getDouble("x"),res.getDouble("y"),res.getDouble("z"),res.getDouble("weight")));
	}

	@Override
	public Position3D getWeightedCentroid(String tagId){
		return jdbcTemplate.query("select sum(x*weight)/sum(weight) as x,sum(y*weight)/sum(weight) as y,sum(z*weight)/sum(weight) as z from cross_points where tag_id=?;",
				new Object[]{tagId},new int[]{Types.VARCHAR},
				(ResultSet res)->{
					if(res.next()){
						return new Position3D(res.getDouble("x"),res.getDouble("y"),res.getDouble("z"));
					}else{
						return null;
					}
				});
	}

	@Override
	public void append(String tagId, WeightedPosition3D point){
		jdbcTemplate.update("insert into cross_points (tag_id,x,y,z,weight) values (?,?,?,?,?);",
				new Object[]{tagId,point.getX(),point.getY(),point.getZ(),point.getWeight()},
				new int[]{Types.VARCHAR,Types.DOUBLE,Types.DOUBLE,Types.DOUBLE,Types.DOUBLE});
	}

	@Override
	public void delete(String tagId){
		jdbcTemplate.update("delete from cross_points where tag_id=?;",
				new Object[]{tagId},new int[]{Types.VARCHAR});
	}

	@Override
	public void clear(){
		jdbcTemplate.update("truncate table cross_points;");
	}
}
