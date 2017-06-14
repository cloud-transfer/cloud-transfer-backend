/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drive;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.Status;
import model.SuccessStatus;

/**
 *
 * @author Dhaval
 */
public class UploadManager
{

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final HashMap<String, DriveUploader> idToUploaderMap = new HashMap<>();
    private final SecureRandom random = new SecureRandom();
    private static final UploadManager UPLOAD_MANAGER;
    
    static
    {
	UPLOAD_MANAGER = new UploadManager();
    }

    private UploadManager()
    {
    }

    public String add(DriveUploader uploader)
    {
	executorService.submit(new UploaderRunnable(uploader));
	String key = uploader.hashCode() + nextKey() + System.currentTimeMillis();
	idToUploaderMap.put(key, uploader);
	return key;
    }

    private String nextKey()
    {
	return new BigInteger(130, random).toString(32);
    }

    public Status getUploadStatus(String id)
    {
	DriveUploader uploader = idToUploaderMap.get(id);

	if (uploader == null)
	    return null;

	Status status = uploader.getStatus();

	if (status instanceof SuccessStatus)
	{
	    SuccessStatus successStatus = (SuccessStatus) status;
	    if (successStatus.getCompletePercentage() == 100)
		idToUploaderMap.remove(id);
	}
	else
	    idToUploaderMap.remove(id);

	return status;
    }

    public static UploadManager getUploadManager()
    {
	return UPLOAD_MANAGER;
    }
}
