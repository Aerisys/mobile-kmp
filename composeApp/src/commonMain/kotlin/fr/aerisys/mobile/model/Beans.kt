package fr.aerisys.mobile.model

data class CameraBean(
    val id: Long,
    val user_id: Long,
    val name: String,
    val mac_address: String,
    val ip_address: String,
    val image_format: String,
    val image_quality: String,
    val image_dimension: String,
    val firmware_version: String,
    val firmware_last_update: Int, // timestamp
)

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
    val createdAt: Long,
)
