package com.example.liuh.eventbus;

/**
 * Created by Administrator on 2016-09-07.
 */
public class MessageEvent {

    public final int key;
    public final Object val;

    public MessageEvent(int key,Object val) {
        this.key = key;
        this.val = val;
    }
}
