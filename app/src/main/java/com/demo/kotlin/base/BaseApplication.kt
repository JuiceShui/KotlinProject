package com.demo.kotlin.base

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.kotlin.utils.Utils
import java.lang.ref.WeakReference
import java.util.*

open class BaseApplication : Application() {
    /**
     * Record mUser's info.
     * The userId and the token.
     */
    private var mResumed = 0
    private var mPaused = 0
    private var mStarted = 0
    private var mStopped = 0

    /**
     * @return The milliseconds time of activity's last call [Activity.onPause]
     */
    // Time of the last paused.
    var lastPausedTime: Long = 0
        private set

    // The list of all mActivities.
    private val mActivities = LinkedList<AppCompatActivity>()

    // The top activity is resumed to user.
    private var mTopActivity: WeakReference<Activity>? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        Utils().init(this)
        registerActivity()
    }


    /**
     * @return When the application is resumed to user, return true.
     */
    val isAppVisible: Boolean
        get() = mStarted > mStopped


    /**
     * Register the lifecycle callbacks of activity
     */
    private fun registerActivity() {
        //TODO Bundle 必须为Bundle？   ！！！与activity中oncreate 对应，不然crash
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            // I use four separate variables here. You can, of course, just use two and
            // increment/decrement them instead of using four and incrementing them all.
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityDestroyed(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                ++mResumed
            }

            override fun onActivityPaused(activity: Activity) {
                ++mPaused
                lastPausedTime = System.currentTimeMillis()
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {
                ++mStarted
            }

            override fun onActivityStopped(activity: Activity) {
                ++mStopped
            }
        })
    }

    /**
     * Add an activity to the record list.
     */
    fun addActivity(activity: AppCompatActivity) {
        mActivities.add(activity)
    }

    /**
     * @return The record list.
     */
    fun getActivities(): LinkedList<AppCompatActivity> {
        return mActivities
    }

    /**
     * Remove an activity from the record list.
     */
    fun removeActivity(activity: Activity?) {
        mActivities.remove(activity)
    }

    /**
     * Record the activity use [.mTopActivity].
     * Must call this method in activity's [Activity.onResume]
     */
    fun resumeActivity(activity: Activity) {
        mTopActivity = WeakReference(activity)
    }

    /**
     * @return The top activity, maybe null.
     */
    fun getTopActivity(): Activity? {
        return mTopActivity!!.get()
    }

    /**
     * @param cls The class must extends [Activity] or subclass of it.
     * @return If the activity is exist in [.mActivities], return true.
     */
    fun isExist(cls: Class<*>): Boolean {
        for (activity in mActivities) {
            if (activity.javaClass.simpleName == cls.simpleName) {
                return true
            }
        }
        return false
    }

    /**
     * Finish mActivities in an appointed list.
     *
     * @param activities Activities to be finished.
     */
    fun finishActivity(vararg activities: Activity?) {
        for (activity in activities) {
            activity?.finish()
        }
    }

    /**
     * Finish all mActivities except an appointed list.
     *
     * @param except The exceptional list.
     */
    fun finishAllActivities(vararg except: Class<*>) {
        for (activity in mActivities) {
            for (c in except) {
                if (activity.javaClass.name != c.name) {
                    activity.finish()
                }
            }
        }
    }

    /**
     * Finish all mActivities.
     */
    fun finishAllActivities() {
        for (activity in mActivities) {
            activity.finish()
        }
    }

    /**
     * Recreate all mActivities.
     */
    fun recreateActivities() {
        for (activity in mActivities) {
            activity.recreate()
        }
    }

    /**
     * Stop a service.
     */
    fun stopService(cls: Class<*>?) {
        stopService(Intent(this, cls))
    }

    /**
     * Start a service.
     */
    fun startService(cls: Class<*>?) {
        startService(Intent(this, cls))
    }

    /**
     * Exit application.
     */
    fun exit() {
        finishAllActivities()
        //        System.exit(0);
    }

    fun finishOther(activity: Class<*>) {
        try {
            for (activity1 in mActivities) {
                if (activity1.javaClass != activity) {
                    activity1.finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun finishOtherAndLaunch(activity: Class<*>) {
        try {
            val intent = Intent(this, activity)
            startActivity(intent)
        } catch (e: Exception) {
            try {
                val intent = Intent(this, activity)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } catch (ignored: Exception) {
            }
        }
        finishOther(activity)
    }

    companion object {
        // The single instance of Application.
        @get:Synchronized
        var instance: BaseApplication? = null
            private set
    }
}