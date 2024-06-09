package smashcraft.renderer.textures;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;
import static org.lwjgl.system.MemoryStack.stackPush;

public class TextureLoader {
    public static int loadTexture(String path) {
        String filePath = "assets/textures/" + path;

        int width, height;
        ByteBuffer imageBuffer;

        // Load image
        try (MemoryStack stack = stackPush()) {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channelsBuffer = stack.mallocInt(1);

            imageBuffer = STBImage.stbi_load(filePath, widthBuffer, heightBuffer, channelsBuffer, 4);
            if (imageBuffer == null) {
                throw new RuntimeException("Failed to load texture file " + filePath + "\n" + STBImage.stbi_failure_reason());
            }

            width = widthBuffer.get();
            height = heightBuffer.get();
        }

        // Create a new OpenGL texture
        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Assuming you're binding a texture
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);

        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);

        // Free the image memory
        STBImage.stbi_image_free(imageBuffer);

        return textureId;
    }
}