// This file is part of the 'texasholdem' project, an open source
// Texas Hold'em poker application written in Java.
//
// Copyright 2009 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.yanchuanli.games.pokr.model;

import com.yanchuanli.games.pokr.util.Config;

/**
 * A Texas Hold'em poker action.
 *
 * @author Oscar Stigter
 */
public enum Action {

    /**
     * Posting the small blind.
     */
    SMALL_BLIND("Small Blind", "sb", Config.ACTION_TYPE_SMALL_BLIND),

    /**
     * Posting the big blind.
     */
    BIG_BLIND("Big Blind", "bb", Config.ACTION_TYPE_BIG_BLIND),


    /**
     * Checking.
     */
    ALLIN("Allin", "a", Config.ACTION_TYPE_ALL_IN),

    /**
     * Checking.
     */
    CHECK("Check", "c", Config.ACTION_TYPE_CHECK),

    /**
     * Calling a bet.
     */
    CALL("Call", "ca", Config.ACTION_TYPE_CALL),

    /**
     * Place an initial bet.
     */
    BET("Bet", "b", Config.ACTION_TYPE_BET),

    /**
     * Raising the current bet.
     */
    RAISE("Raise", "r", Config.ACTION_TYPE_RAISE),

    /**
     * Folding.
     */
    FOLD("Fold", "f", Config.ACTION_TYPE_FOLD),

    CONTINUE("Fold", "con", Config.ACTION_TYPE_FOLD),;


    /**
     * The name.
     */
    private final String name;

    /**
     * The verb.
     */
    private final String verb;

    private final int verbType;

    /**
     * Constructor.
     *
     * @param name The name.
     */
    Action(String name, String verb, int verbType) {
        this.name = name;
        this.verb = verb;
        this.verbType = verbType;
    }

    /**
     * Returns the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the verb form of this action.
     *
     * @return The verb.
     */
    public String getVerb() {
        return verb;
    }

    public int getVerbType() {
        return verbType;
    }

    /*
    * (non-Javadoc)
    * @see java.lang.Enum#toString()
    */
    @Override
    public String toString() {
        return name;
    }

}
