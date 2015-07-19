var MovieController = function() {
	var resource = new Resource("/movie-search/movies");

	var $searchInput = $("#search-movie-input");
	var $addForm = $("#add-movie-form");
	var $searchWaitText = $(".search-wait-text");

	var $searchResultsDiv = $(".search-results");
	var $searchResultsList = $("#search-results-list");
	var $searchInfoText = $searchResultsDiv.find(".search-info-text");
	var $searchErrorText = $searchResultsDiv.find(".error-text");

	var $addSuccessText = $addForm.find(".success-text");
	var $addErrorText = $addForm.find(".error-text");

	function showNoMoviesFoundMsg() {
		$searchWaitText.hide();
		$searchResultsList.empty();

		$searchInfoText.show();
		$searchInfoText.html("No movies found");
		$searchErrorText.hide();
	}

	function showSearchErrorMsg() {
		$searchErrorText.show();
		$searchErrorText
			.html("Something went wrong. Please try again later");
	}

	function loadMovieTitles(movieTitles) {
		movieTitles.movies.forEach(function(movieMap){
			var $titleListItem = $("<li></li>");
			$titleListItem.html(movieMap.title);
			$searchResultsList.append($titleListItem);
		});
	}

	function checkTitleEntered(newMovieData) {
		var mandatoryTitleValue = newMovieData["movie-title"];

		if(!mandatoryTitleValue || 0 === mandatoryTitleValue.length) {
			$addSuccessText.hide();
			$addErrorText.show();
			$addErrorText.html("Movie title is mandatory!");
			return false;
		} 
		return true;
	}

	function showAddSuccessfulMsg(){
		$addErrorText.hide();
		$addSuccessText.show();
		$addSuccessText.html("Movie was added successfully");
	}

	this.searchMovie = function(e) {
		var currentSearchText = $searchInput.val();

		if(currentSearchText.length < 3) {
			$searchWaitText.hide();
		} else {
			$searchWaitText.show();
			$searchResultsList.empty();

			resource.query({"textToMatch": currentSearchText})
				.then(function(result) {
					$searchWaitText.hide();
					$searchInfoText.hide();
					
					if(result && result.status === 200) {
						loadMovieTitles(result);
					} else {
						showNoMoviesFoundMsg();
					}
				}, function(result) {
					showSearchErrorMsg();
				});
		}
	}

	this.addMovie = function(e) {
		var newMovieData = helpers.getDataFromForm($addForm);

		if(!checkTitleEntered(newMovieData)) {
			e.preventDefault();
			return;
		}
		resource.create(newMovieData).then(function(result) {
			if(result.status === 200) {
				showAddSuccessfulMsg();
			}
		}, function(result) {
			$addSuccessText.hide();
			$addErrorText.show();
			if(result.status === 409) {
				$addErrorText.html("A movie with this title already exists");
			} else {
				$addErrorText.html("Movie was not added successfully");
			}
		});
		e.preventDefault();
	}

	this.init = function() {
		$searchWaitText.hide();

		$(".main-container")
			.on("input", "#search-movie-input", this.searchMovie);

		$(".main-container")
			.on("submit", "#add-movie-form", this.addMovie);
	}
}