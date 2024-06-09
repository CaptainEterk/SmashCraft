package smashcraft.structure.block;

public enum BlockRenderType {
    BLOCK_SAME(BlockShape.BLOCK),   // All the faces have the same texture
    BLOCK_SIDES(BlockShape.BLOCK),  // All the faces on the sides have the same texture but the top and bottom faces are unique
    BLOCK_UNIQUE(BlockShape.BLOCK), // All the faces are unique
    SPRITE4_ONE(BlockShape.SPRITE4),  // A four-sided sprite that is one block tall
    SPRITE4_TWO(BlockShape.SPRITE4),  // A four-sided sprite that is two blocks tall
    SPRITE2_ONE(BlockShape.SPRITE2),  // A two-sided sprite that is one block tall
    SPRITE2_TWO(BlockShape.SPRITE2)   // A two-sided sprite that is two blocks tall
    ;

    private final BlockShape blockShape;

    BlockRenderType(BlockShape blockShape) {
        this.blockShape = blockShape;
    }

    public BlockShape getShape() {
        return blockShape;
    }
}