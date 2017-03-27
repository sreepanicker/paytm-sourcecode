<%-- 
    Document   : profiledetail
    Created on : 25-Mar-2017, 12:32:04 AM
    Author     : sreejithu.panicker
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Profile Page</title>
        <%
            if (session.getAttribute("sessionId")==null ){
                response.sendRedirect("/paytmnoplogin/login.jsp");
            }else{
                String sessionId = (String)session.getAttribute("sessionId");
                if (!sessionId.equals(session.getId())){
                    response.sendRedirect("/paytmnoplogin/login.jsp");
                }
            }
            
        %>
        <style>
            .container{
                padding: 16px;
            }
            .logoutsec{
                width: 95%;
                color: #ccc;
                text-align: center;
                background-color: #FAA819;
                height: 20px;
                padding: 20px;
 
            }
            .submitbtn {
                background-color: #4CAF50;
                color: white;
                margin: 8px 0;
                border: none;
                cursor: pointer;
                width: 8%;
                float: right;
                height: 30px;
                
            }
            .loggedinuser{
                float: left
            }
            
            .footer {
                position:absolute;
                bottom:10px;
                width:95%;
                height:30px;
                background:#ccc;
                
            }
            .hiddenitem{
                visibility: hidden;
                display: none;
            }
            .changepass{
               width: 95%;
               visibility: hidden;
               height: 30px;
            }
            
            input[type=password] {
                width: 10%;
                display: inline-block;
                border: 1px solid #ccc;
                box-sizing: border-box;
                float:left;
                height: 30px;
            }
            .passwordsubmit{
                float:left;
                background-color: #4CAF50;
                display: inline-block;
                color: white;
                border: none;
                cursor: pointer;
                width: 15%;
                height: 30px;
                padding:0;
                bottom :20px
            }

            
            
        </style>
        <script>
            function anchorClick(){
                if (document.getElementById("passclass").style.visibility ==='visible'){
                    document.getElementById("passclass").style.visibility = "hidden";
                    document.getElementById("anchorid").innerHTML="Change Password";
                }else{
                    document.getElementById("passclass").style.visibility = "visible";
                    document.getElementById("anchorid").innerHTML="Ignore Password Change";
                }
            }
            function product() {
                var pass, reptpass;
                    pass = document.getElementById("password").value;
                    reptpass =document.getElementById("password_repeat").value;
                    if (pass !== reptpass) {
                        alert("Password doesn't match");
                        return false;
                    }
                    return true;
            }
        </script>
    </head>
    <body>        
        <div class="container">
            <div class="logoutsec">
                <form name="logout" action="/paytmnoplogin/controller" method="post">
                    <button type="submit" class="submitbtn">Logout</button>
                    <label class="loggedinuser"> Logged in User :<%=request.getAttribute("fullname")%> </label>
                    <input class="hiddenitem" name="hiddenitem" type="text" value="logout" />
                </form>
                    <a onclick='anchorClick()' id="anchorid"> Change Password </a>
            </div>
            <div class="changepass" id="passclass">
                <form name="changepassword" action="/paytmnoplogin/controller" onsubmit="return product()" method="post" >
                    <input type="password" placeholder="Enter Password" maxlength="10" name="password" id="password" required>
                    <input type="password" placeholder="Repeat Password" maxlength="10" name="password_repeat" id="password_repeat" required>
                    <button type="submit" class="passwordsubmit">Update Password</button>
                    <input class="hiddenitem" name="hiddenitem" type="text" value="changepassword" />
                </form>
            </div>
            <div class="historydetails">
                <p style="font-family:courier;">Activity History.</p>
                <p style="font-family:courier;">This section is for Challenge 5</p
            </div>
            <div class="footer">
                <p style="text-align: center; color:#4CAF50;margin-top: 5px;"> Developed by Sreejithu Panicker</p>
            </div>
        </div>
    </body>
</html>
