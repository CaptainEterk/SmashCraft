package smashcraft.structure.block;

import static org.lwjgl.opengl.GL30C.*;

public abstract class Block {
    private int vao;
    private int vbo;

    public abstract BlockRenderType getRenderType();

    public abstract BlockType getType();

    public abstract int[] getTextureCoords();

    public abstract boolean isSolid();

    private float[] getRawTextureCoords(float textureIncrement) {
        int[] texCoords = getTextureCoords();
        float[] out;
        BlockRenderType renderType = getRenderType();
        if (renderType.getShape() == BlockShape.BLOCK) {
            out = new float[48]; // 4 points/face  6 faces/block  2 coords/point
            if (renderType == BlockRenderType.BLOCK_SAME) { // All the faces are the same texture
                for (int i = 0; i < 24; i++) { // 4 points/face  6 faces/block
                    float u = (float) texCoords[0] * textureIncrement;
                    float v = (float) texCoords[1] * textureIncrement;

                    switch (i % 4) {
                        case 3:
                            v += textureIncrement;
                            break;
                        case 2:
                            u += textureIncrement;
                            v += textureIncrement;
                            break;
                        case 1:
                            u += textureIncrement;
                            break;
                    }

                    out[i * 2] = u;
                    out[i * 2 + 1] = v;
                }
            } else if (renderType == BlockRenderType.BLOCK_SIDES) {
                int ti = 0;
                for (int i = 0; i < 6; i++) { // 6 faces/block
                    for (int j = 0; j < 4; j++) { // 4 points/face
                        float u = (float) texCoords[ti] * textureIncrement;
                        float v = (float) texCoords[ti + 1] * textureIncrement;

                        // 0: Bottom Right
                        // 1: Bottom Left
                        // 2: Top left
                        // 3: Top Right
                        switch (j) {
                            case 0:
                                u += textureIncrement;
                                v += textureIncrement;
                                break;
                            case 1:
                                v += textureIncrement;
                                break;
                            case 3:
                                u += textureIncrement;
                                break;
                        }

                        out[(i * 4 + j) * 2] = u;
                        out[(i * 4 + j) * 2 + 1] = v;
                    }
                    if (ti < 4) {
                        ti += 2;
                    }
                }
            }
        } else {
            out = new float[0];
        }
        return out;
    }

    public Block init() {
        float[] vertices = getVertices(getRenderType());
        float[] textureCoords = getRawTextureCoords(0.25f);
        int[] indices = getIndices(getRenderType());
        int ebo;

        // Generate VAO, VBO, and EBO
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ebo = glGenBuffers();

        // Bind VAO
        glBindVertexArray(vao);

        // Bind VBO and upload vertex data
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * 4L + textureCoords.length * 4L, GL_STATIC_DRAW);

        // Upload vertex positions
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Upload texture coordinates
        glBufferSubData(GL_ARRAY_BUFFER, vertices.length * 4L, textureCoords);

        // Bind EBO and upload index data
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Set vertex attribute pointers
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 2 * Float.BYTES, vertices.length * 4L);
        glEnableVertexAttribArray(1);

        // Unbind buffers (the VAO keeps the state)
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        return this;
    }

    private int[] getIndices(BlockRenderType renderType) {
        return switch (renderType.getShape()) {
            case BLOCK -> new int[]{
                    // Top face
                    0, 1, 2,
                    2, 3, 0,

                    // Bottom face
                    4, 5, 6,
                    6, 7, 4,

                    // North face
                    8, 9, 10,
                    10, 11, 8,

                    // South face
                    12, 13, 14,
                    14, 15, 12,

                    // East face
                    16, 17, 18,
                    18, 19, 16,

                    // West face
                    20, 21, 22,
                    22, 23, 20
            };
            default -> throw new UnsupportedOperationException("Doesn't support type " + renderType);
        };
    }

    private float[] getVertices(BlockRenderType renderType) {
        return switch (renderType) {
            case BLOCK_UNIQUE, BLOCK_SIDES, BLOCK_SAME -> new float[]{
                    // Top face
                    0.0f, 1.0f, 1.0f,  // Front-top-left
                    1.0f, 1.0f, 1.0f,  // Front-top-right
                    1.0f, 1.0f, 0.0f,  // Back-top-right
                    0.0f, 1.0f, 0.0f,  // Back-top-left

                    // Bottom face
                    0.0f, 0.0f, 1.0f,  // Front-bottom-left
                    1.0f, 0.0f, 1.0f,  // Front-bottom-right
                    1.0f, 0.0f, 0.0f,  // Back-bottom-right
                    0.0f, 0.0f, 0.0f,  // Back-bottom-left

                    // North face
                    0.0f, 0.0f, 1.0f,  // Bottom-left
                    1.0f, 0.0f, 1.0f,  // Bottom-right
                    1.0f, 1.0f, 1.0f,  // Top-right
                    0.0f, 1.0f, 1.0f,  // Top-left

                    // South face
                    0.0f, 0.0f, 0.0f,  // Bottom-left
                    1.0f, 0.0f, 0.0f,  // Bottom-right
                    1.0f, 1.0f, 0.0f,  // Top-right
                    0.0f, 1.0f, 0.0f,  // Top-left

                    // East face
                    1.0f, 0.0f, 1.0f,  // Front-bottom-right
                    1.0f, 0.0f, 0.0f,  // Back-bottom-right
                    1.0f, 1.0f, 0.0f,  // Back-top-right
                    1.0f, 1.0f, 1.0f,  // Front-top-right

                    // West face
                    0.0f, 0.0f, 1.0f,  // Front-bottom-left
                    0.0f, 0.0f, 0.0f,  // Back-bottom-left
                    0.0f, 1.0f, 0.0f,  // Back-top-left
                    0.0f, 1.0f, 1.0f   // Front-top-left
            };
            default -> throw new UnsupportedOperationException("Doesn't support type " + renderType);
        };
    }

    public int getVAO() {
        return vao;
    }

    public int getVBO() {
        return vbo;
    }
}