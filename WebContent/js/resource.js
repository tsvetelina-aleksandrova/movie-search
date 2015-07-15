var Resource = function(url) {
	this.query = function(queryParams) {
		return Q($.ajax({
		    url: url,
		    method: "get",
		    data: queryParams,
		    dataType: "json"
		}));
	}

	this.create = function(data) {
		return Q($.ajax({
		    url: url, 
		    method: "post",
		    data: data,
		    dataType: "json"
		}));
	}
}