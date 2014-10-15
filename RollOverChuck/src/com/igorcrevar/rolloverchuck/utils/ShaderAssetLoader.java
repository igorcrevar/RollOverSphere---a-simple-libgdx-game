package com.igorcrevar.rolloverchuck.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

public class ShaderAssetLoader extends AsynchronousAssetLoader<ShaderProgram, ShaderAssetLoader.ShaderAssetLoaderParameter> {
	static public class ShaderAssetLoaderParameter extends AssetLoaderParameters<ShaderProgram> {
		public static String DefaultVertexShaderExtension = ".vsh";
		public static String DefaultFragmentShaderExtension = ".fsh";
		public static String BasePath = "shaders/";
		public String vertexShaderExtension = DefaultVertexShaderExtension;
		public String fragmentShaderExtension = DefaultFragmentShaderExtension;
		public String basePath = BasePath;
	}
	
    public ShaderAssetLoader (final FileHandleResolver resolver) {
    	super( resolver );     
    }

  	@Override
	public void loadAsync(AssetManager manager, String fileName,
			FileHandle file, ShaderAssetLoaderParameter parameter) {
	}

	@Override
	public ShaderProgram loadSync(AssetManager manager, String fileName,
			FileHandle file, ShaderAssetLoaderParameter parameter) {
		String vse = parameter != null ? parameter.vertexShaderExtension : ShaderAssetLoaderParameter.DefaultVertexShaderExtension;
		String fse = parameter != null ? parameter.fragmentShaderExtension : ShaderAssetLoaderParameter.DefaultFragmentShaderExtension;
		String basePath = parameter != null ? parameter.basePath : ShaderAssetLoaderParameter.BasePath;
		
		final String vName = String.format("%s%s%s", basePath, fileName, vse);
		final String fName = String.format("%s%s%s", basePath, fileName, fse);
		final ShaderProgram shader = new ShaderProgram(resolve(vName), resolve(fName));
		if (!shader.isCompiled()) {
		    throw new IllegalStateException(shader.getLog());
		}
		 
		return shader;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName,
			FileHandle file, ShaderAssetLoaderParameter parameter) {
		return null;
	}
}