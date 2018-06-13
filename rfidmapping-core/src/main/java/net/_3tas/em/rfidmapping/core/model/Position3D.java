/**
 * 
 */
package net._3tas.em.rfidmapping.core.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author sat3
 *
 */
public class Position3D implements Serializable{
	private static final long serialVersionUID=1L;
	private final double x;
	private final double y;
	private final double z;
	
	public Position3D(double x,double y,double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}

	public double getX(){
		return x;
	}

	public double getY(){
		return y;
	}

	public double getZ(){
		return z;
	}
	
	public double getDistanceFrom(Position3D p){
		return Math.sqrt((x-p.x)*(x-p.x)+(y-p.y)*(y-p.y)+(z-p.z)*(z-p.z));
	}
	
	public static Position3D centroidOf(Collection<Position3D> positions){
		double cx=0;
		double cy=0;
		double cz=0;
		double div=positions.size();
		for(Position3D position:positions){
			cx+=position.getX()/div;
			cy+=position.getY()/div;
			cz+=position.getZ()/div;
		}
		return new Position3D(cx,cy,cz);
	}
}
