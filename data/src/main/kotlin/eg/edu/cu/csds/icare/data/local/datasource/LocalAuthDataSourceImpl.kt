package eg.edu.cu.csds.icare.data.local.datasource

import eg.edu.cu.csds.icare.data.local.db.dao.UserDao
import eg.edu.cu.csds.icare.data.local.db.entity.UserEntity
import org.koin.core.annotation.Single

@Single
class LocalAuthDataSourceImpl(
    private val userDao: UserDao,
) : LocalAuthDataSource {
    override suspend fun getUser(): UserEntity? = userDao.getEmployee()

    override suspend fun saveEmployee(entity: UserEntity) {
        userDao.getEmployee()?.let {
            userDao.updateEmployee(entity)
        } ?: run {
            userDao.persistUser(entity)
        }
    }

    override suspend fun clearEmployee() {
        userDao.clearEmployee()
    }
}
