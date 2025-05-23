//
//  LocalShell.java
//  
//
//  Created by Brandon Cobb on 5/22/25.
//
package com.brandongcobb.vegan.store.ai.tools;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LocalShell {
    
    public static String executeShellCommand(String command) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("sh", "-c", command); // Use "cmd.exe /c" on Windows

            Process process = builder.start();
            String output = new String(process.getInputStream().readAllBytes());
            String error  = new String(process.getErrorStream().readAllBytes());

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return output;
            } else {
                return "❌ Error:\n" + error;
            }

        } catch (Exception e) {
            return "⚠️ Shell execution failed: " + e.getMessage();
        }
    }
}
