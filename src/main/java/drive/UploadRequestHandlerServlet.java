/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.GoogleApiTokenInfo;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpResponseException;

/**
 *
 * @author Dhaval
 */
@WebServlet(name = "TestingServlet", urlPatterns =
{
    "/drive/submit_upload_url"
})
public class UploadRequestHandlerServlet extends HttpServlet
{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
    {
	response.setContentType("application/json;charset=UTF-8");
	try (PrintWriter out = response.getWriter())
	{
	    GoogleApiTokenInfo tokenInfo = (GoogleApiTokenInfo) request.getSession().getAttribute("token");

	    if (tokenInfo == null)
	    {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return;
	    }

	    String errorMessage = null;
	    URL url;
	    String fileName;

	    try
	    {

		String urlString = request.getParameter("url");
		url = new URL(urlString);
		System.err.println("url is valid");
		fileName = request.getParameter("filename");
	    }
	    catch (MalformedURLException e)
	    {
		e.printStackTrace();
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL is invalid.");
		return;
	    }

	    DriveUploader uploader;

	    if (fileName == null)
		uploader = new DriveUploader(url, tokenInfo);
	    else
		uploader = new DriveUploader(url, tokenInfo, fileName);

	    try
	    {
		uploader.init();
	    }
	    catch (NoHttpResponseException ex)
	    {
		errorMessage = ex.getMessage();
	    }
	    catch (UnknownHostException ex)
	    {
		errorMessage = "No host found with url: " + url;
	    }
	    catch (HttpResponseException ex)
	    {
		errorMessage = "Error: " + ex.getStatusCode() + " " + ex.getLocalizedMessage();
	    }
	    catch (IOException ex)
	    {
		errorMessage = "ErrorMessage: " + ex.getLocalizedMessage();
	    }

	    String id = UploadManager.getUploadManager().add(uploader);

	    ObjectMapper mapper = new ObjectMapper();
	    ObjectNode rootNode = mapper.createObjectNode();

	    if (errorMessage != null)
		rootNode.put("error", errorMessage);
	    else
		rootNode.put("id", id);

	    out.print(rootNode.toString());
	    response.setStatus(HttpServletResponse.SC_ACCEPTED);
	}
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
    {
	processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
    {
	processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
	return "Short description";
    }// </editor-fold>

}
