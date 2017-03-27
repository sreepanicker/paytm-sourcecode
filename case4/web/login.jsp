<%-- 
    Document   : login
    Created on : 24-Mar-2017, 11:34:54 PM
    Author     : sreejithu.panicker
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Login Page</title>

        <style>
        /* Full-width input fields */
        input[type=text], input[type=password] {
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            box-sizing: border-box;
        }

        .signupbtn {
            background-color: #4CAF50;
            color: white;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            cursor: pointer;
            width: 100%;
        }


        /* Add padding to container elements */
        .container {
            padding: 16px;
        }

        form{
            border:1px solid #ccc;
            position: absolute;
            height: 40%;
            width: 40%;
            top: 45%;
            left: 40%;
            margin-top: -50px;
            margin-left: -100px;
        }
        .hiddenitem{
            visibility: hidden;
            display:none;
        }

        </style>
    </head>
<body>
<form action="/paytmnoplogin/controller" method="post">
  <div class="container">
      <%String error = (String)request.getAttribute("error"); %>
      <label><b>User Name</b><%=(error!=null)?error:"" %></label>
    <input type="text" placeholder="User Name" name="username" maxlength="10" required>

    <label><b>Password</b></label>
    <input type="password" placeholder="Enter Password" maxlength="10" name="password" required>
    <button type="submit" class="signupbtn">Sign In</button>
    <p><a href="/paytmnoplogin/newuser.jsp">New User</a></p>
    <input class="hiddenitem" type="text" name="hiddenitem" value="login" />
  </div>
</form>
</body>
</html>
