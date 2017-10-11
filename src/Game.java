
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game {
	
	private DisplayManager displayManager;
	private Renderer renderer;
	private Mesh mesh;
	
	public void start() {
		
		displayManager = new DisplayManager();
	    // Initialise the Game
	    displayManager.init(600, 480);
		renderer = new Renderer(displayManager);
		float now, last, delta;

	    last = 0;
	    
        float[] positions = new float[]{
            -0.5f,  0.5f, -1.05f,
            -0.5f, -0.5f, -1.05f,
             0.5f, -0.5f, -1.05f,
             0.5f,  0.5f, -1.05f,
        };
        
        float[] colours = new float[]{
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,
        };
        
        mesh = new Mesh(positions, colours, indices);
	 

	    // Loop continuously and render and update
	    while (!displayManager.shouldClose())
	    {
	        // Get the time
	        now = (float) glfwGetTime();
	        delta = now - last;
	        last = now;

	        // Update and render
	        update(delta);
	        render(delta);

	        // Poll the events and swap the buffers
	        glfwPollEvents();
	        glfwSwapBuffers(displayManager.windowID);
	    }

	    // Destroy the window
	    glfwDestroyWindow(displayManager.windowID);
	    glfwTerminate();

	    System.exit(0);
	}
	
	public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void update(float delta) {
		
	}
	
	public void render(float delta) {
	    glClearColor(0, 0, 0, 0);
		renderer.render(mesh);
	}
}
