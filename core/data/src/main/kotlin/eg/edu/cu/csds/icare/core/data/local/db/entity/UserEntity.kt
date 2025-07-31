package eg.edu.cu.csds.icare.core.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import eg.edu.cu.csds.icare.core.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(defaultValue = "")
    val userId: String,
    val roleId: Short,
    @ColumnInfo(defaultValue = "")
    val displayName: String = "",
    val nationalId: String,
    val birthDate: Long,
    val gender: Char,
    val address: String,
    val phoneNumber: String,
    @ColumnInfo(defaultValue = "")
    val email: String = "",
    @ColumnInfo(defaultValue = "")
    val photoUrl: String = "",
    @ColumnInfo(defaultValue = "0")
    val isEmailVerified: Boolean = false,
    @ColumnInfo(defaultValue = "0")
    val linkedWithGoogle: Boolean = false,
    @ColumnInfo(defaultValue = "0")
    val isActive: Boolean = true,
)

fun UserEntity.toModel(): User =
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

fun User.toEntity(): UserEntity =
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
