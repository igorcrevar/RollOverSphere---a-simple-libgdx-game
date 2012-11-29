package com.igorcrevar.rolloversphere.objects.boxes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.igorcrevar.rolloversphere.collsion.CollisionSolver;
import com.igorcrevar.rolloversphere.collsion.ICollisionIterationHandler;
import com.igorcrevar.rolloversphere.objects.boxes.factory.IBoxesFactory;

public class BoxesManager implements ICollisionIterationHandler {
	private class Coordinate
	{
		public Coordinate(int x, int y) {
			set(x, y);
		}
		
		public void set(int x, int y) {
			this.x = x;
			this.y = y;
		}
		public int x, y;
	}
	
	private List<Box> mBoxes = new ArrayList<Box>();
	//list of all boxes intersected with passed sphere
	private List<Box> mIntersectedBoxes = new ArrayList<Box>();
	//list of all boxes expired(timeouted) after update
	private List<Box> mExpiredBoxes = new ArrayList<Box>();
	
	private float mRadiusOfBall;
	private IBoxesFactory mBoxesFactory;	
	private float mHeight;
	private float mWidth;
	private float mMinX;
	private float mMinZ;
	
	private float nodeSize;
	private int gridWidth;
	private boolean grid[][];
	
	public BoxesManager(IBoxesFactory bf){
		setBoxesFactory(bf);
	}
	
	public void init(float minX, float maxX, float minZ, float maxZ, float nodeSize){
		this.mMinX = minX;
		this.mMinZ = minZ;
		this.mHeight = Math.abs(maxZ - minZ);
		this.mWidth = Math.abs(maxX - minX);
		
		this.nodeSize = nodeSize;
		gridWidth = getDivide(mWidth, nodeSize);
		grid = new boolean[getDivide(mHeight, nodeSize)][];
		for (int i = 0; i < grid.length; ++i) {
			grid[i] = new boolean [gridWidth];
			for (int j = 0; j < gridWidth; ++j) {
				grid[i][j] = false;
			}
		}
	}
	
	private int getDivide(float length, float size) {
		double number = (length + size - 1) / size;
		return (int)number;
	}
	
	private Coordinate tmpCoordinate = new Coordinate(0, 0); //GC optimizier!
	private Coordinate getBoxCoordinate(Box box) {
		tmpCoordinate.x = getDivide(box.position.x - this.mMinX, gridWidth);
		tmpCoordinate.y = getDivide(box.position.z - this.mMinZ, grid.length);
		return tmpCoordinate;
	}
	
	public void setBoxesFactory(IBoxesFactory bf){
		mBoxesFactory = bf;
	}
	
	private List<Coordinate> getFreeGrids() {
		ArrayList<Coordinate> list = new ArrayList<Coordinate>();
		for (int i = 0; i < grid.length; ++i) {
			for (int j = 0; j < grid[i].length; ++j) {
				if (!grid[i][j]) {
					list.add(new Coordinate(j, i));
				}
			}
		}
		return list;
	}
	
	private Random mRandom = new Random();
	public void addNew(Vector3 pos, float r){
		Box pb = mBoxesFactory.get();		
		pb.init();
		pb.position.y = 0.5f;
		
		List<Coordinate> freeCoords = getFreeGrids();
		if (freeCoords.size() > 0) {			
			//retrieve one of free nodes
			int posInsideGrid = mRandom.nextInt(freeCoords.size());
			Coordinate coord = freeCoords.get(posInsideGrid);
			//this node is not free any more 
			grid[coord.y][coord.x] = true;			
			//fix position and add to list of visible boxes
			pb.position.x = coord.x * nodeSize + (int)(Math.random() * nodeSize) + this.mMinX;
			pb.position.z = coord.y * nodeSize + (int)(Math.random() * nodeSize) + this.mMinZ;
			mBoxes.add(pb);
		}
	}
	
	/**
	 * Update boxes, remove not alive and render
	 * @param camera
	 * @param timeDiff
	 */
	public void updateAndRender(PerspectiveCamera camera, float timeDiff){
		mExpiredBoxes.clear(); //clear list of expired
		int size = mBoxes.size();
		for (int i = size - 1; i >= 0; --i){
			Box box = mBoxes.get(i);	
			if (box.updateBox(timeDiff)){
				box.render(camera);
			}
			else{
				mBoxes.remove(i);		
				//add to expired list
				mExpiredBoxes.add(box); 
				//node where was box is now free
				Coordinate coord = getBoxCoordinate(box);
				grid[coord.y][coord.x] = false; 
			}
		}
	}
	
	private void doCollideCheck(Vector3 pos, float r){
		int size = mBoxes.size();
		for (int i = size - 1; i >= 0; --i){
			Box box = mBoxes.get(i);
			if (CollisionSolver.isCollide(pos, r, box.position, box.boundingSphereR)){
				mIntersectedBoxes.add(box);
				mBoxes.remove(box);
				//node where was box is now free
				Coordinate coord = getBoxCoordinate(box);
				grid[coord.y][coord.x] = false; 
			}
		}
	}
	
	/* Do not call this method!!!
	 * (non-Javadoc)
	 * @see com.igorcrevar.rolloversphere.collsion.ICollisionIterationHandler#iterationHandler(com.badlogic.gdx.math.Vector3, java.lang.Object)
	 */
	@Override
	public boolean iterationHandler(Vector3 position, Object tag) {
		doCollideCheck(position, mRadiusOfBall);		
		return true;
	}
	
	public void doCollideCheck(Vector3 posStart, Vector3 posEnd, float r){
		mIntersectedBoxes.clear();
		//TODO: mRadiusOfBall should be in tag
		mRadiusOfBall = r;		
		CollisionSolver.iterateOver(this, posStart, posEnd, null);		
	}
	
	public void remove(List<Box> pbs){
		for (Box pb : pbs){
			mBoxes.remove(pb);
			//node where was box is now free
			Coordinate coord = getBoxCoordinate(pb);
			grid[coord.y][coord.x] = false; 
		}
	}
	
	/**
	 * Check collision with sphere(center from posStart to posEnd and radius s) for every box
	 * remove collided boxes. update and render boxes 
	 * @return list of removed boxes
	 */
	public void doAll(PerspectiveCamera camera, float timeDiff, 
								   Vector3 posStart, Vector3 posEnd, float r){
		doCollideCheck(posStart, posEnd, r);
		updateAndRender(camera, timeDiff);
	}
	
	public int getBoxesCount(){
		return mBoxes.size();
	}
	
	public int getNotUpgradeBoxCount(){
		int size = 0;
		for (Box box:mBoxes){
			if (box.getUpgradeType() == UpgradeType.NOT_UPGRADE){
				++size;
			}
		}
		
		return size;
	}

	public List<Box> getExpired(){
		return mExpiredBoxes;
	}
	
	public List<Box> getIntersectedBoxes(){
		return mIntersectedBoxes;
	}
	
}
