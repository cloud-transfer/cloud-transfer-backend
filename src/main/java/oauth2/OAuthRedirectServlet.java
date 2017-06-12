/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.utils.URIBuilder;

/**
 *
 * @author Dhaval
 */
@WebServlet(name = "OAuthRedirectServlet", urlPatterns =
{
    "/OAuth2/OAuthRedirectServlet"
})
public class OAuthRedirectServlet extends HttpServlet
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
	Logger logger = Logger.getLogger(OAuthRedirectServlet.class.getName());

	try
	{

	    String oauthUrl = "https://accounts.google.com/o/oauth2/auth";

	    URIBuilder builder = new URIBuilder(oauthUrl);
	    builder.addParameter("scope", "https://www.googleapis.com/auth/drive");
	    builder.addParameter("access_type", "offline");
	    builder.addParameter("redirect_uri", System.getenv("redirect_uri"));
	    builder.addParameter("response_type", "code");
	    builder.addParameter("client_id", System.getenv("client_id"));

	    String redirectUrl = builder.build().toString();
	    response.sendRedirect(redirectUrl);
	}

	catch (URISyntaxException ex)
	{
	    logger.log(Level.SEVERE, null, ex);
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
