package com.demo.kotlin.utils.bus.entity

class DeadEvent
/**
 * Creates a new DeadEvent.
 *
 * @param source object broadcasting the DeadEvent (generally the [Bus]).
 * @param event  the event that could not be delivered.
 */(val source: Any, val event: Any)