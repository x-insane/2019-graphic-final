package com.xinsane.image_enhancer;

import com.xinsane.image_enhancer.servlet.EmbossServlet;
import com.xinsane.image_enhancer.servlet.PaintServlet;
import com.xinsane.image_enhancer.servlet.SourceServlet;
import com.xinsane.image_enhancer.servlet.UploadServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.File;

public class Application {

    static {
        String dllFilename = "./opencv/libopencv_java401.so";
        if (System.getProperty("os.name").toLowerCase().contains("windows"))
            dllFilename = "./opencv/opencv_java401.dll";
        try {
            File dllFile = new File(dllFilename);
            System.load(dllFile.getAbsolutePath());
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 1963;
        if (args.length > 0)
            port = Integer.parseInt(args[0]);
        
        Server server = new Server();

        // HTTP Connector
        ServerConnector httpConnector = new ServerConnector(server);
        httpConnector.setPort(port);
        server.addConnector(httpConnector);

        HandlerList gzipHandlerList = new HandlerList();

        // Resource Handler
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        String resource_path = Application.class.getResource("/target").toString();
        resourceHandler.setResourceBase(resource_path);
        gzipHandlerList.addHandler(resourceHandler);

        // GZIP Support
        GzipHandler gzip = new GzipHandler();
        gzip.setHandler(gzipHandlerList);

        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(gzip);

        // Servlet Handler
        ServletContextHandler servletHandler = new ServletContextHandler();
        servletHandler.setSessionHandler(new SessionHandler());
        servletHandler.addServlet(UploadServlet.class, "/upload");
        servletHandler.addServlet(SourceServlet.class, "/source/*");
        servletHandler.addServlet(EmbossServlet.class, "/emboss/*");
        servletHandler.addServlet(PaintServlet.class, "/paint/*");
        handlerList.addHandler(servletHandler);

        server.setHandler(handlerList);

        // Start Server
        server.setStopAtShutdown(true);
        server.start();
    }

}
