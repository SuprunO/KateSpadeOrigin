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

import org.openqa.selenium.By;

/**
 * Class is used to create Field-sets to fill in inputs and select dropdowns using single function.
 * Field set is supposed to be ArrayList, each element consists of String type("input" or "dropdown"),
 * By locator(locator to find current filed or dropdown) and String value(text or value to select or fill in)
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class Field {
    public static final String INPUT = "input";
    public static final String SELECT = "dropdown";

    private By locator;
    private String value;
    private String type;

    public Field(String t, By l, String v) {
        type = t;
        locator = l;
        value = v;
    }

    /**
     * @return the locator of the current input field
     */
    public By getLocator() {
        return locator;
    }

    /**
     * @return the Type of the current input field: input|dropdown
     */
    public String getType() {
        return type;
    }

    /**
     * @return the value that should be entered into the current input field
     */
    public String getValue() {
        return value;
    }
}
