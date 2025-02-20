package eg.edu.cu.csds.icare.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import eg.edu.cu.csds.icare.core.domain.model.User

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val roleId: Short,
    val job: String,
    val nationalId: String,
    val dateOfBirth: Long,
    val gender: Char,
    val address: String,
    val phoneNumber: String,
)

fun UserEntity.toModel(): User =
    User(
        roleId = this.roleId,
        job = this.job,
        nationalId = this.nationalId,
        dateOfBirth = this.dateOfBirth,
        gender = this.gender,
        address = this.address,
        phoneNumber = this.phoneNumber,
    )

fun User.toEntity(): UserEntity =
    UserEntity(
        roleId = this.roleId,
        job = this.job,
        nationalId = this.nationalId,
        dateOfBirth = this.dateOfBirth,
        gender = this.gender,
        address = this.address,
        phoneNumber = this.phoneNumber,
    )
