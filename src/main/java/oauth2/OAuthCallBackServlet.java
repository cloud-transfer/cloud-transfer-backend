/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.OAuthException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import javax.servlet.http.HttpSession;
import model.GoogleApiTokenInfo;
import static oauth2.OAuthUtility.isHttpSuccessStatusCode;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Dhaval
 */
@WebServlet(name = "OAuthCallBackServlet", urlPatterns =
{
    "/OAuth2/callback"
})
public class OAuthCallBackServlet extends HttpServlet
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
	PrintWriter out = response.getWriter();

	if (request.getParameter("code") != null)
	{
	    String code = request.getParameter("code");
	    try
	    {
		GoogleApiTokenInfo tokenInfo = getAccessToken(code);
		HttpSession session = request.getSession();
		session.setAttribute("token", tokenInfo);
		out.print(tokenInfo);
	    }
	    catch (OAuthException e)
	    {
		out.print(e.getMessage());
	    }
	}
	else if (request.getParameter("error") != null)
	{
	    String errorMessage = request.getParameter("error");
	    response.sendError(SC_UNAUTHORIZED, errorMessage);
	}
	else
	{
	    response.setStatus(SC_BAD_REQUEST);
	}
    }

    public GoogleApiTokenInfo getAccessToken(String code) throws IOException
    {
	String tokenUrl = "https://www.googleapis.com/oauth2/v4/token";
	String redirectUri = System.getenv("redirect_uri");
	HttpClient httpClient = HttpClientBuilder.create().build();
	HttpPost httpPost = new HttpPost(tokenUrl);

	List<NameValuePair> parameters = new ArrayList<>();
	parameters.add(new BasicNameValuePair("code", code));
	parameters.add(new BasicNameValuePair("client_id", System.getenv("client_id")));
	parameters.add(new BasicNameValuePair("client_secret", System.getenv("client_secret")));
	parameters.add(new BasicNameValuePair("redirect_uri", redirectUri));
	parameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
	httpPost.setEntity(new UrlEncodedFormEntity(parameters));

	org.apache.http.HttpResponse response = httpClient.execute(httpPost);
	int httpStatusCode = response.getStatusLine().getStatusCode();
	InputStream inputStream = response.getEntity().getContent();
	ObjectMapper objectMapper = new ObjectMapper();

	if (isHttpSuccessStatusCode(httpStatusCode))
	{
	    GoogleApiTokenInfo token = objectMapper.readValue(inputStream, GoogleApiTokenInfo.class);
	    return token;
	}
	else
	{
	    JsonNode rootNode = objectMapper.readTree(inputStream);
	    OAuthException exception = new OAuthException();
	    exception.setError(rootNode.get("error").asText());
	    exception.setDescription(rootNode.get("error_description").asText());
	    throw exception;
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
