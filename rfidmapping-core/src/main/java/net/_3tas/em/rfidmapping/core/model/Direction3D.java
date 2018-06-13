/**
 * 
 */
package net._3tas.em.rfidmapping.core.model;

import java.io.Serializable;

/**
 * @author sat3
 *
 */
public class Direction3D implements Serializable{
	private static final long serialVersionUID=1L;
	private final double alpha;
	private final double delta;
	
	public Direction3D(double alpha,double delta){
		this.alpha=alpha;
		this.delta=delta;
	}

	public double getAlpha(){
		return alpha;
	}

	public double getDelta(){
		return delta;
	}
	
	public double[] getUnitVector(){
		return new double[]{
				Math.cos(delta)*Math.cos(alpha),
				Math.cos(delta)*Math.sin(alpha),
				Math.sin(delta)
				};
	}
}
