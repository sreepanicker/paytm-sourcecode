/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sree;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author sreejithu.panicker
 */
public class searchservice extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String productId = (String) request.getParameter("productid");
        if (productId != null) {
            String product = productId.trim();
            JsonObject jsonObject = getProductDetails(product);
            if (jsonObject != null) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("<div class=\"data\">");
                    stringBuilder.append("<label> Product details for ID: ").append(productId).append("</label>");
                    stringBuilder.append("<label> Name :").append(jsonObject.getJsonObject("result").getJsonString("name")).append("</label>");
                    float price = jsonObject.getJsonObject("result").getInt("price_in_cents", 0);
                    stringBuilder.append("<label> Price: $").append(price/100).append("</label>");
                    stringBuilder.append("<label> Tags:").append(jsonObject.getJsonObject("result").getJsonString("tags")).append("</label>");
                    stringBuilder.append("<label> Primary category:").append(jsonObject.getJsonObject("result").getJsonString("primary_category")).append("</label>");
                    stringBuilder.append("<label> Origin:").append(jsonObject.getJsonObject("result").getJsonString("origin")).append("</label>");
                    stringBuilder.append("<label> Package:").append(jsonObject.getJsonObject("result").getJsonString("package")).append("</label>");
                    stringBuilder.append("<label> Alcohol content:").append(jsonObject.getJsonObject("result").getInt("alcohol_content", 0)).append("</label>");
                    stringBuilder.append("<label> Inventory count:").append(jsonObject.getJsonObject("result").getInt("inventory_count", 0)).append("</label>");
                    stringBuilder.append("</div>");
                    display(response, stringBuilder.toString());
                } catch (Exception e) {
                    String message = "<div class=\"data\"> <label> Invaild Product ID : ((LCBO server might be down))" + productId + " </label> </div>";
                    display(response, message);
                }
            } else {
                String message = "<div class=\"data\"> <label> Invaild Product ID: " + productId + " </label> </div>";
                display(response, message);
            }
        } else {

            String message = "<div class=\"data\"> <label> Invaild Product ID  </label> </div>";
            display(response, message);
        }
    }

    private void display(HttpServletResponse response, String message) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Search Result</title>");
            out.println("<style>\n" +
"label {\n" +
"	width: 100%;\n" +
"	display: inline-block;\n" +
"}\n" +
"\n" +
".submitbtn {\n" +
"    background-color: #4CAF50;\n" +
"    color: white;\n" +
"    padding: 14px 20px;\n" +
"    margin: 8px 0;\n" +
"    border: none;\n" +
"    cursor: pointer;\n" +
"    width: 30%;\n" +
"    position: absolute;\n" +
"    left: 25%;\n" +
"}\n" +
"form {\n" +
"	border:1px solid #ccc;\n" +
"	position: absolute;\n" +
"	height: 75%;\n" +
"	width: 60%;\n" +
"	top: 15%;\n" +
"	left: 15%;\n" +
"	margin-top: -50px;\n" +
"       margin-left: -100px;\n" +
"}\n" +
"\n" +
".container {\n" +
"    padding: 16px;\n" +
"}\n" +
"</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<form name='sendback' action='/paytmlcbo/search.jsp'>");
            out.println(message);
            out.println("<input class=\"submitbtn\" type=\"submit\" value=\"Search Again\">");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private JsonObject getProductDetails(String productId) {
        JsonObject jsonObject = null;
        try {
            //lcbo api URL 
            String URL = "http://lcboapi.com/products/" + productId;
            //lcbo tokem
            String token = "MDo1MmFmNDU2OC0wZmRhLTExZTctOTlkZS1hM2YxZGU5YzUxZWI6amZuU0FwTnZnY2t4b1paVmVTY20wN3lMc2tOZGR2MnQwaFFo";
            Client client = ClientBuilder.newClient();
            WebTarget webTarget = client.target(URL);
            Builder request = webTarget.request();
            request.header("Content-type", MediaType.APPLICATION_JSON);
            request.header("Authorization", "Token " + token);
            Response response = request.get();
            if (response.getStatus() == 200) {
                JsonReader jsonReader = Json.createReader(new StringReader(response.readEntity(String.class)));
                jsonObject = jsonReader.readObject();
            }
            client.close();
        } catch (Exception e) {
            //got the excecption , service is down
            //log the error
            System.out.println(e);
        }
        return jsonObject;
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
            throws ServletException, IOException {
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
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "search service";
    }// </editor-fold>

}
