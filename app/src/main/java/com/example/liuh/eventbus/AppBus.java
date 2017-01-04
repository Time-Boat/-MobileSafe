package com.example.liuh.eventbus;

import com.squareup.otto.Bus;

/**
 * Created by Administrator on 2016-09-14.
 */

public class AppBus extends Bus {
   private static AppBus bus;

   public static AppBus getInstance() {
       if (bus == null) {
               bus = new AppBus();
           }

       return bus;
    }
}