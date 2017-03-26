
package com.sree.paytmhelloapp;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 * @author sreejithu.panicker
 * 
 */
public class HelloApp {
    //http embeded server 
    private Server jettyServer;
    
    // entry point for the application.
    public static void main(String args[]) throws Exception{
        
        //creating the instance of the application
        HelloApp application  = new HelloApp();
        //calling shutdownhook
        application.shutDownHook();
        //calling the main function
        application.startServer();
        
        
        
    }
    private  void shutDownHook() throws Exception{
        /*
        Gracefull shutdown hook for the application,
        it will be called when 'docker stop' is called. 
       */  
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {
                    stopServer();
                    System.out.println("graceful shutdown");
                } catch (Exception e) {
                   System.out.println(e);
                }
            }

        });
    }
    
    private void startServer() throws Exception{
        
         //creating the server
        jettyServer = new Server(80);
        //creating the servlet context handler 
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        
       
        //attaching the handler
        jettyServer.setHandler(contextHandler);
        //creating servlet holder 
        ServletHolder helloWorldHolder = contextHandler.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        helloWorldHolder.setInitOrder(0);
        
        //adding the RestService to servlet 
        helloWorldHolder.setInitParameter("jersey.config.server.provider.classnames",
                com.sree.paytmhelloapp.HelloWorld.class.getCanonicalName());
        
        jettyServer.start();
        jettyServer.join();

        
    }
    //stoping the server
    private void stopServer() throws Exception{
        jettyServer.destroy();
    }
    
}
