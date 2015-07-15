<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Movie Searcher</title>
<link rel="stylesheet" type="text/css" href="lib/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="lib/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>

<body>
<div class="search-div">
<h3>Search</h3>
<p class="info-text">Enter at least 3 letters to start searching</p>
<input type="text" class="form-control" name="movie-search-input" placeholder="Search..." ></input>
<div class="search-results">
</div>
</div>

<div class="add-div">
<h3>Add</h3>
<p class="info-text">Add new movie</p>
<form>
<input type="text" class="form-control" name="movie-title" placeholder="Title" ></input>
<textarea class="form-control" name="movie-descr" placeholder="Description" ></textarea>
<button type="submit" class="btn btn-success">Add</button>
</form> 
</div>

<script src="lib/jquery/dist/jquery.js"></script>

</body>
</html>