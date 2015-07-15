var helpers = (function() {

	function getDataFromForm($form) {
		var formArray = $form.serializeArray();
		var data = {};

		formArray.forEach(function(field) {
			data[field.name] = field.value;
		})

		return data;
	}
	return {
		getDataFromForm: getDataFromForm
	};
}());