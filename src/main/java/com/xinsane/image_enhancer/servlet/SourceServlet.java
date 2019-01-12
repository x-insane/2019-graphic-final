package com.xinsane.image_enhancer.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SourceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filename = request.getRequestURI().replace("/source/", "");
        if (Boolean.TRUE.equals(request.getSession().getAttribute(filename))) {
            File src = new File("./images/" + filename);
            if (src.exists()) {
                readFile(response, src);
                return;
            }
        }
        File errorImage = new File("./res/error.png");
        if (errorImage.exists())
            SourceServlet.readFile(response, errorImage);
    }

    static void readFile(HttpServletResponse response, File src) throws IOException {
        if (src.exists()) {
            response.setContentLengthLong(src.length());
            OutputStream outputStream = response.getOutputStream();
            FileInputStream inputStream = new FileInputStream(src);
            int length;
            byte[] buffer = new byte[1024];
            while ((length = inputStream.read(buffer)) != -1)
                outputStream.write(buffer, 0, length);
            inputStream.close();
            outputStream.close();
        }
    }

}
