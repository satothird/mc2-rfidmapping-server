/**
 * 
 */
package net._3tas.em.rfidmapping.core.model;

import java.io.Serializable;

/**
 * @author sat3
 *
 */
public class InfiniteVector3D implements Serializable{
	private static final long serialVersionUID=1L;
	private final Position3D startingPosition;
	private final Direction3D direction;
	
	public InfiniteVector3D(Position3D startingPosition,Direction3D direction){
		if(startingPosition==null || direction==null){
			throw new IllegalArgumentException();
		}
		this.startingPosition=startingPosition;
		this.direction=direction;
	}
	
	public InfiniteVector3D(double x,double y,double z,double alpha,double delta){
		startingPosition=new Position3D(x,y,z);
		direction=new Direction3D(alpha,delta);
	}

	public Position3D getStartingPosition(){
		return startingPosition;
	}

	public Direction3D getDirection(){
		return direction;
	}
	
	public Position3D getPositionAtDistance(double distance){
		double[] unitVector=direction.getUnitVector();
		double x=startingPosition.getX()+distance*unitVector[0];
		double y=startingPosition.getY()+distance*unitVector[1];
		double z=startingPosition.getZ()+distance*unitVector[2];
		return new Position3D(x,y,z);
	}
	
	public Position3D getCrossPointWith(InfiniteVector3D vec,double crossingThreshold,double distanceLimit){
		if(vec==null || crossingThreshold<0 || distanceLimit<=0){
			throw new IllegalArgumentException();
		}
		double[] unitVectorT=this.direction.getUnitVector();
		double[] unitVectorO=vec.direction.getUnitVector();
		double unitProd=calcInnerProductOf3DVector(unitVectorT,unitVectorO);
		double div=1-unitProd*unitProd;
		if(div==0){
			return null;
		}else{
 			double[] vectorTO=new double[]{vec.getStartingPosition().getX()-this.getStartingPosition().getX(),vec.getStartingPosition().getY()-this.getStartingPosition().getY(),vec.getStartingPosition().getZ()-this.getStartingPosition().getZ()};
			double dT=(calcInnerProductOf3DVector(vectorTO,unitVectorT)-unitProd*calcInnerProductOf3DVector(vectorTO,unitVectorO))/div;
			double dO=(unitProd*calcInnerProductOf3DVector(vectorTO,unitVectorT)-calcInnerProductOf3DVector(vectorTO,unitVectorO))/div;
			if(dT<=distanceLimit && dT>0 && dO<=distanceLimit && dO>0){
				Position3D pT=getPositionAtDistance(dT);
				Position3D pO=vec.getPositionAtDistance(dO);
				if(pT.getDistanceFrom(pO)<=crossingThreshold){
					return new Position3D((pT.getX()+pO.getX())/2,(pT.getY()+pO.getY())/2,(pT.getZ()+pO.getZ())/2);
				}else{
					return null;
				}
			}else{
				return null;
			}
		}
	}
	
	private double calcInnerProductOf3DVector(double[] a,double[] b){
		return a[0]*b[0]+a[1]*b[1]+a[2]*b[2];
	}
}
