package com.walmartlabs.concord.plugins;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Path;

public class OKHttpDownloadManager
        implements com.walmartlabs.concord.sdk.DependencyManager,
        com.walmartlabs.concord.runtime.v2.sdk.DependencyManager
{

    private final File toolDir;

    public OKHttpDownloadManager(String tool)
    {
        this.toolDir = new File(System.getProperty("user.home"), ".m2/tools/" + tool);
        this.toolDir.mkdirs();
    }

    @Override
    public Path resolve(URI uri)
            throws IOException
    {
        String urlString = uri.toString();
        String fileName = urlString.substring(urlString.lastIndexOf('/') + 1);
        File target = new File(toolDir, fileName);
        if (!target.exists()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(urlString).build();
            Call call = client.newCall(request);
            Response response = call.execute();
            if (response.code() == 404) {
                throw new RuntimeException(String.format("The URL %s doesn't exist.", uri));
            }
            download(response.body().byteStream(), target);
        }
        return target.toPath();
    }

    //
    // https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
    //
    // surprised this is the fastest way to convert an inputstream to a string
    //
    private void download(InputStream stream, File target)
            throws IOException
    {
        byte[] buffer = new byte[8192];
        int length;
        try (OutputStream result = new FileOutputStream(target)) {
            while ((length = stream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
        }
    }
}
