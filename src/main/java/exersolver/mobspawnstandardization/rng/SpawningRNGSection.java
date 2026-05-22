package exersolver.mobspawnstandardization.rng;

public class SpawningRNGSection {
    public final int chunkX;
    public final int chunkZ;
    public final int dimensionId;
    public final int categoryId;

    public SpawningRNGSection(int chunkX, int chunkZ, int dimensionId, int categoryId) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.dimensionId = dimensionId;
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SpawningRNGSection))
            return false;
        SpawningRNGSection other = (SpawningRNGSection) object;
        return other.chunkX == this.chunkX && other.chunkZ == this.chunkZ && other.dimensionId == this.dimensionId && other.categoryId == this.categoryId;
    }

    @Override
    public int hashCode() {
        int hash = this.chunkX;
        hash = 31 * hash + this.chunkZ;
        hash = 31 * hash + this.dimensionId;
        hash = 31 * hash + this.categoryId;
        return hash;
    }
}
