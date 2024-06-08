package smashcraft.renderer.camera;

import smashcraft.Input;
import smashcraft.window.Window;

import static org.lwjgl.glfw.GLFW.*;

public class CameraController extends Camera {
    private final float SPEED = 0.05f;

    private final Input input;

    public CameraController(Window window) {
        this.input = new Input(window);
    }

    public void tick() {
        if (input.isMouseCaptured()) {
            int moveX = (input.isKeyPressed(GLFW_KEY_D) ? 0 : 1) - (input.isKeyPressed(GLFW_KEY_A) ? 0 : 1);
            int moveY = (input.isKeyPressed(GLFW_KEY_SPACE) ? 0 : 1) - (input.isKeyPressed(GLFW_KEY_LEFT_SHIFT) ? 0 : 1);
            int moveZ = (input.isKeyPressed(GLFW_KEY_W) ? 0 : 1) - (input.isKeyPressed(GLFW_KEY_S) ? 0 : 1);
            move(moveX, moveY, moveZ, SPEED);
            if (input.isKeyPressed(GLFW_KEY_ESCAPE)) {
                input.releaseMouse();
            }

            // Update the mouse delta and rotate accordingly
            input.updateMouseDelta();
            rotateX((float) input.getMouseDeltaY()/1000);
            rotateY((float) input.getMouseDeltaX()/1000);
        }
        else if (input.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
            input.captureMouse();
        }
    }
}