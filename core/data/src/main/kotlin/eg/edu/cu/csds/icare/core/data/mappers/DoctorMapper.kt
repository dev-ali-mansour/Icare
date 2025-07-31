package eg.edu.cu.csds.icare.core.data.mappers

import android.content.Context
import eg.edu.cu.csds.icare.core.data.dto.DoctorDto
import eg.edu.cu.csds.icare.core.data.local.db.entity.DoctorEntity
import eg.edu.cu.csds.icare.core.data.util.getFormattedTime
import eg.edu.cu.csds.icare.core.domain.model.Doctor

fun DoctorDto.toDoctorEntity(): DoctorEntity =
    DoctorEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        clinicId = clinicId,
        email = email,
        phone = phone,
        specialty = specialty,
        fromTime = fromTime,
        toTime = toTime,
        rating = rating,
        price = price,
        profilePicture = profilePicture,
    )

fun DoctorEntity.toDoctor(context: Context): Doctor =
    Doctor(
        id = id,
        firstName = firstName,
        lastName = lastName,
        name = "$firstName $lastName",
        clinicId = clinicId,
        email = email,
        phone = phone,
        specialty = specialty,
        fromTime = fromTime,
        toTime = toTime,
        availability = "${fromTime.getFormattedTime(context)} - ${toTime.getFormattedTime(context)}",
        rating = rating,
        price = price,
        profilePicture = profilePicture,
    )

fun Doctor.toDoctorDto(): DoctorDto =
    DoctorDto(
        id = id,
        firstName = firstName,
        lastName = lastName,
        clinicId = clinicId,
        email = email,
        phone = phone,
        specialty = specialty,
        fromTime = fromTime,
        toTime = toTime,
        rating = rating,
        price = price,
        profilePicture = profilePicture,
    )
