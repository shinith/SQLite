<?php
include 'inc/hcode.php';

?>
<style>
html
{background:#0081a5;}
</style>
<center>
    
    <img src="img/icon.png" alt="" style="width:120px; margin-top:30%; margin-bottom:20px;">

</center>
<div class="row" style="padding:0px 20px;">
    <div class="col s12">
      <div class="row" style="border:1px solid #eee; border-radius:10px; background:#fff; padding:20px 15px 15px; margin: 0 0 5px 0;">
        <div class="input-field col s12">
          <input placeholder="Ex:9876543210" id="mob" type="tel" class="validate">
          <label for="first_name">Mobile Number or Email ID</label>
        </div>

        <div class="input-field col s12">
          <input placeholder="Ex:*********" id="password" type="password" class="validate">
          <label for="password">Password</label>
        </div>
      </div>
<p style="text-align:right; margin:0; padding:0; padding-right:5px;"><a href="https://app.gurujisyoga.com/enroll?view=reset" class=" btn-flat" style="text-transform:none; font-weight:600; margin:0; padding:0;">Forgot Password</a></p>
      

      <button class="btn waves-effect  s06" style="width:100%; border-radius:8px; height:40px; background:#eee; color:#0081a5;" id="signin" name="action" onclick="SignIN();">Sign in
  </button>

  <p style="text-align:center; margin:10px;  "><a href="https://app.gurujisyoga.com/enroll" class=" btn-flat" style="text-transform:none; font-weight:600; margin:0; padding:10px; padding-top:2px;">Don't have an account? Signup Here</a></p>
    
</div>

<script>


function SignIN() {

var username = $('#mob').val();
var password = $('#password').val();
$('#signin').html("Please Wait...");
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
var response = JSON.parse(this.responseText);
if(response.sts==1)
{
  console.log(response.email);
 AndroidInterface.Login(username,password,response.email);
 
}
else
{
  AndroidInterface.showToast(response.msg);
  $('#signin').html("Sign In");
}
      console.log(response.msg);
    }
  };
  xhttp.open("POST", "https://app.gurujisyoga.com/api/login-app.php", true);
  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  xhttp.send("username="+username+"&passwd="+password);
}
</script>


</div>
<?php include 'inc/fcode.php'; ?>