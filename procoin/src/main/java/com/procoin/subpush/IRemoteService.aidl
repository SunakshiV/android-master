// IRemoteService.aidl
package com.procoin.subpush;
import com.procoin.subpush.IReceivedCallback;

interface IRemoteService {
    int send(String reqUrl);
    void registerReceivedCallback(IReceivedCallback cb);
    void unregisterReceivedCallback(IReceivedCallback cb);
}

