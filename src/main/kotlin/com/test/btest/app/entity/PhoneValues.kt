package com.test.btest.app.values

import java.time.LocalDateTime

@JvmInline value class PhoneId(val value: Int)
@JvmInline value class Brand(val value: String)
@JvmInline value class Device(val value: String)
@JvmInline value class Availability(val value: Boolean)
@JvmInline value class Person(val value: String)
@JvmInline value class ExtractedDate(val value: LocalDateTime)