import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

/**
 * This example illustrates how to obtain and use a WebLogic naming context.
 * The same code will work on both a client and a server.  If it is run
 * on a client, it will delegate to a remote context running on the
 * server specified by <tt>java.naming.provider.url</tt>.  If it is run on
 * the server specified by the <tt>java.naming.provider.url</tt>, a local
 * context is used.  If it is run on some other server, the context will
 * again delegate to a context located on the server specified by
 * <tt>java.naming.provider.url</tt>.
 * <p>
 * The example first creates a default naming context, then binds an object
 * into that context under a newly created subcontext, and finally looks up
 * the object. Note that the binding created here will be available for the
 * life of the server to any client that obtains a WebLogic InitialContext
 * through the same server.
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
 * The <a href="build.xml">build.xml</a> script is stored in the
 * <font face="Courier New" size = -1><i>SAMPLES_HOME</i>\server\examples\src\examples\jndi</font>
 * directory, where <font face="Courier New" size = -1><i>SAMPLES_HOME</font></i> refers
 * to the directory for all samples and examples for the WebLogic Platform.
 * <p>
 * To build the example, move to the JNDI example directory and run the script using the
 * following Java ant command:
 * <pre><font face="Courier New">prompt&gt;<b> ant </b></font></pre>
 * <p>
 * </ol>
 * <h3><a name="Run the Example"></a>Run the Example</h3>
 * <p>
 * In your development shell, run the client with the following command:
 * <pre><font face="Courier New">prompt&gt;<b> ant run.weblogiccontext </b></font></pre>
 * Parameters contained within the run script are:
 * <dl>
 * <dt><i><font face="Courier New" size = -1><b>hostname</b></font></i>
 * <dd>Host name of the WebLogic Server.
 * <dt><i><font face="Courier New" size = -1><b>port</b></font></i>
 * <dd>Port where the WebLogic Server is listening for connections.
 * </dl>
 * If no argument is provided, the URL defaults to "t3://localhost:7001".
 *
 * <p>
 * Try running this example a second time within the same WebLogic Server session.
 * Note that although the JNDI binding has already been created (the first time you
 * ran this example), no exception in thrown when the subcontext is recreated.
 * WebLogic's context implementation does not throw an exception if the
 * object that is bound to an existing name is identical to the current object that
 * is bound to that name. This example generates a unique string (using the date) for
 * the bound object. When binding this object for the second time, an
 * <tt>NameAlreadyBoundException</tt> is thrown since the object is not identical to
 * the object that is currently bound to the same name. Now, the <tt>rebind()</tt>
 * method must be used to successfully bind the new object.
 * <p>
 * The key to creating an InitialContext is correctly specifying
 * the required properties.  The <tt>java.naming.factory.initial</tt> property
 * (referred to here via the static <tt>InitialContext.INITIAL_CONTEXT_FACTORY</tt>)
 * specifies the factory that is used to create
 * the context. Other properties (like <tt>java.naming.provider.url</tt>)
 * are used to pass on parameters to the specified factory.
 * <p>
 * These properties can either be provided as system properties or passed
 * explicitly to the InitialContext constructor.  This example creates and
 * passes the properties explicitly for clarity. Note that passing the
 * properties explicitly is the only alternative when working in an applet,
 * since an applet is not allowed to modify or read all system properties.
 *
 * @author Copyright (c) 1999-2005 by BEA Systems, Inc. All Rights Reserved.
 */
public class StudentInfoContextModified {

  private static final HashMap<Integer, String> map = new HashMap<>();
  static {
    map.put(1, "one");
    map.put(2, "two");
    map.put(3, "three");
  }

  /**
   * Runs this example from the command line.
   */
  public static void main(String[] args) {
    if (args.length > 1) {
      System.out.println(
        "Usage: java examples.jndi.WebLogicContextExample WebLogicURL\n" +
        "If no URL is provided, t3://localhost:7001 is used");
    }
    else {
      Context ctx = null;
      try {
        String url = (args.length == 1) ? args[0] : null;
        System.out.println(url);
        Hashtable env = new Hashtable();
        // This *required* property specifies the factory to be used
        // to create the context.
        env.put(
                Context.INITIAL_CONTEXT_FACTORY,
                "weblogic.jndi.WLInitialContextFactory"
        );

        if (url != null) {
          // This property specifies the URL of the WebLogic Server that will
          // provide the naming service.  Defaults to t3://localhost:7001
          env.put(Context.PROVIDER_URL, url);
        }

        ctx = new InitialContext(env);
        System.out.println("Initial context created");

        try {
          ctx.createSubcontext("student");
          System.out.println("Subcontext 'student' created");
        } catch (NameAlreadyBoundException e) {
          // Subcontext already exists.
          // Note that WebLogic's Context implementation does not throw this
          // exception if the name is already bound to an identical object.
          System.out.println(
                  "Subcontext 'student' already exists;"
                          + " continuing with existing subcontext"
          );
        }

        //BIND OBJECTS

        ArrayList<StudentInfo> students = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {

          StudentInfo studentInfo = new StudentInfo();
          String bindStr = "Student #" + i;
          studentInfo.setFirst_name(bindStr);
          studentInfo.setEmail("studentEmail"+i+"@jhu.edu");
          students.add(studentInfo);
          String bindName = "student." + map.get(i);

          // Create a unique String object (bindStr) and bind it to (bindName)
          DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
                  DateFormat.DEFAULT,
                  Locale.getDefault());
          try {
            ctx.bind(bindName, studentInfo);
            System.out.println("Bound '" + bindStr + "' to '" + bindName + "' for the first time.");
          } catch (NameAlreadyBoundException e) {
            // Binding already exists.
            // As for the createSubcontext() method, this exception is not thown
            // by the WebLogic implementation of Context when the name is aleady
            // bound to an identical object. However, we have created a unique
            // string by including the date and time here. Run the example more than
            // once in the same WL sever session and the exception will be thrown.
            System.out.println("Overriding old binding. Rebinding '" +
                    bindStr + "' to '" + bindName + "'.");
            // Force a new binding
            ctx.rebind(bindName, studentInfo);
          }

          StudentInfo bound = (StudentInfo) ctx.lookup(bindName);
          if(bound != null && bound.getFirst_name().equals(bindStr)){
            System.out.println("Student Binding Successful");
          }else{
            System.out.println("Student Binding Failed.");
          }

        }

        System.out.println("");

        //VERIFY STATE IS SAME.

        for(int i = 1; i <= 3; i++){
          String bindName = "student." + map.get(i);
          StudentInfo student = (StudentInfo) ctx.lookup(bindName);

          if(student.getFirst_name().equals(students.get(i-1).getFirst_name())
                  && student.getEmail().equals(students.get(i-1).getEmail())){
            System.out.println(student.getFirst_name() + "(Pulled from WLS)" + " = " + students.get(i-1).getFirst_name() + " (Local Copy)");
            System.out.println(student.getEmail()+ "(Pulled from WLS)" + " = " + students.get(i-1).getEmail() + " (Local Copy)");
            System.out.println("Binding State Verified Identical");
            System.out.println();
          }else{
            System.out.println("Binding State Verified Changed");
          }

        }


      }
      catch (NamingException e) {
        System.out.println(e.toString());
      }
      finally {
        if (ctx != null) {
          try {
            ctx.close();
          } catch (NamingException e) {
            System.out.println("Failed to close context due to: " + e);
          }
        }
      }
    }
  }
}

