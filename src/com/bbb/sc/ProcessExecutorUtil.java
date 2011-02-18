package com.bbb.sc;

import static com.google.common.base.Preconditions.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

/**
 * Utility class for running external processes
 * @author bbalazs
 *
 */
class ProcessExecutorUtil {
    
    public List<String> executeCommand(String...command) throws ExecuteException, IOException {
        List<String> cmdList = new ArrayList<String>();
        Collections.addAll(cmdList, command);
        return executeCommand(cmdList);
    }

    
    /**
     * 
     * @param command
     * @return
     * @throws ExecuteException
     * @throws IOException
     */
    public List<String> executeCommand(List<String> command) throws ExecuteException, IOException {
        checkNotNull(command);
        checkArgument(!command.isEmpty());
        
        CommandLine cmdLine = parseCommandLine(command);
        
        BufferedReader reader = executeAndReturnOutput(cmdLine);
                
        List<String> result = new ArrayList<String>();
        while (reader.ready()) {
            result.add(reader.readLine());
        }
        
        return result;
    }
    
    private CommandLine parseCommandLine(List<String> command) {
        checkNotNull(command);
        checkArgument(!command.isEmpty());

        CommandLine cmdLine = new CommandLine(command.get(0));
        
        for (String argument : command.subList(1, command.size())) {
            cmdLine.addArgument(argument);
        }
        
        return cmdLine;
    }
    
    private BufferedReader executeAndReturnOutput(CommandLine cmdLine) throws ExecuteException, IOException {
        checkNotNull(cmdLine);
        
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();

        executor.setStreamHandler(new PumpStreamHandler(stdout));
        executor.execute(cmdLine);
        
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(stdout.toByteArray())));
        
        return reader;
    }

}
