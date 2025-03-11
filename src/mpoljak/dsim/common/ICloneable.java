package mpoljak.dsim.common;

/**
 * Prototype
 * @param <T> type of cloned (and also cloning) instance.
 */
public interface ICloneable<T> {
    /**
     * Creates new instance based on internal state of prototype, which is called instance.
     * @return new instance with same internal state.
     */
    public T cloneInstance();
}
