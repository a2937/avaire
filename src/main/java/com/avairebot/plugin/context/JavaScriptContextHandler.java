package com.avairebot.plugin.context;

import com.avairebot.AvaIre;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * A class designed to contain the process handler for all
 * JavaScript plugins.
 */
public class JavaScriptContextHandler
{

    /**
     * Instantiates a new JavaScript context handler.
     */
    public JavaScriptContextHandler()
    {
        initialize();
    }

    private Context jsContext;

    /**
     * Initialize.
     */
    public void initialize()
    {
        jsContext = Context.newBuilder("js").allowAllAccess(true).build();
        jsContext.getBindings("js").putMember("avaire", AvaIre.getInstance());
    }


    /**
     * Executes the JavaScript file.
     * Returns true if it runs properly.
     *
     * @param fileName the file name
     * @return a value determining if the operation was a success.
     */
    public boolean execute(String fileName)
    {
       String script = readFile(fileName);
       if(script.isEmpty())
       {
           return false;
       }
       runScript(script);
       return true;
    }

    private String readFile(String fileName)
    {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null)
            {

                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return "";
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "";
        }
    }

    private  Value runScript(String script)
    {
        return jsContext.eval("js", script);
    }

}
