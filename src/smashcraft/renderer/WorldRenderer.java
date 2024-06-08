package smashcraft.renderer;

import org.lwjgl.opengl.GL11;
import smashcraft.renderer.shaders.ShaderProgram;
import smashcraft.renderer.textures.TextureLoader;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class WorldRenderer {
    private final int textureId;
    private int VAO, VBO;
    private ShaderProgram shaderProgram;

    public WorldRenderer(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
        textureId = TextureLoader.loadTexture("texture.jpg");
        initRenderData();
    }

    private void initRenderData() {
        float[] vertices = {
                // Positions        // Texture Coords
                -0.5f,  0.5f, -2.0f,  0.0f, 1.0f, // Top-left
                0.5f,  0.5f, -2.0f,  1.0f, 1.0f, // Top-right
                0.5f, -0.5f, -2.0f,  1.0f, 0.0f, // Bottom-right
                -0.5f, -0.5f, -2.0f,  0.0f, 0.0f  // Bottom-left
        };
        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        int EBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        float t = (float) (Math.sin(glfwGetTime()) / 2 + 0.5);

        glUseProgram(shaderProgram.getShaderProgram("default"));

        // Bind the texture
        glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // Render the quad
        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        glUseProgram(0);
    }
}
