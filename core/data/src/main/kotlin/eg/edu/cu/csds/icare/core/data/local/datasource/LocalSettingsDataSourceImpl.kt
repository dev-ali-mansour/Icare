package eg.edu.cu.csds.icare.core.data.local.datasource

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.data.local.db.dao.SettingsDao
import eg.edu.cu.csds.icare.core.data.local.db.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class LocalSettingsDataSourceImpl(
    private val settingsDao: SettingsDao,
) : LocalSettingsDataSource {
    override suspend fun saveOnBoardingState(completed: Boolean) {
        settingsDao.updateSettings(SettingsEntity(id = 1, onBoardingCompleted = completed))
    }

    override fun getOnBoardingState(): Flow<Resource<Boolean>> =
        flow {
            emit(Resource.Loading())
            settingsDao.getSettings().collect { settingsEntity ->
                settingsEntity?.let {
                    emit(Resource.Success(it.onBoardingCompleted))
                } ?: run {
                    emit(Resource.Success(false))
                }
            }
        }
}
