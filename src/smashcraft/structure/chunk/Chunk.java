package smashcraft.structure.chunk;

import smashcraft.structure.block.BlockData;
import smashcraft.structure.block.BlockHandler;
import smashcraft.structure.block.BlockType;

import java.util.Random;

public class Chunk {
    public static final int CHUNK_WIDTH = 16;
    public static final int CHUNK_LENGTH = 16;
    public static final int CHUNK_HEIGHT = 256;

    private final BlockData[] blocks; // This might be improved by adding a "length" thing. (blockData and length) so that the same data doesn't need to be used again and again

    public Chunk() {
        this.blocks = new BlockData[CHUNK_WIDTH * CHUNK_LENGTH * CHUNK_HEIGHT];
    }

    public void fill() {
        Random random = new Random(0);
        for (int x = 0; x < CHUNK_WIDTH; x++) {
            for (int z = 0; z < CHUNK_LENGTH; z++) {
                for (int y = 0; y < CHUNK_HEIGHT; y++) {
                    setBlockData(x, y, z, random.nextBoolean() ? new BlockData(BlockType.COBBLE_STONE, x, y, z) : new BlockData(BlockType.BEDROCK, x, y, z));
                }
            }
        }
    }

    public void bakeAll(BlockHandler blockHandler) {
        for (int x = 0; x < CHUNK_WIDTH; x++) {
            for (int z = 0; z < CHUNK_LENGTH; z++) {
                for (int y = 0; y < CHUNK_HEIGHT; y++) {
                    BlockData current = getBlockData(x, y, z);
                    BlockData top = getBlockData(x, y + 1, z);
                    BlockData bottom = getBlockData(x, y - 1, z);
                    BlockData north = getBlockData(x, y, z + 1);
                    BlockData south = getBlockData(x, y, z - 1);
                    BlockData east = getBlockData(x + 1, y, z);
                    BlockData west = getBlockData(x - 1, y, z);

                    bake(current, top, blockHandler, 0);
                    bake(current, bottom, blockHandler, 1);
                    bake(current, north, blockHandler, 2);
                    bake(current, south, blockHandler, 3);
                    bake(current, east, blockHandler, 4);
                    bake(current, west, blockHandler, 5);
                }
            }
        }
    }

    private void bake(BlockData currentBlockData, BlockData blockData, BlockHandler blockHandler, int face) {
        if (blockData != null) {
            currentBlockData.hideFace(face);
        }
        else {
            currentBlockData.showFace(face);
        }
    }

    public BlockData getBlockData(int x, int y, int z) {
        if (!checkBounds(x, CHUNK_WIDTH) || !checkBounds(y, CHUNK_HEIGHT) || !checkBounds(z, CHUNK_LENGTH)) {
            return null;
        }
        int i = getBlockIndex(x, y, z);
        return blocks[i];
    }

    public void setBlockData(int x, int y, int z, BlockData data) {
        blocks[getBlockIndex(x, y, z)] = data;
    }

    private int getBlockIndex(int x, int y, int z) {
        return x + (z * CHUNK_WIDTH) + (y * CHUNK_WIDTH * CHUNK_LENGTH);
    }

    private boolean checkBounds(int value, int max) {
        return value >= 0 && max > value;
    }
}