var MovieController = function() {
	var resource = new Resource("/movies");
	var $searchInput = $("#search-movie-input");
	var $addForm = $("#add-movie-form");

	this.searchMovie = function(e) {
		var currentSearchText = $searchInput.val();
		if(currentSearchText.length >= 3) {
			resource.query(currentSearchText);
		}
	}

	this.addMovie = function(e) {
		// validate form
		var newMovieData = helpers.getDataFromForm($addForm);
		resource.create(newMovieData);
		console.log("a");
		e.preventDefault();
	}

	this.init = function() {
		$(".main-container").on("input", 
			"#search-movie-input", this.searchMovie);

		$(".main-container").on("submit", 
			"#add-movie-form", this.addMovie);
	}
}