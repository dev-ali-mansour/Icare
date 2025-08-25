package eg.edu.cu.csds.icare.core.data.local.datasource

import eg.edu.cu.csds.icare.core.data.local.db.dao.SettingsDao
import eg.edu.cu.csds.icare.core.data.local.db.entity.SettingsEntity
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class LocalSettingsDataSourceImpl(
    private val settingsDao: SettingsDao,
) : LocalSettingsDataSource {
    override fun finishOnBoarding(): Flow<Result<Unit, DataError.Local>> =
        flow {
            runCatching {
                settingsDao.updateSettings(SettingsEntity(id = 1, onBoardingCompleted = true))
                emit(Result.Success(Unit))
            }.onFailure {
                Timber.e("Error saving on-boarding state: ${it.message}")
                emit(Result.Error(DataError.Local.UNKNOWN))
            }
        }

    override fun getOnBoardingState(): Flow<Result<Boolean, DataError.Local>> =
        flow {
            runCatching {
                settingsDao.getSettings().collect { settingsEntity ->
                    settingsEntity?.let {
                        emit(Result.Success(it.onBoardingCompleted))
                    } ?: run {
                        emit(Result.Success(false))
                    }
                }
            }.onFailure {
                emit(Result.Error(DataError.Local.UNKNOWN))
            }
        }
}
