package fr.aerisys.mobile.model

import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class CameraBean(
    val id: Long?,
    val userId: Long?,
    val name: String?,
    var macAddress: String?,
    var ipAddress: String?,
    val imageFormat: String?,
    val imageQuality: String?,
    var imageDimension: String?,
    val firmwareVersion: String?,
    val firmwareLastUpdate: Long?, // timestamp
) {
    @OptIn(ExperimentalTime::class)
    constructor() : this(
        null,
        null,
        "",
        "MAC-${
            Random.nextInt(
                1000,
                9999
            )}",
        "",
        "JPEG",
        "30",
        "640x480",
        "1.0.0",
        Clock.System.now().toEpochMilliseconds() / 1000
    )
}

data class DroneBean(
    val id: Long,
    val userId: Long,
    val name: String,
    val macAddress: String?,
    val ipAddress: String?,
    val flightMode: String?,
    val motorPower: Long?,
    val altitudeLimit: Long?,
    val firmwareVersion: String?,
    val firmwareLastUpdate: Long?,
    val addedAt: Long?,
    val updatedAt: Long?
)

data class UserBean(
    val id: Long,
    val userName: String,
    val email: String,
    val password: String,
    val createdAt: Long
)
