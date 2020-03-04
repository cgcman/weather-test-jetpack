package com.example.test

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.test.viewmodels.WeatherViewModel
import com.example.test.viewmodels.WelcomeViewModel
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    val application = mock(Application::class.java)
    val wvm: WelcomeViewModel = mock(WelcomeViewModel(application)::class.java)
    val weavm: WeatherViewModel = mock(WeatherViewModel::class.java)

    @Before
    fun setupUserEmpty(){
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `check user is not empty`() {
        `when`(wvm.checkUserExist()).thenReturn("Marco")
        Assert.assertEquals("Marco", wvm.checkUserExist())
    }

    @Test
    fun `if user empty the button not show`() {
        `when`(wvm.getButtonIsShow()).thenReturn(false)
        Assert.assertEquals(false, wvm.getButtonIsShow())
    }

    @Test
    fun `check conectivity`() {
        weavm.refreshData()
        `when`(weavm.chekConnectivity(true)).thenReturn(true)
        verify(weavm, times(1)).refreshData()
    }

}