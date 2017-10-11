import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;


public class DisplayManager {
	
	public long windowID;
	public int width;
	public int height;
	
	public void init(int width, int height) {
		this.width = width;
		this.height = height;
		
		System.out.println("awdwdawad");
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
		{
		    System.err.println("Error initializing GLFW");
		    System.exit(1);
		}
		
		glfwWindowHint(GLFW_SAMPLES, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		
		windowID = glfwCreateWindow(640, 480, "My GLFW Window", NULL, NULL);

		if (windowID == NULL)
		{
		    System.err.println("Error creating a window");
		    System.exit(1);
		}
		
		glfwMakeContextCurrent(windowID);
		glfwSwapInterval(1);
		GL.createCapabilities();
		glfwShowWindow(windowID);
	}
	
	public void Destroy() {
		glfwDestroyWindow(windowID);
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(windowID);
	}

	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}
	
	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}
}
