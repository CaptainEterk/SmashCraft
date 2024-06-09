package smashcraft.renderer.camera;

import org.joml.Math;
import smashcraft.Input;
import smashcraft.window.Window;

import static org.lwjgl.glfw.GLFW.*;

public class CameraController extends Camera {
    private final float SPEED = 1f;

    private final Input input;

    private boolean wireframe;

    public CameraController(Window window) {
        this.input = new Input(window);
    }

    public void tick() {
        if (input.isMouseCaptured()) {

            // Calculate the movement of the player
            // Each move takes two keys and converts them each into a "0" or a "1"
            // Then, they are subtracted from each other two create a number that is either "-1", "0", or "1"
            int moveX = (input.isKeyPressed(GLFW_KEY_D) ? 0 : 1) - (input.isKeyPressed(GLFW_KEY_A) ? 0 : 1);
            int moveY = (input.isKeyPressed(GLFW_KEY_SPACE) ? 0 : 1) - (input.isKeyPressed(GLFW_KEY_LEFT_SHIFT) ? 0 : 1);
            int moveZ = (input.isKeyPressed(GLFW_KEY_W) ? 0 : 1) - (input.isKeyPressed(GLFW_KEY_S) ? 0 : 1);
            move(moveX, moveY, moveZ, SPEED);

            // Update the mouse delta and rotate accordingly
            input.updateMouseDelta();
            rotateX((float) input.getMouseDeltaY()/1000);
            rotateY((float) input.getMouseDeltaX()/1000);

            if (rotation.x > Math.PI/2) rotation.x = (float) Math.PI/2;
            if (rotation.x < -Math.PI/2) rotation.x = (float) -Math.PI/2;

            // If the key "GLFW_KEY_ESCAPE" is pressed, release the mouse (the player wants to see the mouse again)
            if (input.isKeyPressed(GLFW_KEY_ESCAPE)) {
                input.releaseMouse();
            }

            // A toggleable setting for if you want to see a wireframe or not
            wireframe = input.isKeyPressed(GLFW_KEY_F1);
        }
        else if (input.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
            // Capture the mouse
            input.captureMouse();

            // Stop the camera from moving a lot when you first click on the screen
            input.updateMouseDelta();
        }
    }

    public boolean wireframe() {
        return wireframe;
    }
}