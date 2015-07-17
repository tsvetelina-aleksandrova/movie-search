var MovieController = function() {
	var resource = new Resource("/movie-search/movies");
	var $searchInput = $("#search-movie-input");
	var $addForm = $("#add-movie-form");


	this.searchMovie = function(e) {
		var currentSearchText = $searchInput.val();
		var $searchResultsDiv = $(".search-results");
		var $searchInfoText = $(".search-wait-text");
		var $searchResultsList = $("#search-results-list");

		if(currentSearchText.length >= 3) {
			$searchInfoText.css("visibility", "visible");
			$searchResultsDiv.find(".error-text").empty();

			resource.query({"textToMatch": currentSearchText}).then(function(err) {
				// wtf
			}, function(result) {
				$searchInfoText.css("visibility", "hidden");
				if(result.status !== 200) {
					if(result.status === 204){
						$(".search-info-text").html("No movies found");
					}
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
		var newMovieData = helpers.getDataFromForm($addForm);
		var mandatoryTitleValue = newMovieData["movie-title"];
		console.log(mandatoryTitleValue.length);
		if(!mandatoryTitleValue || 0 === mandatoryTitleValue.length) {
			$addForm.find(".error-text").html("Movie title is mandatory!");
			e.preventDefault();
			return;
		} 
		resource.create(newMovieData).then(function(err) {
			//
		}, function(result){
			if(result.status === 200) {
				$addForm.find(".success-text").html("Movie was added successfully");
			} else {
				if(result.status === 409) {
					$addForm.find(".error-text").html("A movie with this title already exists");
				} else {
					$addForm.find(".error-text").html("Movie was not added successfully");
				}
			}
		});
		e.preventDefault();
	}

	this.init = function() {
		$(".main-container").on("input", 
			"#search-movie-input", this.searchMovie);

		$(".main-container").on("submit", 
			"#add-movie-form", this.addMovie);
	}
}