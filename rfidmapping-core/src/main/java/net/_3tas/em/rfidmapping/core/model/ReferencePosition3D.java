/**
 * 
 */
package net._3tas.em.rfidmapping.core.model;

/**
 * @author sat3
 *
 */
public class ReferencePosition3D extends Position3D{
	private static final long serialVersionUID=1L;
	private final double rangeToApply;
	
	public ReferencePosition3D(double x,double y,double z,double rangeToApply){
		super(x,y,z);
		this.rangeToApply=rangeToApply;
	}

	public double getRangeToApply(){
		return rangeToApply;
	}
	
	public boolean isCovering(Position3D p){
		return super.getDistanceFrom(p)<=rangeToApply;
	}
}
