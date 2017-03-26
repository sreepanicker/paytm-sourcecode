<%-- 
    Document   : search
    Created on : 23-Mar-2017, 4:30:58 PM
    Author     : sreejithu.panicker
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html>
<style>
input[type=text]{
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
}
label {
	width: 100%;
	display: inline-block;
}

.submitbtn {
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    cursor: pointer;
    width: 50%;
    position: absolute;
    left: 25%;
}
form {
	border:1px solid #ccc;
	position: absolute;
	height: 35%;
	width: 40%;
	top: 45%;
	left: 40%;
	margin-top: -50px;
        margin-left: -100px;
}

.container {
    padding: 16px;
}
</style>
<body>
<form  name="search" action="/paytmlcbo/searchservice" onsubmit="return product()"  method="post">
  <div class="container">
    <label><b>You can search LCBO products here</b></label>
    <label><b>Sample product id's:10512,432856,110056 </b></label>
    <input type="text"  maxlength="10" id="productid" name="productid" placeholder="please enter product id"  required>
    <button type="submit" class="submitbtn">Search</button>
  </div>
</form>
<script>
        function product() {
            var inputvalue;
            inputvalue = document.getElementById("productid").value;
            if (isNaN(inputvalue) || inputvalue ===" ") {
                alert("Input not valid");
                return false;
            }
            return true;
        }
</script>
</body>
</html>