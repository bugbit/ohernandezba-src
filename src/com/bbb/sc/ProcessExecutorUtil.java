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
 * Uses Apache Exec
 */
class ProcessExecutorUtil {
    
    /**
     * Running an external command getting the system output from the process.
     * E.g.: List<String> ouput = executeCommand("cmd", "/c", "dir");
     * Best practice is not to use spaces between the arguments, instead give each argument separately.
     * @param command - the command you want to run
     * @return - a list of Strings with the output
     * @throws ExecuteException - if the exit value wasn't 0 
     * @throws IOException - if the command couldn't be found
     */
    public List<String> executeCommand(String... command) throws ExecuteException, IOException {
        List<String> cmdList = new ArrayList<String>();
        Collections.addAll(cmdList, command);
        return executeCommand(cmdList);
    }

    
    /**
     * Running an external command getting the system output from the process.
     * Best practice is not to use spaces between the arguments, instead give each argument separately.
     * @param command - the command you want to run
     * @return - a list of Strings with the output
     * @throws ExecuteException - if the exit value wasn't 0 
     * @throws IOException - if the command couldn't be found
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
