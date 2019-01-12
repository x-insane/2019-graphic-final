package com.xinsane.image_enhancer.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class PaintServlet extends HttpServlet {

    static {
        File dir = new File("./images/paint");
        if (!dir.exists()) {
            if (!dir.mkdirs())
                System.err.println("can not create directory images/paint");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filename = request.getRequestURI().replace("/paint/", "");
        if (Boolean.TRUE.equals(request.getSession().getAttribute(filename))) {
            File src = new File("./images/" + filename);
            File dest = new File("./images/paint/" + filename);
            if (!dest.exists() && src.exists()) {
                try {
                    System.out.println("./paint " + src.getAbsolutePath() + " " + dest.getAbsolutePath());
                    String[] exec = new String[] {
                            "./paint", src.getAbsolutePath(), dest.getAbsolutePath()
                    };
                    Process process = Runtime.getRuntime().exec(exec);
                    BufferedInputStream bis = new BufferedInputStream(process.getErrorStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(bis));
                    String line;
                    while ((line = br.readLine()) != null)
                        System.err.println(line);
                    process.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
