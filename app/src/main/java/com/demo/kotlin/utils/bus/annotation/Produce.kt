package com.demo.kotlin.utils.bus.annotation

import com.demo.kotlin.utils.bus.thread.EventThread
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class Produce(
    val tags: Array<Tag> = [],
    val thread: EventThread = EventThread.MAIN_THREAD
)
