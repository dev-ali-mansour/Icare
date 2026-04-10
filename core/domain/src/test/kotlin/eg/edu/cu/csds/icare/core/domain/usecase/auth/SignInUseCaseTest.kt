import app.cash.turbine.test
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

@Suppress("UnusedFlow")
class SignInUseCaseTest {
    private lateinit var mockAuthRepository: AuthRepository
    private lateinit var signInUseCase: SignInUseCase

    @Before
    fun setUp() {
        mockAuthRepository = mockk()
        signInUseCase = SignInUseCase(mockAuthRepository)
    }

    @Test
    fun `invoke WHEN repository returns success THEN should emit Success`() =
        runTest {
            // Arrange
            val testEmail = "test@example.com"
            val testPassword = "password123"
            val expectedRequestState = RequestState.Success(Unit)

            coEvery {
                mockAuthRepository.signInWithEmailAndPassword(testEmail, testPassword)
            } returns flowOf(expectedRequestState)

            // Act & Assert
            signInUseCase(testEmail, testPassword).test {
                assertEquals(expectedRequestState, awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 1) {
                mockAuthRepository.signInWithEmailAndPassword(testEmail, testPassword)
            }
        }

    @Test
    fun `invoke WHEN repository returns invalid credentials error THEN should emit Error`() =
        runTest {
            // Arrange
            val testEmail = "test@example.com"
            val testPassword = "wrongpassword"
            val expectedError = DataError.Remote.INVALID_CREDENTIALS
            val expectedRequestState = RequestState.Error(expectedError)

            coEvery {
                mockAuthRepository.signInWithEmailAndPassword(testEmail, testPassword)
            } returns flowOf(expectedRequestState)

            // Act & Assert
            signInUseCase(testEmail, testPassword).test {
                assertEquals(expectedRequestState, awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 1) {
                mockAuthRepository.signInWithEmailAndPassword(testEmail, testPassword)
            }
        }

    @Test
    fun `invoke WHEN repository returns user not authorized error THEN should emit Error`() =
        runTest {
            // Arrange
            val testEmail = "nonexistent@example.com"
            val testPassword = "password123"
            val expectedError = DataError.Remote.USER_NOT_AUTHORIZED
            val expectedRequestState = RequestState.Error(expectedError)

            coEvery {
                mockAuthRepository.signInWithEmailAndPassword(testEmail, testPassword)
            } returns flowOf(expectedRequestState)

            // Act & Assert
            signInUseCase(testEmail, testPassword).test {
                assertEquals(expectedRequestState, awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 1) {
                mockAuthRepository.signInWithEmailAndPassword(testEmail, testPassword)
            }
        }

    @Test
    fun `invoke WHEN repository flow emits no internet error THEN should emit Error`() =
        runTest {
            // Arrange
            val testEmail = "test@example.com"
            val testPassword = "password123"
            val expectedError = DataError.Remote.NO_INTERNET
            val expectedRequestState = RequestState.Error(expectedError)

            coEvery {
                mockAuthRepository.signInWithEmailAndPassword(testEmail, testPassword)
            } returns flowOf(expectedRequestState)

            // Act & Assert
            signInUseCase(testEmail, testPassword).test {
                assertEquals(expectedRequestState, awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 1) {
                mockAuthRepository.signInWithEmailAndPassword(testEmail, testPassword)
            }
        }

    @Test
    fun `invoke WHEN repository flow throws IOException THEN flow should throw IOException`() =
        runTest {
            // Arrange
            val testEmail = "test@example.com"
            val testPassword = "password123"
            val expectedException = IOException("Simulated network problem")

            coEvery {
                mockAuthRepository.signInWithEmailAndPassword(testEmail, testPassword)
            } returns flow { throw expectedException }

            // Act & Assert
            signInUseCase(testEmail, testPassword).test {
                val actualException = awaitError()
                assertEquals(expectedException.javaClass, actualException.javaClass)
                assertEquals(expectedException.message, actualException.message)
            }

            coVerify(exactly = 1) {
                mockAuthRepository.signInWithEmailAndPassword(testEmail, testPassword)
            }
        }

    @Test
    fun `invoke WHEN repository flow throws and maps to UNKNOWN error THEN should emit Error`() =
        runTest {
            // Arrange
            val testEmail = "exception@example.com"
            val testPassword = "password"
            val expectedRequestState = RequestState.Error(DataError.Remote.UNKNOWN)

            coEvery {
                mockAuthRepository.signInWithEmailAndPassword(testEmail, testPassword)
            } returns flowOf(expectedRequestState)

            // Act & Assert
            signInUseCase(testEmail, testPassword).test {
                assertEquals(expectedRequestState, awaitItem())
                awaitComplete()
            }

            coVerify(exactly = 1) {
                mockAuthRepository.signInWithEmailAndPassword(testEmail, testPassword)
            }
        }
}
