package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.UserDto
import eg.edu.cu.csds.icare.core.data.local.db.entity.UserEntity
import eg.edu.cu.csds.icare.core.domain.model.User

fun UserDto.toUserEntity(): UserEntity =
    UserEntity(
        userId = userId,
        roleId = this.roleId,
        nationalId = this.nationalId,
        birthDate = this.birthDate,
        gender = this.gender,
        address = this.address,
        phoneNumber = this.phoneNumber,
        isActive = this.isActive,
        displayName = this.displayName,
        email = this.email,
        photoUrl = this.photoUrl,
        isEmailVerified = this.isEmailVerified,
        linkedWithGoogle = this.linkedWithGoogle,
    )

fun UserEntity.toUser(): User =
    User(
        userId = userId,
        roleId = this.roleId,
        nationalId = this.nationalId,
        birthDate = this.birthDate,
        gender = this.gender,
        address = this.address,
        phoneNumber = this.phoneNumber,
        isActive = this.isActive,
        displayName = this.displayName,
        email = this.email,
        photoUrl = this.photoUrl,
        isEmailVerified = this.isEmailVerified,
        linkedWithGoogle = this.linkedWithGoogle,
    )

fun User.toUserDto(): UserDto =
    UserDto(
        userId = this.userId,
        roleId = this.roleId,
        nationalId = this.nationalId,
        birthDate = this.birthDate,
        gender = this.gender,
        address = this.address,
        phoneNumber = this.phoneNumber,
        isActive = this.isActive,
        displayName = this.displayName,
        email = this.email,
        photoUrl = this.photoUrl,
        isEmailVerified = this.isEmailVerified,
        linkedWithGoogle = this.linkedWithGoogle,
    )

fun UserDto.toUser(): User =
    User(
        userId = this.userId,
        roleId = this.roleId,
        nationalId = this.nationalId,
        birthDate = this.birthDate,
        gender = this.gender,
        address = this.address,
        phoneNumber = this.phoneNumber,
        isActive = this.isActive,
        displayName = this.displayName,
        email = this.email,
        photoUrl = this.photoUrl,
        isEmailVerified = this.isEmailVerified,
        linkedWithGoogle = this.linkedWithGoogle,
    )
