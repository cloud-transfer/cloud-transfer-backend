package com.github.dhaval_mehta.savetogoogledrive.uploader;

import com.github.dhaval_mehta.savetogoogledrive.model.UploadInformation;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Dhaval
 */
public class UploadManager {

    private static final UploadManager UPLOAD_MANAGER;

    static {
        UPLOAD_MANAGER = new UploadManager();
    }

    private final ExecutorService executorService;
    private final HashMap<String, UploadTask> idToTaskMap;

    private UploadManager() {
        executorService = Executors.newFixedThreadPool(10);
        idToTaskMap = new HashMap<>();
    }

    public static UploadManager getUploadManager() {
        return UPLOAD_MANAGER;
    }

    public UploadInformation getUploadInformation(@NotNull String id) {
        UploadTask uploadTask = idToTaskMap.get(id);

        if (uploadTask == null)
            return null;

        return uploadTask.getUploader().getUploadInformation();
    }

    public void add(@NotNull UploadTask uploadTask) {
        idToTaskMap.put(uploadTask.getId(), uploadTask);
        executorService.execute(uploadTask);
    }

    public void distroy() {
        executorService.shutdown();
    }
}
