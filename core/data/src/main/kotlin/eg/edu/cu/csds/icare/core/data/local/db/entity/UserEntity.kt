package eg.edu.cu.csds.icare.core.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val gender: String,
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
