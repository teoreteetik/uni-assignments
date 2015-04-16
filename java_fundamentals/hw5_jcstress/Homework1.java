/*
 * Copyright (c) 2014, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.openjdk.jcstress.tests.jfhw5;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Description;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IntResult4;

/**
 * r1 == r3 == 1, r2 == r4 == 0 is not possible
 * Since the statements in T3 and T4 are reads of volatile variables, we know that they cannot be reordered and are executed in the order presented.
 * If r1 == r3 == 1, then either r2 or r4 can be 0, but not both.
 * For r4 == 0 and r3 == 1, T3(1) must execute after T4(2), but then T4(1) has also executed and if r3 == 1 then T3(2) cannot evaluate to 0. 
 */

@JCStressTest
@Description("Homework1")
@Outcome(id = { "[1, 0, 1, 0]" }, expect = FORBIDDEN)
@Outcome(id = { "[0, 0, 0, 0]", "[0, 0, 1, 1]", "[1, 1, 0, 0]", "[1, 1, 1, 1]",
                "[1, 0, 0, 1]", "[0, 1, 1, 0]", "[0, 0, 0, 1]", "[1, 1, 0, 1]", 
                "[0, 1, 0, 0]", "[0, 1, 1, 1]", "[0, 1, 0, 1]", "[0, 0, 1, 0]",
                "[1, 1, 1, 0]", "[1, 0, 0, 0]", "[1, 0, 1, 1]" }, 
         expect = ACCEPTABLE)
@State
public class Homework1 {

    int x;
    int y;
    int z;
    
    volatile int v1;
    volatile int v2;
    
    @Actor
    public void actor1(IntResult4 r) {
      v1 = 1;
    }

    @Actor
    public void actor2(IntResult4 r) {
      v2 = 1;
    }
    
    @Actor
    public void actor3(IntResult4 r) {
      r.r1 = v1;
      r.r2 = v2;
    }
    
    @Actor
    public void actor4(IntResult4 r) {
      r.r3 = v2;
      r.r4 = v1;
    }

}
