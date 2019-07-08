package com.tebet.mojual.common.base;

import android.content.Context;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;

import java.util.List;

public class BaseSimpleAdapter<T> extends ArrayAdapter<T> {
    public BaseSimpleAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public BaseSimpleAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public BaseSimpleAdapter(@NonNull Context context, int resource, @NonNull T[] objects) {
        super(context, resource, objects);
    }

    public BaseSimpleAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public BaseSimpleAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }

    public BaseSimpleAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
