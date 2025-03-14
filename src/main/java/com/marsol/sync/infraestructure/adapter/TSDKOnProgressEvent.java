package com.marsol.sync.infraestructure.adapter;

import com.sun.jna.Callback;

public interface TSDKOnProgressEvent extends Callback {
    void callback(int var1, int var2, int var3, int var4);
}
