
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;
import org.joml.Intersectionf;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;

import org.joml.Matrix4f;

public class Renderer {
    private final Vector3f max;

    private final Vector3f min;

    private final Vector2f nearFar;

    private Vector3f dir;
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

	private Matrix4f viewMatrix;
	
	public Renderer(DisplayManager display) {
		
	    dir = new Vector3f();
        min = new Vector3f();
        max = new Vector3f();
        nearFar = new Vector2f();
		
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
			shaderProgram.createUniform("selected");
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
		viewMatrix = transformation.getViewMatrix(camera);
		
		shaderProgram.setUniform("texture_sampler", 0);
		selectGameItem(gameItems, camera);
		
		 for(GameItem gameItem : gameItems) {
			 Mesh mesh = gameItem.getMesh();
			// Set world matrix for this item
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
			shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
			// Render the mesh for this game item
			if(gameItem.isSelected()) {
			    shaderProgram.setUniform("selected", 1);
			    System.out.println("Working");
			} else {
			    shaderProgram.setUniform("selected", 0);
			}
		    shaderProgram.setUniform("colour", mesh.getColour());
		    shaderProgram.setUniform("useColour", mesh.isTextured() ? 0 : 1);
			mesh.render();
		}
	    shaderProgram.unbind();
	}
	
    public void selectGameItem(ArrayList<GameItem> gameItems, Camera camera) {        
        dir = viewMatrix.positiveZ(dir).negate();
        selectGameItem(gameItems, camera.getPosition(), dir);
}
    
    public boolean selectGameItem(ArrayList<GameItem> gameItems, Vector3f center, Vector3f dir) {
    	
    	boolean selected = false;
        GameItem selectedGameItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        for (GameItem gameItem : gameItems) {
            gameItem.setSelected(false);
            min.set(gameItem.getPosition());
            max.set(gameItem.getPosition());
            min.add(-gameItem.getScale(), -gameItem.getScale(), -gameItem.getScale());
            max.add(gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
            if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;
                selectedGameItem = gameItem;
            }
        }

        if (selectedGameItem != null) {
            selectedGameItem.setSelected(true);
            selected = true;
        }
        return selected;
}
}
