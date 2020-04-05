var xhr;
function checkUsername() {
    var username=document.getElementById('username').value;
    xhr=new XMLHttpRequest();
    xhr.onreadystatechange = process;
    xhr.open("Get","usernameIsExist?username="+username,true);
    xhr.send(null);
}
function  process() {
    if(xhr.readyState==4){
        if(xhr.status==200){
            var responseInfo=xhr.responseText;
            var msg=document.getElementById('isExistInfo');
            if(responseInfo=='Exist'){
                msg.innerText='this username has been used';
            }else if (responseInfo== 'Not Exist'){
                msg.innerText='this username is usable';
            }
        }
    }
    
}