
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
	
	public Renderer(DisplayManager display) {
        shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("shader.vs");
        shaderProgram.attachFragmentShader("shader.fs");
        shaderProgram.link();
        
        // Create projection matrix
        float aspectRatio = (float) display.getWidth() / display.getHeight();
        projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
        try {
        	System.out.println("at least this works");
			shaderProgram.createUniform("projectionMatrix");
			shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
	
	public void render(Mesh mesh) {
		clear();
		shaderProgram.bind();
		shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		
	    // Draw the mesh
	    glBindVertexArray(mesh.getVaoId());
	    glEnableVertexAttribArray(0);
	    glEnableVertexAttribArray(1);
	    glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

	    // Restore state
	    glDisableVertexAttribArray(0);
	    glBindVertexArray(0);

	    shaderProgram.unbind();
	}
}
