package com.cyl.musiclake.data

import android.content.Context
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.db.FolderInfo
import io.reactivex.Observable

/**
 * Created by D22434 on 2018/1/8.
 */

object AppRepository {

    fun getPlayHistoryRepository(): Observable<List<Music>> {
        return Observable.create({ PlayHistoryLoader.getPlayHistory() })
    }

    fun getFolderInfosRepository(mContext: Context): Observable<List<FolderInfo>> {
        return FolderLoader.getFoldersWithSong(mContext)
    }

    fun getFolderSongsRepository(mContext: Context, path: String): Observable<List<Music>> {
        return Observable.create({ SongLoader.getSongListInFolder(mContext, path) })
    }

}
