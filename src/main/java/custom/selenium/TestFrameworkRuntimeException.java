package custom.selenium;

/**
 * This class is going to used any time the Framework work needs to be interrupted by serious error.
 * Created to override some Exceptions and convert it to <code>RuntimeExceptions</code>
 * because I hate when method has "throws any-blah-blah-blah-Exception".
 *
 * @author <a href="mailto:vyarosh@speroteck.com">Vadym Yarosh</a>
 */
public class TestFrameworkRuntimeException extends RuntimeException {

    /**
     * Create a new <code>TestFrameworkRuntimeException</code> with no
     * detail mesage.
     */
    public TestFrameworkRuntimeException() {
        super();
    }

    /**
     * Create a new <code>TestFrameworkRuntimeException</code> with
     * the <code>String</code> specified as an error message.
     *
     * @param message  The error message for the exception.
     */
    public TestFrameworkRuntimeException(String message) {
        super(message);
    }

    /**
     * Create a new <code>TestFrameworkRuntimeException</code> with the
     * given <code>Exception</code> base cause and detail message.
     *
     * @param  message  The detail message.
     * @param  cause  The exception to be encapsulated in a
     *                TestFrameworkRuntimeException
     */
    public TestFrameworkRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * Create a new <code>TestFrameworkRuntimeException</code> with a
     * given <code>Exception</code> base cause of the error.
     *
     * @param  cause  The exception to be encapsulated in a
     *                TestFrameworkRuntimeException.
     */
    public TestFrameworkRuntimeException(Throwable cause) {
        super(cause);
    }
}
