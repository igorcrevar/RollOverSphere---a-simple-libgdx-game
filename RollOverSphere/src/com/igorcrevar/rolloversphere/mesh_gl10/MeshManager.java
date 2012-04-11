package com.igorcrevar.rolloversphere.mesh_gl10;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Mesh;

public class MeshManager {
	private Map<String, AbstractMesh> mMeshes = new HashMap<String, AbstractMesh>();
	private static MeshManager instance;
	static
	{
		instance = new MeshManager();
	}
	
	public static MeshManager getInstance(){
		return instance;
	}
	
	public void put(String key, AbstractMesh mesh){
		mMeshes.put(key, mesh);
	}
	
	/**
	 * Get Mesh object for given key
	 * @param key
	 * @return
	 */
	public AbstractMesh getMesh(String key){
		return mMeshes.get(key);
	}
	
	/**
	 * Get Libgdx Mesh from Mesh with given key
	 * @param key
	 * @return
	 */
	public Mesh getMeshLibGdx(String key){
		return mMeshes.get(key).getMesh();
	}
	
	public void dispose(){
		for (String key: mMeshes.keySet()){			
			mMeshes.get(key).dispose();
		}
	}
}
