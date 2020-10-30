package com.demo.kotlin.utils.bus.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class Tag(val value: String = DEFAULT) {
    companion object {
        const val DEFAULT = "rxbus_default_tag"
    }
}
