package com.nbrk.rates;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by rpagyc on 30.10.2015.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListProvider(this.getApplicationContext(), intent);
    }
}
