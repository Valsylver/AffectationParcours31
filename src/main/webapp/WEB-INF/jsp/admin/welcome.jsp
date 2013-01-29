<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Affectation parcours/filières 3ème année Centrale Marseille</title>
    <!-- Bootstrap -->
    <link href="/css/bootstrap.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<nav class="navbar navbar-inverse">
			<div class="navbar-inner">
				<div class="pull-right">
					 <div class="btn-group">
	            		<button class="btn btn-action dropdown-toggle" data-toggle="dropdown">Success <span class="caret"></span></button>
	                		<ul class="dropdown-menu">
			                  <li><a href="#">Action</a></li>
			                  <li><a href="#">Another action</a></li>
			                  <li><a href="#">Something else here</a></li>
			                  <li class="divider"></li>
			                  <li><a href="#">Separated link</a></li>
	                		</ul>
	              	</div>
              	</div>
				<form class="navbar-search pull-left" action="">
					<input class="search-query" type="text" placeholder="Recherche">
				</form>
			</div>
		</nav>
		
		<div class="row">
			<div class="span9">
				<legend>Administration de l'affectation en parcours et filières 3A</legend>

				<div>
					<p><br /> <a href="/admin/parcours">Résultats pour les parcours d'approfondissement</a> <br />
					<a href="/admin/filieres">Résultats pour les filières métiers</a>
				</div>
			</div>
			<div class="span3">
				<div class="pull right">
					<a href="/logout">Deconnexion<i class="icon-off"></i></a>
				</div>
			</div>
		</div>
	</div>
	<script src="/js/bootstrap.min.js"></script>
</body>
</html>


