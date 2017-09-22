/*
 * Copyright (c) 2015, Speroteck Inc. (www.speroteck.com)
 * and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Speroteck or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package lcg.selenium;

/**
 * This class is used any time the Framework work needs to be interrupted
 * by serious error. Created to override some Exceptions and convert it to
 * <code>RuntimeExceptions</code> because I hate when method has throws, ex.:
 * <code>public void doSomething() throws EnyBlahBlahBlahException</code>
 *
 * @author <a href="mailto:vyarosh@speroteck.com">Vadym Yarosh</a>
 */
public class TestFrameworkRuntimeException extends RuntimeException {

    /**
     * Create a new <code>TestFrameworkRuntimeException</code> with no
     * detail message.
     */
    public TestFrameworkRuntimeException() {
        super();
    }

    /**
     * Create a new <code>TestFrameworkRuntimeException</code> with
     * the <code>String</code> specified as an error message.
     *
     * @param   message
     *          The error message for the exception.
     */
    public TestFrameworkRuntimeException(String message) {
        super(message);
    }

    /**
     * Create a new <code>TestFrameworkRuntimeException</code> with the
     * given <code>Exception</code> base cause and detail message.
     *
     * @param   message
     *          The detail message.
     * @param   cause
     *          The exception to be encapsulated in a
     *          TestFrameworkRuntimeException
     */
    public TestFrameworkRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * Create a new <code>TestFrameworkRuntimeException</code> with a
     * given <code>Exception</code> base cause of the error.
     *
     * @param   cause
     *          The exception to be encapsulated in a
     *          TestFrameworkRuntimeException.
     */
    public TestFrameworkRuntimeException(Throwable cause) {
        super(cause);
    }
}
