package com.igorcrevar.rolloversphere.objects;

import com.badlogic.gdx.graphics.Texture;
import com.igorcrevar.rolloversphere.mesh_gl10.AbstractMesh;
import com.igorcrevar.rolloversphere.mesh_gl10.MeshManager;
import com.igorcrevar.rolloversphere.mesh_gl10.WaterMesh;
import com.igorcrevar.rolloversphere.texture.TextureManager;

public class DynamicPlane extends GameObject{

	@Override
	protected AbstractMesh getMesh() {
		return MeshManager.getInstance().getMesh("water");
	}

	@Override
	protected Texture getTexture() {
		return TextureManager.getInstance().getTexture("flag");
	}

	@Override
	public void update(float timeDiff) {
		WaterMesh mesh = (WaterMesh)mMesh;
		mesh.updateMesh(timeDiff);
	}

}
