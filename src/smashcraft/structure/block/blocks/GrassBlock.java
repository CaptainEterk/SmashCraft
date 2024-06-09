package smashcraft.structure.block.blocks;

import smashcraft.structure.block.Block;
import smashcraft.structure.block.BlockRenderType;
import smashcraft.structure.block.BlockType;

public class GrassBlock extends Block {

    @Override
    public BlockRenderType getRenderType() {
        return BlockRenderType.BLOCK_SIDES;
    }

    @Override
    public BlockType getType() {
        return BlockType.GRASS_BLOCK;
    }

    @Override
    public int[] getTextureCoords() {
        return new int[] {0, 1, 1, 0, 0, 0}; // Top, bottom, sides
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}