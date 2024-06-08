package smashcraft.renderer.camera;

import org.joml.Math;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Camera {
    protected final Vector3f position;
    protected final Vector3f rotation;

    public Camera() {
        this.position = new Vector3f();
        this.rotation = new Vector3f();
    }

    public void move(float mx, float my, float mz, float MOVEMENT_SPEED) {
        // Calculate movement direction based on player's rotation
        float moveX = mz * MOVEMENT_SPEED * Math.sin(rotation.y()) +
                mx * MOVEMENT_SPEED * Math.sin(rotation.y() + (float)Math.PI / 2);
        float moveZ = mz * MOVEMENT_SPEED * Math.cos(rotation.y()) +
                mx * MOVEMENT_SPEED * Math.cos(rotation.y() + (float)Math.PI / 2);

        // Apply movement to player's position
        position.add(-moveX, -my*MOVEMENT_SPEED, moveZ);

        // Optional: Print position for debugging
//        System.out.println(position.x() + " " + position.y() + " " + position.z());
    }

    protected void rotateX(float angle) {
        rotation.x += angle;
        if (rotation.x > Math.PI) rotation.x = (float) Math.PI;
        if (rotation.x < -Math.PI) rotation.x = (float) -Math.PI;
    }

    protected void rotateY(float angle) {
        rotation.y += angle;
        rotation.y %= (float) (Math.PI*2);
        if (rotation.y() < 0) {
            rotation.y += (float) (Math.PI*2);
        }
    }

    public Vector3fc getPosition() {
        return position;
    }

    public Vector3fc getRotation() {
        return rotation;
    }
}