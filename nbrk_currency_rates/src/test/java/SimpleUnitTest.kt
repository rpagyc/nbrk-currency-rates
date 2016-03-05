import com.nbrk.rates.extensions.toDateLong
import org.junit.Test

/**
 * Created by rpagyc on 02-Mar-16.
 */
class SimpleUnitTest {
  @Test
  fun simpleTest() {
    assert("02.03.2016".toDateLong() is Long)
  }
}
