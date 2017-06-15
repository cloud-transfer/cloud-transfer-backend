/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drive;

/**
 *
 * @author Dhaval
 */
public class UploaderRunnable implements Runnable
{

    DriveUploader uploader;

    public UploaderRunnable(DriveUploader uploader)
    {
	this.uploader = uploader;
    }

    @Override
    public void run()
    {
	uploader.upload();
    }

}
