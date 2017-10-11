
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;

import org.joml.Vector3f;

public class Game {
	
    private int displxInc = 0;

    private int displyInc = 0;

    private int displzInc = 0;

    private int scaleInc = 0;
	
	private DisplayManager displayManager;
	private Renderer renderer;
	private Mesh mesh;
	private ArrayList<GameItem> gameItems;
	
	public void start() {
		
		displayManager = new DisplayManager();
	    // Initialise the Game
	    displayManager.init(600, 480);
		renderer = new Renderer(displayManager);
		float now, last, delta;

	    last = 0;
	    
	    float[] positions = new float[] {
	            // V0
	            -0.5f, 0.5f, 0.5f,
	            // V1
	            -0.5f, -0.5f, 0.5f,
	            // V2
	            0.5f, -0.5f, 0.5f,
	            // V3
	            0.5f, 0.5f, 0.5f,
	            // V4
	            -0.5f, 0.5f, -0.5f,
	            // V5
	            0.5f, 0.5f, -0.5f,
	            // V6
	            -0.5f, -0.5f, -0.5f,
	            // V7
	            0.5f, -0.5f, -0.5f,
	            
	            // For text coords in top face
	            // V8: V4 repeated
	            -0.5f, 0.5f, -0.5f,
	            // V9: V5 repeated
	            0.5f, 0.5f, -0.5f,
	            // V10: V0 repeated
	            -0.5f, 0.5f, 0.5f,
	            // V11: V3 repeated
	            0.5f, 0.5f, 0.5f,

	            // For text coords in right face
	            // V12: V3 repeated
	            0.5f, 0.5f, 0.5f,
	            // V13: V2 repeated
	            0.5f, -0.5f, 0.5f,

	            // For text coords in left face
	            // V14: V0 repeated
	            -0.5f, 0.5f, 0.5f,
	            // V15: V1 repeated
	            -0.5f, -0.5f, 0.5f,

	            // For text coords in bottom face
	            // V16: V6 repeated
	            -0.5f, -0.5f, -0.5f,
	            // V17: V7 repeated
	            0.5f, -0.5f, -0.5f,
	            // V18: V1 repeated
	            -0.5f, -0.5f, 0.5f,
	            // V19: V2 repeated
	            0.5f, -0.5f, 0.5f,
	        };
	        float[] textCoords = new float[]{
	            0.0f, 0.0f,
	            0.0f, 0.5f,
	            0.5f, 0.5f,
	            0.5f, 0.0f,
	            
	            0.0f, 0.0f,
	            0.5f, 0.0f,
	            0.0f, 0.5f,
	            0.5f, 0.5f,
	            
	            // For text coords in top face
	            0.0f, 0.5f,
	            0.5f, 0.5f,
	            0.0f, 1.0f,
	            0.5f, 1.0f,

	            // For text coords in right face
	            0.0f, 0.0f,
	            0.0f, 0.5f,

	            // For text coords in left face
	            0.5f, 0.0f,
	            0.5f, 0.5f,

	            // For text coords in bottom face
	            0.5f, 0.0f,
	            1.0f, 0.0f,
	            0.5f, 0.5f,
	            1.0f, 0.5f,
	        };
	        int[] indices = new int[]{
	            // Front face
	            0, 1, 3, 3, 1, 2,
	            // Top Face
	            8, 10, 11, 9, 8, 11,
	            // Right face
	            12, 13, 7, 5, 12, 7,
	            // Left face
	            14, 15, 6, 4, 14, 6,
	            // Bottom face
	            16, 18, 19, 17, 16, 19,
	            // Back face
	            4, 6, 7, 5, 4, 7,};
        Texture texture = null;
		try {
			texture = new Texture("resources/grassblock.png");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Kabloo");
			e.printStackTrace();
		}
        gameItems = new ArrayList<GameItem>();
    	for(int i = 0; i < 100; i++) {
        		for(int x = -10; x < 10; x++) {
		        Mesh mesh = new Mesh(positions, textCoords, indices, texture);
		        GameItem gameItem = new GameItem(mesh);
		        gameItem.setPosition(x, (float)-1.5, i-(2*i)-2);
		        gameItem.setScale((float)1);
		        gameItems.add(gameItem);
    		}	
    	}


      
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
		 for (GameItem gameItem : gameItems) {
	            // Update position
	            Vector3f itemPos = gameItem.getPosition();
	            float posx = itemPos.x + displxInc * 0.01f;
	            float posy = itemPos.y + displyInc * 0.01f;
	            float posz = itemPos.z + displzInc * 0.01f;
	            gameItem.setPosition(posx, posy, posz);

	            // Update scale
	            float scale = gameItem.getScale();
	            scale += scaleInc * 0.05f;
	            if (scale < 0) {
	                scale = 0;
	            }
	            gameItem.setScale(scale);

	            // Update rotation angle
	            float rotation = gameItem.getRotation().x + 1.5f;
	            if (rotation > 360) {
	                rotation = 0;
	            }
	            gameItem.setRotation(rotation, rotation, rotation);
	        }
    }
	
	public void render(float delta) {
	    glClearColor(0, 0, 0, 0);
		renderer.render(gameItems);
	}
}
