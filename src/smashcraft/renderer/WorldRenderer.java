package smashcraft.renderer;

import org.joml.Matrix4f;
import smashcraft.renderer.shaders.ShaderProgram;
import smashcraft.renderer.textures.TextureLoader;
import smashcraft.structure.block.Block;
import smashcraft.structure.block.BlockData;
import smashcraft.structure.block.BlockHandler;
import smashcraft.structure.block.BlockType;
import smashcraft.structure.chunk.Chunk;

import java.util.*;

import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.glGetUniformLocation;
import static org.lwjgl.opengl.GL20C.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;

public class WorldRenderer {
    private final int textureId;
    private final BlockHandler blockHandler;
    private Chunk chunk;
    private int modelLocation;

    public WorldRenderer(ShaderProgram shaderProgram) {
        this.textureId = TextureLoader.loadTexture("texture_atlas.png");
        this.blockHandler = new BlockHandler();
        this.blockHandler.initBlocks();
        initRenderData(shaderProgram);
    }

    private void initRenderData(ShaderProgram shaderProgram) {
        chunk = new Chunk();
        chunk.fill();
        chunk.bakeAll(blockHandler);
        modelLocation = glGetUniformLocation(shaderProgram.getShaderProgram("default"), "model");
    }

    public void render() {
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Cache blocks by type
        Map<BlockType, List<BlockData>> blockMap = new HashMap<>();

        for (int x = 0; x < Chunk.CHUNK_WIDTH; x++) {
            for (int z = 0; z < Chunk.CHUNK_LENGTH; z++) {
                for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
                    BlockData data = chunk.getBlockData(x, y, z);
                    blockMap.computeIfAbsent(data.getType(), k -> new ArrayList<>()).add(data);
                }
            }
        }

        // Render blocks by type
        for (Map.Entry<BlockType, List<BlockData>> entry : blockMap.entrySet()) {
            BlockType type = entry.getKey();
            List<BlockData> blockDataList = entry.getValue();
            Block block = blockHandler.getBlock(type);

            glBindVertexArray(block.getVAO());

            for (BlockData blockData : blockDataList) {
                int x = blockData.getX();
                int y = blockData.getY();
                int z = blockData.getZ();
                Matrix4f translation = new Matrix4f().translate(x, y, z);
                float[] matrix = new float[16];
                translation.get(matrix);

                glUniformMatrix4fv(modelLocation, false, matrix); // Set the matrix uniform

                for (int i = 0; i < 6; i++) {
                    if (!blockData.isFaceHidden(i)) {
                        renderFace(i);
                    }
                }
            }

            glBindVertexArray(0); // Unbind the VAO
        }
    }

    public void renderFace(int face_index) {
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, face_index * 24L); // 6 points for each face (3/triangle), and 4 bytes for each unsigned int. (6*4=24)
    }
}