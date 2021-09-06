package com.lib.eventbus

class EventBusFire{
    var eventName: String = ""
    var valueDouble: Double = 0.0
    var valueString: String = ""

    constructor(eventName:String, valueDouble:Double) {
        this.eventName = eventName
        this.valueDouble = valueDouble
    }

    constructor(eventName:String, valueString:String) {
        this.eventName = eventName
        this.valueString = valueString
    }
}