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
import org.openjdk.jcstress.infra.results.IntResult3;


/**
 * r1 == r2 == r3 = 0 is possible
 * An example of the execution order that results in this outcome:
 * T2(1), T3(1), T4(1), ...remaining statements in any order
 */
@JCStressTest
@Description("Homework2")
@Outcome(id = { "[0, 0, 0]", "[0, 1, 0]", "[0, 1, 1]", "[1, 0, 0]" }, expect = ACCEPTABLE)
@Outcome(id = { "[0, 0, 1]", "[1, 0, 1]", "[1, 1, 0]", "[1, 1, 1]" }, expect = FORBIDDEN)
@State
public class Homework2 {

    int x;
    int y;
    int z;
    
    volatile int v1;
    volatile int v2;
    
    @Actor
    public void actor1(IntResult3 r) {
      z = 1;
    }

    @Actor
    public void actor2(IntResult3 r) {
      r.r1 = z;
      if (r.r1 == 0)
        x = 1;
    }
    
    @Actor
    public void actor3(IntResult3 r) {
      r.r2 = x;
      y = r.r2;
    }
    
    @Actor
    public void actor4(IntResult3 r) {
      r.r3 = y;
      x = r.r3;
    }

}
