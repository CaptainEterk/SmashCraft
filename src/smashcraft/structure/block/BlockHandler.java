package smashcraft.structure.block;

import smashcraft.structure.block.blocks.Bedrock;
import smashcraft.structure.block.blocks.Cobblestone;

import java.util.HashMap;
import java.util.Map;

public class BlockHandler {
    private final Map<BlockType, Block> blockMap;

    public BlockHandler() {
        blockMap = new HashMap<>();
    }

    public void initBlocks() {
        blockMap.put(BlockType.COBBLE_STONE, new Cobblestone().init());
        blockMap.put(BlockType.BEDROCK, new Bedrock().init());
    }

    public Block getBlock(BlockType type) {
        return blockMap.get(type);
    }
}