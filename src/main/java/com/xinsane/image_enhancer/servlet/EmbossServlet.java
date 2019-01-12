package com.xinsane.image_enhancer.servlet;

import com.xinsane.image_enhancer.emboss.EmbossHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class EmbossServlet extends HttpServlet {

    static {
        File dir = new File("./images/emboss");
        if (!dir.exists()) {
            if (!dir.mkdirs())
                System.err.println("can not create directory images/emboss");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filename = request.getRequestURI().replace("/emboss/", "");
        if (Boolean.TRUE.equals(request.getSession().getAttribute(filename))) {
            File src = new File("./images/" + filename);
            File dest = new File("./images/emboss/" + filename);
            if (!dest.exists() && src.exists())
                EmbossHandler.handleImage(src.getAbsolutePath(), dest.getAbsolutePath());
            if (dest.exists()) {
                SourceServlet.readFile(response, dest);
                return;
            }
        }
        File errorImage = new File("./res/error.png");
        if (errorImage.exists())
            SourceServlet.readFile(response, errorImage);
    }

}
