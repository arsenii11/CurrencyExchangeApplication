package com.core.schedulers

import com.core.schedulers.Schedulers
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

object AppSchedulers : Schedulers {
    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
    override fun io(): Scheduler = io.reactivex.schedulers.Schedulers.io()
}