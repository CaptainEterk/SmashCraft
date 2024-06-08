package smashcraft.renderer.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20C.*;

public class ShaderProgram {
    private final Map<String, Integer> shaderPrograms;

    public ShaderProgram() {
        shaderPrograms = new HashMap<>();
    }

    public int getShaderProgram(String name) {
        return shaderPrograms.get(name);
    }
    
    private String readShaderFile(String path) {
        StringBuilder shaderSource = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return shaderSource.toString();

    }

    public void addShaderProgram(String name, Map<String, Integer> shaderPaths) {
        int programID = glCreateProgram();
        if (programID == 0) {
            throw new RuntimeException("Error creating shader program.");
        }

        shaderPaths.forEach((path, type) -> {
            String contents = readShaderFile(path);

            int shaderID = glCreateShader(type);
            if (shaderID == 0) {
                throw new RuntimeException("Error creating shader.");
            }

            glShaderSource(shaderID, contents);
            glCompileShader(shaderID);
            if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
                throw new RuntimeException("Error compiling shader: " + glGetShaderInfoLog(shaderID));
            }

            glAttachShader(programID, shaderID);

            glDeleteShader(shaderID);
        });

        glLinkProgram(programID);

        shaderPrograms.put(name, programID);
    }
}