package eg.edu.cu.csds.icare.core.data.local.datasource

import eg.edu.cu.csds.icare.core.data.local.db.dao.SettingsDao
import eg.edu.cu.csds.icare.core.data.local.db.entity.SettingsEntity
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class LocalSettingsDataSourceImpl(
    private val settingsDao: SettingsDao,
) : LocalSettingsDataSource {
    override fun finishOnBoarding(): Flow<RequestState<Unit, DataError.Local>> =
        flow {
            runCatching {
                settingsDao.updateSettings(SettingsEntity(id = 1, onBoardingCompleted = true))
                emit(RequestState.Success(Unit))
            }.onFailure {
                Timber.e("Error saving on-boarding state: ${it.message}")
                emit(RequestState.Error(DataError.Local.UNKNOWN))
            }
        }

    override fun getOnBoardingState(): Flow<RequestState<Boolean, DataError.Local>> =
        flow {
            runCatching {
                settingsDao.getSettings().collect { settingsEntity ->
                    settingsEntity?.let {
                        emit(RequestState.Success(it.onBoardingCompleted))
                    } ?: run {
                        emit(RequestState.Success(false))
                    }
                }
            }.onFailure {
                emit(RequestState.Error(DataError.Local.UNKNOWN))
            }
        }
}
