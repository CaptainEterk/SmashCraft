package smashcraft.structure.block.blocks;

import smashcraft.structure.block.Block;
import smashcraft.structure.block.BlockRenderType;
import smashcraft.structure.block.BlockType;

public class Bedrock extends Block {
    @Override
    public BlockRenderType getRenderType() {
        return BlockRenderType.BLOCK_SAME;
    }

    @Override
    public BlockType getType() {
        return BlockType.BEDROCK;
    }

    @Override
    public int[] getTextureCoords() {
        return new int[] {3, 0};
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}