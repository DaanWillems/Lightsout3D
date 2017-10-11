
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import org.joml.Matrix4f;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;

import org.joml.Matrix4f;

public class Renderer {
	
	/**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private Matrix4f projectionMatrix;
	
	private ShaderProgram shaderProgram;

	private Transformation transformation;

	private DisplayManager display;
	
	public Renderer(DisplayManager display) {
		this.display = display;
		transformation = new Transformation();
		
        shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("shader.vs");
        shaderProgram.attachFragmentShader("shader.fs");
        shaderProgram.link();
        
        // Create projection matrix
        float aspectRatio = (float) display.getWidth() / display.getHeight();
        try {
        	System.out.println("at least this works");
			shaderProgram.createUniform("projectionMatrix");
			shaderProgram.createUniform("worldMatrix");
			shaderProgram.createUniform("texture_sampler");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
	
	public void render(ArrayList<GameItem> gameItems) {
		clear();
		shaderProgram.bind();
		
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, display.getWidth(), display.getHeight(), Z_NEAR, Z_FAR);
		shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		
		shaderProgram.setUniform("texture_sampler", 0);
		
		 for(GameItem gameItem : gameItems) {
		        // Set world matrix for this item
		        Matrix4f worldMatrix =
		            transformation.getWorldMatrix(
		                gameItem.getPosition(),
		                gameItem.getRotation(),
		                gameItem.getScale());
		        shaderProgram.setUniform("worldMatrix", worldMatrix);
		        // Render the mes for this game item
		        gameItem.getMesh().render();
	    }
		
	    shaderProgram.unbind();
	}
}
