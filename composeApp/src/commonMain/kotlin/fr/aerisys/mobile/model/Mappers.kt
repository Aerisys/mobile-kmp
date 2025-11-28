package fr.aerisys.mobile.model

import fraerisysmobile.db.Cameras

fun Cameras.toCameraBean() = CameraBean(
    id = this.id,
    userId = this.user_id,
    name = this.name,
    macAddress = this.mac_address,
    ipAddress = this.ip_address,
    imageFormat = this.image_format,
    imageQuality = this.image_quality,
    imageDimension = this.image_dimension,
    firmwareVersion = this.firmware_version,
    firmwareLastUpdate = this.firmware_last_update
)