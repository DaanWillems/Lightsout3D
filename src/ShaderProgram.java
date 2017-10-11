
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;


public class ShaderProgram {
	
    // ProgramID
    int programID;

    // Vertex Shader ID
    int vertexShaderID;
    // Fragment Shader ID
    int fragmentShaderID;
    private final Map<String, Integer> uniforms;
    /**
     * Create a new ShaderProgram.
     */
    public ShaderProgram()
    {
    	uniforms = new HashMap<>();
        programID = glCreateProgram();
    }
    
	public void attachVertexShader(String name)
	{
	    // Load the source
	    String vertexShaderSource = FileUtil.readFromFile(name);
	
	    // Create the shader and set the source
	    vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
	    glShaderSource(vertexShaderID, vertexShaderSource);
	
	    // Compile the shader
	    glCompileShader(vertexShaderID);
	
	    // Check for errors
	    if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE)
	        throw new RuntimeException("Error creating vertex shader\n"
	                                   + glGetShaderInfoLog(vertexShaderID, glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH)));
	
	    // Attach the shader
	    glAttachShader(programID, vertexShaderID);
	}
	
	public void attachFragmentShader(String name)
    {
        // Read the source
        String fragmentShaderSource = FileUtil.readFromFile(name);

        // Create the shader and set the source
        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentShaderSource);

        // Compile the shader
        glCompileShader(fragmentShaderID);

        // Check for errors
        if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Error creating fragment shader\n"
                                       + glGetShaderInfoLog(fragmentShaderID, glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH)));

        // Attach the shader
        glAttachShader(programID, fragmentShaderID);
    }

	public void createUniform(String uniformName) throws Exception {
	    int uniformLocation = glGetUniformLocation(programID,
	        uniformName);
	    if (uniformLocation < 0) {
	        throw new Exception("Could not find uniform:" +
	            uniformName);
	    }
	    uniforms.put(uniformName, uniformLocation);
	}
	
	public void setUniform(String uniformName, Matrix4f value) {
	    // Dump the matrix into a float buffer
	    try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(16);
	        value.get(fb);
	        glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
	    }
	}
	
	public void setUniform(String uniformName, int value) {
	    glUniform1i(uniforms.get(uniformName), value);
	}
	
    public void link()
    {
        // Link this program
        glLinkProgram(programID);

        // Check for linking errors
        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException("Unable to link shader program:");
    }
    
    public void bind()
    {
        glUseProgram(programID);
    }
    
    public static void unbind()
    {
        glUseProgram(0);
    }
    /**
     * Dispose the program and shaders.
     */
    public void dispose()
    {
        // Unbind the program
        unbind();

        // Detach the shaders
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);

        // Delete the shaders
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);

        // Delete the program
        glDeleteProgram(programID);
    }

    /**
     * @return The ID of this program.
     */
    public int getID()
    {
        return programID;
    }

}
