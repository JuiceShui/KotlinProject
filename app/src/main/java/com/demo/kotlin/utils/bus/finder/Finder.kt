package com.demo.kotlin.utils.bus.finder

import com.demo.kotlin.utils.bus.entity.EventType
import com.demo.kotlin.utils.bus.entity.ProducerEvent
import com.demo.kotlin.utils.bus.entity.SubscriberEvent

interface Finder {
    fun findAllProducers(listener: Any): Map<EventType, ProducerEvent>
    fun findAllSubscribers(listener: Any): Map<EventType, Set<SubscriberEvent>>

    companion object {
        val ANNOTATED: Finder = object : Finder {
            override fun findAllProducers(listener: Any): Map<EventType, ProducerEvent> {
                return AnnotatedFinder.findAllProducers(listener)
            }

            override fun findAllSubscribers(listener: Any): Map<EventType, Set<SubscriberEvent>> {
                return AnnotatedFinder.findAllSubscribers(listener)
            }
        }
    }
}