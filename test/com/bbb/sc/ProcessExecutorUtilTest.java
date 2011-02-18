package com.bbb.sc;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.ExecuteException;
import org.junit.Before;
import org.junit.Test;

/*
 * Functional tests for ProcessExecutor
 * The tests relay on the following Windows system commands: dir, echo
 */
public class ProcessExecutorUtilTest {

    ProcessExecutorUtil executor;
    
    @Before
    public void setUp() {
        executor = new ProcessExecutorUtil();
    }
    
    @Test
    public void executeShouldReturnCorrectOutput() throws Exception {
        List<String> result = executor.executeCommand("cmd", "/c", "echo", "TEST");
        
        assertTrue(result.size() == 1);
        assertTrue(result.get(0).equals("TEST"));
    }

    @Test
    public void executeDirShouldReturnMultipleLines() throws Exception {
        List<String> result = executor.executeCommand("cmd", "/c", "dir");
        
        assertTrue(result.size() > 1);
    }
    
    @Test(expected=ExecuteException.class)
    public void executeShouldThrowExecuteExceptionIfCommandUnsuccessful() throws Exception {
        executor.executeCommand("cmd", "/c", "dir", "/NOT_EXISTING_PARAM");
    }

    @Test(expected=IOException.class)
    public void executeShouldThrowIOExceptionIfNoCommandExists() throws Exception {
        executor.executeCommand("NONEXISTING_COMMAND.com");
    }

    @Test(expected=IllegalArgumentException.class)
    public void executeShouldNotAcceptNullAsCommand() throws Exception {
        executor.executeCommand();
    }

    @Test(expected=IllegalArgumentException.class)
    public void executeShouldNotAcceptEmptyList() throws Exception {
        executor.executeCommand(new ArrayList<String>());
    }

    @Test(expected=IllegalArgumentException.class)
    public void executeShouldNotAcceptEmptyList2() throws Exception {
        executor.executeCommand("");
    }
}

