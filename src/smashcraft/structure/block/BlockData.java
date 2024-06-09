package smashcraft.structure.block;

public class BlockData {
    private final BlockType type;
    private byte faceMask;
    private final int x;
    private final int y;
    private final int z;

    public BlockData(BlockType type, int x, int y, int z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.faceMask = (byte) 63;
    }

    public void showFace(int face) {
        faceMask |= (byte) (1 << face);
    }

    public void hideFace(int face) {
        faceMask &= (byte) ~(1 << face);
    }

    public boolean isFaceHidden(int face) {
        return (faceMask & (1 << face)) == 0;
    }

    public BlockType getType() {
        return type;
    }

    public byte getFaceMask() {
        return faceMask;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}