import javax.naming.*;
import java.util.*;
import java.io.*;
import weblogic.common.*;
import weblogic.jndi.*;

/**
 * This example shows how to create a context with default (guest access) to the naming
 * system, and how to create a context that has authenticated access to the
 * naming system.
 * <p>The following sections cover what to do:
 * <p>
 * <ol>
 * <li><a href="#Build the Example">Build the Example</a>
 * <li><a href="#Run the Example">Run the Example</a>
 * </ol>
 * <h3><a name="Build the Example">Build the Example</a></h3>
 * <p>
 * <ol>
 * <li>Set up your development shell, as described in
 * <a href=../examples.html#environment>Setting up
 * your environment</a>.
 * <p>
 * <li>
 * Build the example by executing the <a href=../examples.html#buildScripts>
 * build script</a> provided for this example.
 * <p>
 * The <a href="build.xml">build.xml</a> script is available in the
 * <font face="Courier New" size = -1><i>SAMPLES_HOME</i>\server\examples\src\examples\jndi</font>
 * directory, where <font face="Courier New" size = -1><i>SAMPLES_HOME</font></i> refers
 * to the directory for all samples and examples for the WebLogic Platform.
 * <p>
 * To build the example, move to the JNDI example directory and run the script using the
 * following Java ant command:
 * <pre><font face="Courier New">prompt&gt;<b> ant </b></font></pre>
 * </ol>
 * <p>
 * <h3><a name="Run the Example"></a>Run the Example</h3>
 * <ol>
 * <li>
 * <a href=../examples.html#startServer>Start the WebLogic Server</a> in a new command shell.
 * <p><li>
 * In your development shell, run the client with the following command:
 * <pre><font face="Courier New">prompt&gt;<b> ant run.initialcontext </b></font></pre>
 * Parameters contained within the run script are:
 * <dl>
 * <dt><i><font face="Courier New" size = -1><b>hostname</b></font></i>
 * <dd>Host name of the WebLogic Server.
 * <dt><i><font face="Courier New" size = -1><b>port</b></font></i>
 * <dd>Port where the WebLogic Server is listening for connections.
 * </dl>
 * </ol>
 * @author Copyright (c) 1999-2005 by BEA Systems, Inc. All Rights Reserved.
 */
public class InitialContextExample {
  /**
   * Runs this example from the command line.
   */
  public static void main(String[] args) {
    if (args.length > 3) {
      System.out.println("Usage:  java examples.jndiInitialContextExample "
                         + "[WebLogicURL [userName password]]");
      System.out.println("Example:java examples.jndiInitialContextExample "
                         + "t3://localhost:7001 john mypasswd");

    }
    else {
      int idx = 0;
      // parse the url, user, and password if given
      String url      = (args.length > idx) ? args[idx++] : null;
      String user     = (args.length > idx) ? args[idx++] : null;
      String password = (args.length > idx) ? args[idx++] : "";
      Context ctx = null;

      System.out.println("url=" + url);
      System.out.println("user=" + user);
      System.out.println("password=" + password);

      try {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
          weblogic.jndi.WLInitialContextFactory.class.getName());
        if (url != null) {
          env.put(Context.PROVIDER_URL, url);
        }
        if (user == null) {
          ctx = new InitialContext(env);
          System.out.println("WebLogic context created on behalf of \"guest\"");
        } else {
	  // T3User encrypts user and password, but is not J2EE compliant.
	  // T3User has been deprecated.
          //env.put(Context.SECURITY_CREDENTIALS, new T3User(user, password));

          // The following 2 lines show an alternative method for specifying name
          // and password that transmits the password in clear text:
          env.put(Context.SECURITY_PRINCIPAL, user);
          env.put(Context.SECURITY_CREDENTIALS, password);
          ctx = new InitialContext(env);
          System.out.println("WebLogic context created on behalf of \"" + user + "\"");
        }
      }
      catch (AuthenticationException e) {
        System.out.println("You've specified an invalid user name or password");
      }
      catch (CommunicationException e) {
        System.out.println(
          "Failed to contact " +
          ((url == null) ? "t3://localhost:7001" : url) + ".\n" +
          "Is there a server running at this address?"
        );
      }
      catch (Exception e) {
        System.out.println("An unexpected exception occurred:" +e);
        e.printStackTrace();
      }
      finally {
        if (ctx != null) {
        // Always close context when finished.
          try {
            ctx.close();
          }
          catch (NamingException e) {
            System.out.println("Failed to close context due to: " + e);
          }
        }
      }
    }
  }
}

