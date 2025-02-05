package g.api.adapter.simple;

/**
 * A interface that defines what a View Holder Creator should do.
 *
 * @param <MyAdapterData> the generic type of the data in each item of a list.
 * @author http://www.liaohuqiu.net
 */
public interface GSimpleVHCreatorSuper<MyAdapterData> {
    GSimpleViewHolder<MyAdapterData> createViewHolder(int position);
}