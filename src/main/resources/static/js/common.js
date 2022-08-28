var extensionRegex = new RegExp("(.*?)\.(exe|sh|zip|alz|tiff)$");
var imgMaxSize = 2097152; // 2MB

$(document).ready(function() {

	$(".datepicker").datepicker();
	$(".monthpicker").monthpicker();
	
    // 체크박스 전체 선택
	$("#allCheck").click(function() { //만약 전체 선택 체크박스가 체크된상태일경우 
		if ($("#allCheck").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
			$("input[type=checkbox]").prop("checked", true);
		}
		// 전체선택 체크박스가 해제된 경우 
		else { //해당화면에 모든 checkbox들의 체크를해제시킨다. 
			$("input[type=checkbox]").prop("checked", false);
		}
	})
	
	// 입력 시 엔터로 form태그 전송 막기	
	$('input[type="text"]').keydown(function() {
		 if (event.keyCode === 13) event.preventDefault();
	});
});

// String null Check
function isStrNull(str){
	str += '';
	str = str.trim();
	return (!str || (str === 'null') || (str === 'undefined') || (str === '') || 
			(str === 'NaN') || (str === '0') || (str === 'false')) ? true : false;
}

function comAjax(json, url, successCallback, errCallback){
	// dataType을 명시하지 않으면 모든 타입의 데이터를 서버로부터 가져올 수 있음
	$.ajax({
		url: url,
		type: "post",
		data: json,
		success: function(data) {
			if(typeof successCallback === 'function') successCallback(data);
		},
		error: function(request,status,error) {
			if(typeof errCallback === 'function') errCallback();
		}
	})
}

function formToJson(form){
	return (form.prop('tagName') === 'FORM') ? form.serializeObject() : '';
}

function saveValChkOfStr(tags){
	var result = true;
	$.each(tags, function(idx, item){
		if(!isStrNull($.trim($(item).val()))) result = false;	
	});
	return result;
}

function isNumber(num){
	num += '';
	num = num.trim();
	return (num === '' || isNaN(num)) ? false : true;
}

function numComma(num){
	var num = removeComma(num);
	//return isNumber(num) ? Number(num).toLocaleString("ko-KR", { style: 'currency', currency: 'KRW' }) : '';
	return isNumber(num) ? Number(num).toLocaleString(undefined, {maximumFractionDigits: 0}) : '';
}

function removeComma(num){
	num += '';
	return num.replace(/[^\d]+/g, '');
}

function setNumComma(obj){
    obj.value = numComma(obj.value);
}

function checkExtension(fileName, fileSize){
    if(fileSize >= imgMaxSize){
        alert("이미지 파일은 2MB 이하의 이미지만 가능합니다.");
        return false;
    }

    if(extensionRegex.test(fileName)){
        alert("해당 종류의 파일은 업로드 할 수 없습니다.");
        return false;
    }

    return true;
}

/**
 * Usage: var json = $('#form-login').serializeObject();
 * Output: {username: "admin", password: "123456"}
 * Output: {username: "admin", password: "123456", subscription: ["news","offer"]}
 * */

$.fn.serializeObject = function() {
    var obj = {};
    var arr = this.serializeArray();
    arr.forEach(function(item, index) {
        if (obj[item.name] === undefined) { // New
            obj[item.name] = item.value || '';
        } else {                            // Existing
            if (!obj[item.name].push) {
                obj[item.name] = [obj[item.name]];
            }
            obj[item.name].push(item.value || '');
        }
    });
    return obj;
};

var markingErrorField = function (response) {
    var $field, error, rejectedValue;
    const errorFields = response.responseJSON.fieldErrors;
    if(!errorFields) return;

    for(var i=0, length = errorFields.length; i<length;i++){
        error = errorFields[i];
        $field = $('#'+error['field']);
        rejectedValue = error['rejectedValue'];

        if($field && $field.length > 0){
            $field.siblings('.errorMsg').remove();
            $field.after('<span class="errorMsg">'+error.defaultMessage+'</span>');
            $field.val(rejectedValue);
        }
    }
}
