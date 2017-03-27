/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sree.paytmserver;

import java.io.File;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 *
 * @author sreejithu.panicker
 */
public class LCBOApp {
    
    // entry to the application.
    public static void main(String[] args) throws Exception{
        
        //creating the jetty server;
        Server jettyServer = new Server(80);
        
        //reading the war file from predefined location for case 3
        //File warFile = new File("/var/javaapp/paytmlcbo.war");
        
        //reading the war for case 4
        File warFile = new File("/var/javaapp/paytmnoplogin.war");
        
        //reading the war for case 4 & 5 
        //File warFile = new File("/var/javaapp/paytmlogin.war");
        if (!warFile.exists()){
            throw new RuntimeException("file not found");
        }
        WebAppContext webLcboContext = new WebAppContext();
        
        //Webcontext for case 3
        //webLcboContext.setContextPath("/paytmlcbo");
        
        // WebContext for case 4
        
        webLcboContext.setContextPath("/paytmnoplogin");
        
        //webcontext for case 4 & 5
        //webLcboContext.setContextPath("/paytmlogin");
        webLcboContext.setWar(warFile.getAbsolutePath());
        webLcboContext.setExtractWAR(true);
        //setting jsp configuration
        Configuration.ClassList classlist = Configuration.ClassList
                .setServerDefault( jettyServer );
        classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                "org.eclipse.jetty.annotations.AnnotationConfiguration" );
        webLcboContext.setAttribute(
                "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$" );
        jettyServer.setHandler(webLcboContext);
        jettyServer.start();
        jettyServer.join();
    }
    
}
