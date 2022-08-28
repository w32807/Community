// 자바스크립트의 정규식에서는 양 끝에 /를 넣어줘야함
function emailChk (email){
    const regex = /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
	var a = regex.test($.trim(email));
    return regex.test($.trim(email));
}

function phoneNumChk (phoneNum){
    const regex = /\d{2,3}[- .]\d{3,4}[- .]\d{4}/;
    return regex.test($.trim(phoneNum));
}

// 최소 8 자, 대문자 하나 이상, 소문자 하나, 숫자 하나 및 특수 문자 하나 이상
function pwdChk (pwd){
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}/;
    return regex.test($.trim(pwd));
}

function nameChk (name){
    const regex = /^[가-힣a-zA-Z]+$/;
    return regex.test($.trim(name));
}

function reset(obj){
	$(obj).val("");
	return $(obj);
}

function focus(obj){
	$(obj).focus();
	return $(obj);
}

