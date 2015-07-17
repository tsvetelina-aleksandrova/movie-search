var helpers = (function() {

	function getDataFromForm($form) {
		var formArray = $form.serializeArray();
		var data = {};

		formArray.forEach(function(field) {
			data[field.name] = getSafeValue(field.value);
		})

		return data;
	}

	function getSafeValue(value) {
		var encodedValue = value.trim().replace(/[\u00A0-\u9999<>\&]/gim, function(i) {
   			return '&#'+i.charCodeAt(0)+';';
   		});
   		console.log(encodedValue);
   		return encodedValue;
	}

	return {
		getDataFromForm: getDataFromForm,
		getSafeValue: getSafeValue
	};
}());