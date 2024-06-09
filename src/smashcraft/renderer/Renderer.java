package smashcraft.renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.opengl.GL11;
import smashcraft.renderer.camera.CameraController;
import smashcraft.renderer.shaders.ShaderProgram;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20C.*;

public class Renderer {
    private final WorldRenderer worldRenderer;
    private final ShaderProgram shaderProgram;
    private int shaderID;

    // Uniform locations
    private int viewLoc;
    private int projectionLoc;

    public Renderer(WorldRenderer worldRenderer, ShaderProgram shaderProgram) {
        this.worldRenderer = worldRenderer;
        this.shaderProgram = shaderProgram;
    }

    /**
     * Initializes the OpenGL state for the rendering.
     */
    public void init() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);

        shaderID = shaderProgram.getShaderProgram("default");

        // Get uniform locations
        viewLoc = glGetUniformLocation(shaderID, "view");
        projectionLoc = glGetUniformLocation(shaderID, "projection");

        updateProjection(1920f/1080f, 90, 0.1f, 1000);
        glViewport(0, 0, 1920, 1080);
    }

    /**
     * Clears the window then calls <code>worldRenderer.render()</code>
     * After the world has been rendered, <code>glfwSwapBuffers</code> is run.
     * Finally, poll for events using <code>glfwPollEvents</code>.
     *
     * @param window           The OpenGL window for <code>glfwSwapBuffers</code>
     * @param cameraController The camera to render from
     */
    public void render(long window, CameraController cameraController) {
        // Clear the screen
        glClear(GL11.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer

        // Use the shader program
        glUseProgram(shaderID);

        // Handle the camera view matrix
        Matrix4f viewMatrix = getViewMatrix(cameraController);

        // Set view matrix uniform
        glUniformMatrix4fv(viewLoc, false, viewMatrix.get(new float[16]));

        if (cameraController.wireframe()) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }
        else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }

        // Render the world
        worldRenderer.render();

        // Cleanup
        glUseProgram(0); // Shader program

        // Swap the color buffers
        glfwSwapBuffers(window);

        // Poll for window events. The key callback above will only be invoked during this call.
        glfwPollEvents();
    }

    private static Matrix4f getViewMatrix(CameraController cameraController) {
        Vector3fc rotation = cameraController.getRotation();
        Vector3fc position = cameraController.getPosition();

        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();

        // Apply rotation in Yaw-Pitch-Roll order
        viewMatrix
                .rotate((float) rotation.x(), new Vector3f(1, 0, 0))
                .rotate((float) rotation.y(), new Vector3f(0, 1, 0))
                .translate(-position.x(), -position.y(), -position.z());

        // Translate the camera to its position
        viewMatrix.translate(-position.x(), -position.y(), -position.z());
        return viewMatrix;
    }

    public void updateProjection(float aspectRatio, float fov, float near, float far) {
        Matrix4f projectionMatrix = new Matrix4f().identity().perspective((float) Math.toRadians(fov), aspectRatio, near, far);
        glUseProgram(shaderID); // Make sure the shader program is active
        glUniformMatrix4fv(projectionLoc, false, projectionMatrix.get(new float[16]));
        glUseProgram(0); // Optionally deactivate the shader program
    }
}