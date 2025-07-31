package eg.edu.cu.csds.icare.core.data.local.datasource

import eg.edu.cu.csds.icare.core.data.local.db.entity.UserEntity

interface LocalAuthDataSource {
    suspend fun getUser(): UserEntity?

    suspend fun saveEmployee(entity: UserEntity)

    suspend fun clearEmployee()
}
