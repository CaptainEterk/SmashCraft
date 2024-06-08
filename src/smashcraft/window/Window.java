package smashcraft.window;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private final long window;

    public Window(long window) {
        this.window = window;
    }

    public void show() {
        glfwShowWindow(window);
    }

    public void maximize() {
        glfwMaximizeWindow(window);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public long getWindow() {
        return window;
    }
}