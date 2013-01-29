<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Affectation parcours/filières 3ème année Centrale Marseille</title>
    <!-- Bootstrap -->
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/select2.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<nav class="navbar navbar-inverse">
		</nav>
		
		<div class="row">
			<div class="span8">	            
	            <form:form action="/admin/saveConfig" method="POST" commandName="when" class="well form-horizontal">
						<legend>Configuration</legend>
						<label for="start">Date de départ
						<form:errors path="start" cssStyle="color:red" >
						La date n'a pas le bon format
						</form:errors>
						</label>
						<div class="input-append">
						<form:input id="start" path="start" class="span3" />
						<span class="add-on">dd/MM/yyyy/hh:mm</span>
						</div>
						<br />
						
						<label for="firstEmail">Date d'envoi du premier email de rappel
						<form:errors path="firstEmail" cssStyle="color:red" >
						La date n'a pas le bon format
						</form:errors>
						</label>
						<div class="input-append">
						<form:input id="firstEmail" path="firstEmail" class="span3" />
						<span class="add-on">dd/MM/yyyy/hh:mm</span>
						</div>
						<br />
						
						<label for="secondEmail">Date d'envoi du 2ème email de rappel
						<form:errors path="secondEmail" cssStyle="color:red" >
						La date n'a pas le bon format
						</form:errors>
						</label>
						<div class="input-append">
						<form:input id="secondEmail" path="secondEmail" class="span3" />
						<span class="add-on">dd/MM/yyyy/hh:mm</span>
						</div>
						<br />
						
						<label for="end">Date de fin
						<form:errors path="end" cssStyle="color:red" >
						La date n'a pas le bon format
						</form:errors>
						</label>
						<div class="input-append">
						<form:input id="end" path="end" class="span3" />
						<span class="add-on">dd/MM/yyyy/hh:mm</span>
						</div>
						<br />

						
        				<button class="btn btn-primary pull-right" name="commit" type="submit">Valider</button>
        				<br />

				</form:form>
				
				<br />
				
				<span id="blocResultat"></span>
			</div>
	            
			<div class="span4">
				<div class="pull right">
					<a href="/logout">Deconnexion <i class="icon-off"></i></a>
				</div>
			</div>
		</div>
	</div>
	
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
    <script src="/js/bootstrap-typeahead.min.js" type="text/javascript"></script>
    <script src="/js/select2.min.js" type="text/javascript"></script>
	
</body>
</html>
        
   
