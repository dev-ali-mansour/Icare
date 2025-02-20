package eg.edu.cu.csds.icare.data.local.datasource

import eg.edu.cu.csds.icare.data.local.db.entity.PermissionEntity
import eg.edu.cu.csds.icare.data.local.db.entity.UserEntity

interface LocalAuthDataSource {
    suspend fun getUser(): UserEntity?

    suspend fun getPermissions(): List<PermissionEntity>

    suspend fun saveEmployee(
        entity: UserEntity,
        permissions: List<PermissionEntity>,
    )

    suspend fun clearEmployee()
}
