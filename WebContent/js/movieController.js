var MovieController = function() {
	var resource = new Resource("/movie-search/movies");
	var $searchInput = $("#search-movie-input");
	var $addForm = $("#add-movie-form");


	this.searchMovie = function(e) {
		var currentSearchText = $searchInput.val();
		var $searchResultsDiv = $(".search-results");
		var $searchInfoText = $(".search-info-text");
		var $searchResultsList = $("#search-results-list");

		if(currentSearchText.length >= 3) {
			$searchInfoText.css("visibility", "visible");

			resource.query({"textToMatch": currentSearchText}).then(function(err) {
				// wtf
			}, function(result) {
				$searchInfoText.css("visibility", "hidden");
				if(result.status !== 200) {
					$searchResultsDiv.find(".error-text").html(
						"Something went wrong :( Please try again later");
				} else {
					var movieTitles = result.responseText;
					movieTitles = movieTitles.slice(1, movieTitles.length - 1);
					movieTitles = movieTitles.split(", ");

					$searchResultsList.empty();
					movieTitles.forEach(function(elem){
						var $titleListItem = $("<li></li>");
						$titleListItem.html(elem);
						$searchResultsList.append($titleListItem);
					});
					console.log($searchResultsList);
				}
			});
		} else {
			$searchInfoText.css("visibility", "hidden");
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