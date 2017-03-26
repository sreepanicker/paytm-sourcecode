/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sree;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.PersistTo;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author sreejithu.panicker
 */
public class controller extends HttpServlet {

    //structure  of the Json Object
    /*
     {  ID : "panicker",
     fullname : "sreejithu Panicker",
     passwd  : "xxxxx",
     activity : [
     {action : "account creation", time :"2011-01-01"},{action : "Login ", time :"2011-01-01"}
     ]
     }
    
     */
    //typically these are part of connection pool
    private static Cluster couchbaseCluster;
    private static Bucket eventBucket;
    private static final String COUCHBASEURL = "paytmcouchbase";
    private static final String BUCKETNAME = "USER_PROFILE";

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
        String actionItem = (request.getParameter("hiddenitem") != null) ? request.getParameter("hiddenitem") : "";
        switch (actionItem) {
            case "login":
                // if the login is success 
                String logName = (request.getParameter("username") != null) ? request.getParameter("username") : "";
                String pass = (request.getParameter("password") != null) ? request.getParameter("password") : "";
                
                //serverside validation 
                if (!logName.equals("") && !pass.equals("")) {
                    JsonDocument jsonCasDocumnet = readUser(logName, pass);
                    if (jsonCasDocumnet!=null) {
                        HttpSession httpSession = request.getSession(true);                       
                        httpSession.setAttribute("logName",logName);
                        httpSession.setAttribute("sessionId", httpSession.getId());
                        request.setAttribute("json", updateUserActivity(jsonCasDocumnet, "User login"));
                        getServletContext().getRequestDispatcher("/profiledetail.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error", " * Invaild user name/password or not present");
                        getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
                    }
                }else{
                    request.setAttribute("error", " * Data validation error");
                    getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
                }
                break;
            case "newuser":
                // check first user is there 
                //validate the data
                String loginName = (request.getParameter("loginname") != null) ? request.getParameter("loginname") : "";
                String userName = (request.getParameter("username") != null) ? request.getParameter("username") : "";
                String passWord = (request.getParameter("password") != null) ? request.getParameter("password") : "";
                String repPassword = (request.getParameter("password_repeat") != null) ? request.getParameter("password_repeat") : "";
                //validating repeat password and password are same && username is not "" and loginame is not blank
                if (repPassword.equals(passWord) && !userName.equals("") && !loginName.equals("") ) {
                    // checking user already exists
                    JsonDocument jsonDocument = readUser(loginName,null);
                   
                    if (jsonDocument == null) {
                        //if the creation is sucessfull
                        JsonDocument jsonCasDocumnet = createUser(loginName,userName,passWord);
                        if (jsonCasDocumnet != null){
                            HttpSession httpSession = request.getSession(true);
                            httpSession.setAttribute("logName",loginName);
                            httpSession.setAttribute("sessionId", httpSession.getId());
                            request.setAttribute("json",jsonCasDocumnet );
                            getServletContext().getRequestDispatcher("/profiledetail.jsp").forward(request, response);
                        }else{
                            request.setAttribute("error", " * Unknown error !");
                            getServletContext().getRequestDispatcher("/newuser.jsp").forward(request, response);
                        }
                    } else {
                        request.setAttribute("error", " * User already exists!");
                        getServletContext().getRequestDispatcher("/newuser.jsp").forward(request, response);
                    }
                }else{
                    request.setAttribute("error", " * Data validation error at server!");
                    getServletContext().getRequestDispatcher("/newuser.jsp").forward(request, response);
                }
                break;
            case "logout":
                //getting the current session assocaiated with current request;
                HttpSession httpSession = request.getSession();
                if (httpSession != null) {
                    JsonDocument jsonCasDocumnet = readUser((String)httpSession.getAttribute("logName"), null);
                    updateUserActivity(jsonCasDocumnet,"Logout");
                    httpSession.invalidate();
                }
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                break;
            case "changepassword":
                String password = (request.getParameter("password") != null) ? request.getParameter("password") : "";
                String reppassword = (request.getParameter("password_repeat") != null) ? request.getParameter("password_repeat") : "";
                if (password.equals(reppassword)){
                    updatePassword((String)request.getSession().getAttribute("logName"),password);
                    request.setAttribute("error", " * password changed");                    
                }else {
                    request.setAttribute("error", "Password doesn't match!");
                }
                request.getSession().invalidate();
                getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
                break;
            default:
                break;
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
        return "Short description";
    }// </editor-fold>

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        try {
            couchbaseCluster = CouchbaseCluster.create(COUCHBASEURL);
            eventBucket = couchbaseCluster.openBucket(BUCKETNAME,1,TimeUnit.MINUTES);
            System.out.println("Connected");
        } catch (Exception e) {
            System.out.println(e);
            throw new ServletException("Connection error(db)");
        }
    }

    @Override
    public void destroy() {
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
        try {
            eventBucket.close();
            couchbaseCluster.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    /*
     this function will read the data from couch db based on username(loginName)
    */
    
    private JsonDocument readUser(String userName, String password) {
        JsonDocument jsonDocument = eventBucket.get(userName);
        if( jsonDocument != null && password!=null){
           String pass = (String) jsonDocument.content().get("passwd");
           if (!pass.equals(password)){
               return null;
           }
        }
        return jsonDocument;
    }

    /*
     This function will create the user in coucnbase db
    */
    private JsonDocument createUser(String userName, String fullName, String passWord) {
        //Standered  user feilds
        JsonObject jsonObject = JsonObject.create();
        jsonObject.put("fullname", fullName);
        jsonObject.put("passwd", passWord);
        
        //Activity feilds
        JsonArray jsonArray = JsonArray.create();
        JsonObject jsonActivityObj = JsonObject.create();
        jsonActivityObj.put("action", "User Created");
        jsonActivityObj.put("time", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        jsonArray.add(jsonActivityObj);
        
        //Adding activity to the jsonObject 
        jsonObject.put("activity", jsonArray);
        
        //setiing the ID 
        JsonDocument jsonDocument = JsonDocument.create(userName, jsonObject);
        JsonDocument jsonDocumentCAS = eventBucket.insert(jsonDocument, PersistTo.ONE);
        return jsonDocumentCAS;
    }
    // This function will update the user activity and give back a CAS JsonDocument
    
    private JsonDocument updateUserActivity(JsonDocument jsonCAS, String actionString){     
        //creating a new activity 
        JsonObject jsonActivityObj = JsonObject.create();
        jsonActivityObj.put("action", actionString);
        jsonActivityObj.put("time", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        //refering the JsonArray and adding a new data
        jsonCAS.content().getArray("activity").add(jsonActivityObj);
        //upsert the data
        return  eventBucket.replace(jsonCAS,PersistTo.ONE);
        
    }
    private  void updatePassword(String logName, String passwd){
        JsonDocument jsonDocument = readUser(logName, null);
        jsonDocument.content().put("passwd", passwd);
        updateUserActivity(jsonDocument,"User changed password");
    }
}
