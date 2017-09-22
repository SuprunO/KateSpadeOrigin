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

import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Class contains custom test Rule implementation to create
 * single {@link TestWatcher} Rule based on parameters in command line.
 *  <code>this</code> have to be passed for the
 * {@link SauceOnDemandTestWatcher} correct construction.
 *
 * @author <a href="mailto:vyarosh@speroteck.com">Vadym Yarosh</a>
 */
public class TestWatcherMaker implements TestRule {
    private SauceOnDemandSessionIdProvider session;

    public TestWatcherMaker(SauceOnDemandSessionIdProvider session) {
        this.session = session;
    }

    /**
     * Not modified just implemented.
     */
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
            }
        };
    }


    /**
     * {@link TestWatcher} Rule that handles test passed/failed actions.
     *
     * @return  {@link TestWatcher} Instance
     */
    private TestWatcher getMyTestWatcher() {
        return new TestWatcher() {
           @Override
            protected void failed(Throwable e, Description d) {
                TestFactory.failActions(e);
            }

            @Override
            protected void succeeded(Description d) {
                TestFactory.successActions();
            }
        };
    }

    /**
     * JUnit Rule({@link TestWatcher} subclass) which will mark
     * the Sauce Job as passed/failed when the test succeeds or fails.
     * And overriding to keep local framework functional.
     *
     * @return  {@link SauceOnDemandTestWatcher} Instance
     */
    private SauceOnDemandTestWatcher getSauceLabsWatcher() {
         return new SauceOnDemandTestWatcher(session, TestFactory.authentication){
             @Override
             protected void failed(Throwable e, Description d) {
                 super.failed(e, d);
                 TestFactory.failActions(e);
             }

             @Override
             protected void succeeded(Description d) {
                 super.succeeded(d);
                 TestFactory.successActions();
             }
         };
    }

    /**
     * Creates and returns {@link TestWatcher} Rule or SauceLabs Rule Instance
     * that handles SauceLabs Jobs as passed/failed. And carries on current
     * passed/failed actions.
     * Created only if {@link TestFactory#sauceLabsSession} was set.
     *
     * @return {@link SauceOnDemandTestWatcher} | {@link TestWatcher} Instance
     */
    public TestWatcher getWatcher() {
        if (TestFactory.sauceLabsSession != null) {
            return getSauceLabsWatcher();
        } else {
            return getMyTestWatcher();
        }
    }
}
