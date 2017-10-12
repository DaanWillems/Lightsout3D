
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
			shaderProgram.createUniform("modelViewMatrix");
			shaderProgram.createUniform("texture_sampler");
			shaderProgram.createUniform("colour");
			shaderProgram.createUniform("useColour");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
	
	public void render(Camera camera, ArrayList<GameItem> gameItems) {
		clear();
		shaderProgram.bind();
		
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, display.getWidth(), display.getHeight(), Z_NEAR, Z_FAR);
		shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		
		// Update view Matrix
		Matrix4f viewMatrix = transformation.getViewMatrix(camera);
		
		shaderProgram.setUniform("texture_sampler", 0);
		
		 for(GameItem gameItem : gameItems) {
			 Mesh mesh = gameItem.getMesh();
			// Set world matrix for this item
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
			shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
			// Render the mesh for this game item
		    shaderProgram.setUniform("colour", mesh.getColour());
		    shaderProgram.setUniform("useColour", mesh.isTextured() ? 0 : 1);
			mesh.render();
		}
	    shaderProgram.unbind();
	}
}
