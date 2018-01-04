package com.cyl.musiclake.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.cyl.musiclake.IMusicService;
import com.cyl.musiclake.ui.music.model.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by D22434 on 2017/9/20.
 */

public class PlayManager {
    public static IMusicService mService = null;
    private static final WeakHashMap<Context, ServiceBinder> mConnectionMap;
    private static final long[] sEmptyList;
    private static ContentValues[] mContentValuesCache = null;

    static {
        mConnectionMap = new WeakHashMap<Context, ServiceBinder>();
        sEmptyList = new long[0];
    }

    public static final ServiceToken bindToService(final Context context,
                                                   final ServiceConnection callback) {

        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null) {
            realActivity = (Activity) context;
        }
        final ContextWrapper contextWrapper = new ContextWrapper(realActivity);
        contextWrapper.startService(new Intent(contextWrapper, MusicPlayService.class));
        final ServiceBinder binder = new ServiceBinder(callback,
                contextWrapper.getApplicationContext());
        if (contextWrapper.bindService(
                new Intent().setClass(contextWrapper, MusicPlayService.class), binder, 0)) {
            mConnectionMap.put(contextWrapper, binder);
            return new ServiceToken(contextWrapper);
        }
        return null;
    }

    public static void unbindFromService(final ServiceToken token) {
        if (token == null) {
            return;
        }
        final ContextWrapper mContextWrapper = token.mWrappedContext;
        final ServiceBinder mBinder = mConnectionMap.remove(mContextWrapper);
        if (mBinder == null) {
            return;
        }
        mContextWrapper.unbindService(mBinder);
        if (mConnectionMap.isEmpty()) {
            mService = null;
        }
    }

    public static final boolean isPlaybackServiceConnected() {
        return mService != null;
    }


    public static void playOnline(Music music) {
        try {
            mService.playOnline(music);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void play(int id) {
        try {
            mService.play(id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void playPause() {
        try {
            mService.playPause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void prev() {
        try {
            mService.prev();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void next() {
        try {
            mService.next();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void setLoopMode(int loopmode) {
        try {
            mService.setLoopMode(loopmode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void seekTo(int ms) {
        try {
            mService.seekTo(ms);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static int position() {
        try {
            return mService.position();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getCurrentPosition() {
        try {
            return mService.getCurrentPosition();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getDuration() {
        try {
            return mService.getDuration();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getSongName() {
        try {
            return mService.getSongName();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "湖科音乐";
    }

    public static String getSongArtist() {
        try {
            return mService.getSongArtist();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "湖科音乐";
    }

    public static boolean isPlaying() {
        try {
            return mService.isPlaying();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isPause() {
        try {
            return mService.isPause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Music getPlayingMusic() {
        try {
            return mService.getPlayingMusic();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Music> getPlayList() {
        try {
            return mService.getPlayList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void setPlayList(List<Music> playlist) {
        try {
            mService.setPlayList(playlist);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void clearQueue() {
        try {
            mService.clearQueue();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public static void refresh() {
        try {
            mService.refresh();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void removeFromQueue(int adapterPosition) {
        try {
            mService.removeFromQueue(adapterPosition);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static final class ServiceBinder implements ServiceConnection {
        private final ServiceConnection mCallback;
        private final Context mContext;


        public ServiceBinder(final ServiceConnection callback, final Context context) {
            mCallback = callback;
            mContext = context;
        }

        @Override
        public void onServiceConnected(final ComponentName className, final IBinder service) {
            mService = IMusicService.Stub.asInterface(service);
            if (mCallback != null) {
                mCallback.onServiceConnected(className, service);
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName className) {
            if (mCallback != null) {
                mCallback.onServiceDisconnected(className);
            }
            mService = null;
        }
    }

    public static final class ServiceToken {
        public ContextWrapper mWrappedContext;

        public ServiceToken(final ContextWrapper context) {
            mWrappedContext = context;
        }
    }
}