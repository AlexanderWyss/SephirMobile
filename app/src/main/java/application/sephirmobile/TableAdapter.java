package application.sephirmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class TableAdapter<T> extends ArrayAdapter<T> {

     TableAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }

    public abstract View getColumns();
}
