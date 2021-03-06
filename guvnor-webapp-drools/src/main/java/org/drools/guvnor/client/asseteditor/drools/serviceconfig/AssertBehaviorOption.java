/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.guvnor.client.asseteditor.drools.serviceconfig;

public enum AssertBehaviorOption {
    IDENTITY("Identity"), EQUALITY("Equality");

    private final String displayValue;

    AssertBehaviorOption(final String displayValue) {
        this.displayValue = displayValue;
    }

    public String toDisplay() {
        return displayValue;
    }

    public String toXML() {
        return toString();
    }

}
