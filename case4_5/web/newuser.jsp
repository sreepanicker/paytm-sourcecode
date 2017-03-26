<%-- 
    Document   : newuser
    Created on : 25-Mar-2017, 12:04:29 AM
    Author     : sreejithu.panicker
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>New User</title>
        <style>
            form{
                border:1px solid #ccc;
                position: absolute;
                height: 60%;
                width: 40%;
                top: 35%;
                left: 35%;
                margin-top: -50px;
                margin-left: -100px;
            }
            input[type=text], input[type=password] {
                width: 100%;
                padding: 12px 20px;
                margin: 8px 0;
                display: inline-block;
                border: 1px solid #ccc;
                box-sizing: border-box;
            }


            button {
                background-color: #4CAF50;
                color: white;
                padding: 14px 20px;
                margin: 8px 0;
                border: none;
                cursor: pointer;
                width: 100%;
            }



            .container {
                padding: 16px;
            }


            .clearfix::after {
                content: "";
                clear: both;
                display: table;
            }
            
            .hiddenitem{
                visibility: hidden; 
                display:none;
            }
            
        </style>
        <script>
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
    <form action="/paytmlogin/controller" onsubmit="return product() " method="post">
    <div>
        <label><b>New User Creation</b></label>  
    </div>
  <div class="container">
    <%String error = (String)request.getAttribute("error"); %>
    <label><b>Login Name</b><%=(error!=null)?error:"" %></label>
    <input type="text" placeholder="Enter login Name" maxlength="10" name="loginname" required>
    
    <label><b>Full user Name</b></label>
    <input type="text" placeholder="Enter Full user Name" maxlength="20" name="username" required>
    
    <label><b>Password</b></label>
    <input type="password" placeholder="Enter Password" maxlength="10" name="password" id="password" required>

    <label><b>Repeat Password</b></label>
    <input type="password" placeholder="Repeat Password" maxlength="10" name="password_repeat" id="password_repeat" required>
    <button type="submit" class="signupbtn">Sign Up</button>
    <input class="hiddenitem" name="hiddenitem" type="text" value="newuser" />
  </div>
</form>
   
</body>
</html>
