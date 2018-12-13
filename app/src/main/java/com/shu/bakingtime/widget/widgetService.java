package com.shu.bakingtime.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class widgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewFactory(this.getApplicationContext(), intent);
    }
}
