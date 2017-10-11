import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FileUtil {
	public static String readFromFile(String name)
	{
	    StringBuilder source = new StringBuilder();
	    try
	    {
	        BufferedReader reader = new BufferedReader(
	                                    new InputStreamReader(
	                                        ShaderProgram.class
	                                                     .getClassLoader()
	                                                     .getResourceAsStream(name)));

	        String line;
	        while ((line = reader.readLine()) != null)
	        {
	            source.append(line).append("\n");
	        }

	        reader.close();
	    }
	    catch (Exception e)
	    {
	        System.err.println("Error loading source code: " + name);
	        e.printStackTrace();
	    }

	    return source.toString();
	}
}
