/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sree;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
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
                    UserObject userObject = readUser(logName, pass);
                    if (userObject!=null) {
                        HttpSession httpSession = request.getSession(true);
                        httpSession.setAttribute("logName",logName);
                        httpSession.setAttribute("sessionId", httpSession.getId());
                        request.setAttribute("fullname",userObject.getUserName());
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
                    UserObject userObjecct = readUser(loginName,null);

                    if (userObjecct == null) {
                        //if the creation is sucessfull
                        if (createUser(loginName,userName,passWord) != null){
                            HttpSession httpSession = request.getSession(true);
                            httpSession.setAttribute("logName",loginName);
                            httpSession.setAttribute("sessionId", httpSession.getId());
                            request.setAttribute("fullname",userName);
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
                    //invalidating sesion
                    httpSession.invalidate();
                }
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                break;
                // change password logic
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
        // Here we are creating a concurrentHash map to store the user details
        ConcurrentHashMap hashMap = new ConcurrentHashMap();
        getServletContext().setAttribute("user", hashMap);
    }

    @Override
    public void destroy() {
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
    }

    /*
     this function will read the data from couch db/servlet Context based on username(loginName)
    */

    private UserObject readUser(String userName, String password) {

        UserObject user = (UserObject)((ConcurrentHashMap)getServletContext().getAttribute("user")).get(userName);
        if( user != null && password!=null){
           if (!user.getPassWord().equals(password)){
               return null;
           }
        }
        return user;
    }

    /*
     This function will create the user in coucnbase db/(servlet Context)
    */
    private UserObject createUser(String loginName, String fullName, String passWord) {
        //Standered  user feilds
        UserObject userObject = new UserObject();
        userObject.setUserName(fullName);
        userObject.setPassWord(passWord);
        ((ConcurrentHashMap)getServletContext().getAttribute("user")).put(loginName, userObject);
        return userObject;
    }

    private  void updatePassword(String logName, String passwd){
        ((UserObject)((ConcurrentHashMap)getServletContext().getAttribute("user")).get(logName)).setPassWord(passwd);
    }
}
