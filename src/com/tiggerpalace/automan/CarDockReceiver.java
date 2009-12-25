package com.tiggerpalace.automan;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;

public class CarDockReceiver extends BroadcastReceiver {
  
  @Override
  public void onReceive(Context ctx, Intent intent) {
    if(intent.hasCategory(Intent.CATEGORY_CAR_DOCK) ||
       intent.hasCategory(Intent.CATEGORY_DESK_DOCK)) {
      ctx.startActivity(new Intent(ctx, DroidCarDock.class));
    }
  }
}
