package smashcraft;

import smashcraft.window.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private Window window;
    private double[] oldMousePos;
    private double mouseDeltaX;
    private double mouseDeltaY;

    public Input(Window window) {
        this.window = window;
        oldMousePos = new double[2];
    }

    public boolean isKeyPressed(int key) {
        return glfwGetKey(window.getWindow(), key) == GLFW_PRESS;
    }

    public boolean isMouseButtonPressed(int button) {
        return glfwGetMouseButton(window.getWindow(), button) == GLFW_PRESS;
    }

    public void updateMouseDelta() {
        double[] xPos = new double[1];
        double[] yPos = new double[1];
        glfwGetCursorPos(window.getWindow(), xPos, yPos);
        double[] pos = new double[] {
                xPos[0],
                yPos[0]
        };
        mouseDeltaX = pos[0]-oldMousePos[0];
        mouseDeltaY = pos[1]-oldMousePos[1];
        oldMousePos = pos;
    }

    public boolean isMouseCaptured() {
        return glfwGetInputMode(window.getWindow(), GLFW_CURSOR) == GLFW_CURSOR_DISABLED;
    }

    public void captureMouse() {
        glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public void releaseMouse() {
        glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public double getMouseDeltaX() {
        return mouseDeltaX;
    }

    public double getMouseDeltaY() {
        return mouseDeltaY;
    }
}