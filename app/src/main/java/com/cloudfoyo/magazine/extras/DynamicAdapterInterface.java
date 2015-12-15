package com.cloudfoyo.magazine.extras;

/**
 * Created by nilesh on 15/12/15.
 */
public interface DynamicAdapterInterface<T> {

    public void addItem(T item);
    public void clearItems();
}
