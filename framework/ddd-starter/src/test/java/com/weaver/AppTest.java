package com.weaver;

import org.junit.Test;

import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
public class AppTest
{

    @Test
    public void testSackWalk(){
        test1();
    }

    private void test1() {
        StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        String stackTrace = stackWalker.walk(s -> s.filter(stackFrame -> stackFrame.getClassName().contains("com.weaver."))
                .map(StackWalker.StackFrame::toString)
                .collect(Collectors.joining("\n")));
        System.out.println("Caller class:\n " + stackTrace);
    }
}
