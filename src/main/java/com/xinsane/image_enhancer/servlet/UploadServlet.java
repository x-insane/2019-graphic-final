package com.xinsane.image_enhancer.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xinsane.image_enhancer.helper.MD5Helper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class UploadServlet extends HttpServlet {

    private static final List<String> suffixes = Arrays.asList(".jpg", ".jpeg", ".png", ".bmp");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        if (!ServletFileUpload.isMultipartContent(request))
            throw new IllegalArgumentException("Request is not multipart.");
        PrintWriter writer = response.getWriter();
        JsonObject res = new JsonObject();
        JsonArray array = new JsonArray();
        try {
            ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
            uploadHandler.setFileSizeMax(2 * 1024 * 1024); // 单个文件不能超过2M
            List<FileItem> items = uploadHandler.parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    JsonObject object = new JsonObject();
                    object.addProperty("field", item.getFieldName());
                    object.addProperty("name", item.getName());
                    object.addProperty("type", item.getContentType());
                    String suffix = item.getName().substring(item.getName().lastIndexOf(".")).toLowerCase();
                    if (suffixes.contains(suffix)) {
                        object.addProperty("accept", true);
                        String filename = MD5Helper.md5(item.get()) + suffix;
                        object.addProperty("save_name", filename);
                        File file = new File("./images", filename);
                        if (!file.exists())
                            item.write(file);
                        BufferedImage image = ImageIO.read(new FileInputStream(file));
                        object.addProperty("width", image.getWidth());
                        object.addProperty("height", image.getHeight());
                        request.getSession().setAttribute(filename, true);
                    }
                    else {
                        object.addProperty("accept", false);
                        object.addProperty("reject_reason", "文件后缀不允许");
                    }
                    array.add(object);
                }
            }
            res.addProperty("error", 0);
            res.add("res", array);
        } catch (Exception e) {
            res.addProperty("error",1);
            res.addProperty("msg", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            writer.write(res.toString());
            writer.close();
        }
    }

}
