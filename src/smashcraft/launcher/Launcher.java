package smashcraft.launcher;

import smashcraft.renderer.Renderer;
import smashcraft.renderer.WorldRenderer;
import smashcraft.renderer.camera.CameraController;
import smashcraft.renderer.shaders.ShaderProgram;
import smashcraft.window.Window;
import smashcraft.window.WindowFactory;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;

public class Launcher {
    public static void main(String[] args) {
        Window window = new Window(WindowFactory.createWindow(500, 500, "SmashCraft Test Window"));

        Map<String, Integer> shaderPaths = new HashMap<>();
        shaderPaths.put("assets/shaders/vertex_shader.vert", GL_VERTEX_SHADER);
        shaderPaths.put("assets/shaders/fragment_shader.frag", GL_FRAGMENT_SHADER);

        ShaderProgram shaderProgram = new ShaderProgram();
        shaderProgram.addShaderProgram("default", shaderPaths);

        Renderer renderer = new Renderer(new WorldRenderer(shaderProgram), shaderProgram);
        renderer.init();

        CameraController cameraController = new CameraController(window);

        window.maximize();
        window.show();

        long oldTime = System.currentTimeMillis();
        int frames = 0;

        // Run the rendering loop until the user has attempted to close the window.
        while (!window.shouldClose()) {
            if (System.currentTimeMillis() - oldTime > 1000) {
                oldTime += 1000;
                System.out.println(frames);
                frames = 0;
            }
            renderer.render(window.getWindow(), cameraController);
            cameraController.tick();
            frames++;
        }
    }
}