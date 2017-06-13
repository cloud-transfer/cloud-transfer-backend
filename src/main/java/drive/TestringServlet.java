/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drive;

import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "TestringServlet", urlPatterns =
{
    "/TestringServlet"
})
public class TestringServlet extends HttpServlet
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
	response.setContentType("text/html;charset=UTF-8");
	try (PrintWriter out = response.getWriter())
	{
	    GoogleApiTokenInfo tokenInfo = (GoogleApiTokenInfo) request.getSession().getAttribute("token");

	    if (tokenInfo == null)
	    {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return;
	    }

	    String errorMessage = null;
	    URL url = new URL(request.getParameter("url"));
	    String fileName = request.getParameter("filename");

	    DriveUploader uploader;

	    if (fileName == null)
		uploader = new DriveUploader(url, tokenInfo);
	    else
		uploader = new DriveUploader(url, fileName, tokenInfo);

	    try
	    {
		uploader.upload();
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

	    if (errorMessage != null)
		out.print(errorMessage);
	    else
		out.print("Successfully Uploaded!");
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
