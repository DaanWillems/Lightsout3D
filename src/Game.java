
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Game {
	
    private int displxInc = 0;

    private int displyInc = 0;

    private int displzInc = 0;

    private int scaleInc = 0;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private final Vector3f cameraInc;
    private static final float CAMERA_POS_STEP = 0.05f;
	private DisplayManager displayManager;
	private Renderer renderer;
	private Mesh mesh;
	private ArrayList<GameItem> gameItems;

	private Camera camera;

	private MouseInput mouseInput;
	
	public Game() {
		mouseInput = new MouseInput();
		 cameraInc = new Vector3f(0, 0, 0);
	}
	
	public void start() {
		camera = new Camera();
		displayManager = new DisplayManager();
	    // Initialise the Game
	    displayManager.init(600, 480);
		renderer = new Renderer(displayManager);
		float now, last, delta;
		mouseInput.init(displayManager);
	    last = 0;
	    
	    Mesh mesh = null;
	    Texture texture = null;
		try {
			mesh = OBJLoader.loadMesh("models/cube.obj");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("waap");

			e.printStackTrace();
		}
		
		try {
	        texture = new Texture("/resources/light.png");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("woop");
			e.printStackTrace();
		}
        gameItems = new ArrayList<GameItem>();
		
        GameItem[][][] items = new GameItem[3][3][3];
        
        mesh.setTexture(texture);
        for(int i = 0; i < 3; i ++) {
            for(int x = 0; x < 3; x ++) {
                for(int y = 0; y < 3; y ++) {
                    GameItem gameItem = new GameItem(mesh);
                    gameItem.setScale(0.4f);
                    gameItem.setPosition(x, y, i-2);
                    gameItems.add(gameItem);
                    items[i][x][y] = gameItem;
                }	
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
	        input();
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
	
	public void input() {
		cameraInc.set(0, 0, 0);
		if (displayManager.isKeyPressed(GLFW_KEY_W)) {
	        cameraInc.z = -1;
	    } else if (displayManager.isKeyPressed(GLFW_KEY_S)) {
	        cameraInc.z = 1;
	    }
	    if (displayManager.isKeyPressed(GLFW_KEY_A)) {
	        cameraInc.x = -1;
	    } else if (displayManager.isKeyPressed(GLFW_KEY_D)) {
	        cameraInc.x = 1;
	    }
	    if (displayManager.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
	        cameraInc.y = -1;
	    } else if (displayManager.isKeyPressed(GLFW_KEY_SPACE)) {
	        cameraInc.y = 1;
	    }
	}
	
	public void update(float delta) {
		mouseInput.input(displayManager);
		 camera.movePosition(cameraInc.x * CAMERA_POS_STEP,
			        cameraInc.y * CAMERA_POS_STEP,
			        cameraInc.z * CAMERA_POS_STEP);
		 
		 Vector2f rotVec = mouseInput.getDisplVec();
        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);

	    // Update camera based on mouse            
	    if (mouseInput.isRightButtonPressed()) {
	    	glfwSetInputMode(displayManager.windowID, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
       	}
	    if (mouseInput.isLeftButtonPressed()) {
	    	glfwSetInputMode(displayManager.windowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	    }
    }
	
	public void render(float delta) {
	    glClearColor(0, 0, 0, 0);
		renderer.render(camera, gameItems);
	}
}
