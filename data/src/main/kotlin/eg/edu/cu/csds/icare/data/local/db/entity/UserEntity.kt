package eg.edu.cu.csds.icare.data.local.db.entity

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
    val nationalId: String,
    val birthDate: Long,
    val gender: Char,
    val address: String,
    val phoneNumber: String,
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
    )
