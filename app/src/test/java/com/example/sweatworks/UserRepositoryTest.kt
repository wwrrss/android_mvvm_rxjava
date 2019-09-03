package com.example.sweatworks

import com.example.sweatworks.data.api.UsersApi
import com.example.sweatworks.data.repositories.UserRepository
import com.example.sweatworks.data.repositories.UserRepositoryImp
import com.example.sweatworks.models.Result
import com.example.sweatworks.models.User
import com.example.sweatworks.utils.Globals
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    lateinit var userRepository: UserRepository

    @Before
    fun setup(){
        val mockedUserApi = MockedUserApi()
        userRepository = UserRepositoryImp(mockedUserApi)
    }

    @Test
    fun shouldReturnMockedResults(){
        userRepository.fetchUsersByPage(1,1,Globals.API_SEED)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                it.results.isNotEmpty()
            }
    }
}

class MockedUserApi: UsersApi {

    override fun getUsersByPage(page: Int, results: Int, seed: String): Observable<Result> {
        val user = User("test@test.com")
        val result = Result(arrayOf(user))
        return Observable.just(result)
    }
}