import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.finalobligatoriskkotlin.MainActivity
import com.example.finalobligatoriskkotlin.R
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.example.finalobligatoriskkotlin.SecondFragment


@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun loginUserWithSpecificDetails() {
        // Type email and password into the respective fields
        onView(withId(R.id.usernameEditText)).perform(typeText("oliverskolepeb@gmail.com"))
        onView(withId(R.id.passwordEditText)).perform(typeText("Frost3310"))

        // Click on the login button
        onView(withId(R.id.loginButton)).perform(click())

        try {
            Thread.sleep(4000)  // Sleep for 4 seconds
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        //Assert
        onView(withId(R.id.actionsSpinner)).check(matches(isDisplayed()))
    }
}
