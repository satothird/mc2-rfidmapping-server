/**
 * 
 */
package net._3tas.em.rfidmapping.core.model;

import java.util.Collection;

/**
 * @author sat3
 *
 */
public class WeightedPosition3D extends Position3D{
	private static final long serialVersionUID=1L;
	private final double weight;

	public WeightedPosition3D(double x,double y,double z,double weight){
		super(x,y,z);
		this.weight=weight;
	}
	
	public WeightedPosition3D(double x,double y,double z){
		super(x,y,z);
		this.weight=1;
	}
	
	public WeightedPosition3D(Position3D position,double weight){
		this(position.getX(),position.getY(),position.getZ(),weight);
	}
	
	public WeightedPosition3D(Position3D position){
		this(position.getX(),position.getY(),position.getZ());
	}

	public double getWeight(){
		return weight;
	}
	
	public static Position3D weightedCentroidOf(Collection<WeightedPosition3D> positions){
		double cx=0;
		double cy=0;
		double cz=0;
		double div=0;
		for(WeightedPosition3D position:positions){
			div+=position.getWeight();
		}
		for(WeightedPosition3D position:positions){
			cx+=position.getX()*position.getWeight()/div;
			cy+=position.getY()*position.getWeight()/div;
			cz+=position.getZ()*position.getWeight()/div;
		}
		return new Position3D(cx,cy,cz);
	}
}
