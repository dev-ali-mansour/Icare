package eg.edu.cu.csds.icare.data.local.datasource

import eg.edu.cu.csds.icare.data.local.db.dao.UserDao
import eg.edu.cu.csds.icare.data.local.db.entity.PermissionEntity
import eg.edu.cu.csds.icare.data.local.db.entity.UserEntity
import org.koin.core.annotation.Single

@Single
class LocalAuthDataSourceImpl(
    private val userDao: UserDao,
) : LocalAuthDataSource {
    override suspend fun getUser(): UserEntity? = userDao.getEmployee()

    override suspend fun getPermissions(): List<PermissionEntity> = userDao.getPermissions()

    override suspend fun saveEmployee(
        entity: UserEntity,
        permissions: List<PermissionEntity>,
    ) {
        userDao.getEmployee()?.let {
            userDao.updateEmployee(entity)
        } ?: run {
            userDao.persistUser(entity)
        }
        userDao.clearPermissions()
        userDao.persistPermissions(permissions)
    }

    override suspend fun clearEmployee() {
        userDao.clearEmployee()
        userDao.clearPermissions()
    }
}
