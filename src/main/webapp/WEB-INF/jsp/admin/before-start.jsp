<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Affectation parcours/filière 3ème année Centrale Marseille</title>
    <!-- Bootstrap -->
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="/css/select2.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<tags:headerAdmin />
		
		<center>
			<a class="btn btn-danger btn-large" href="/admin/config/stopProcess"><i class="icon-white icon-stop"></i><br /> Stopper le processus</a>
			<br />
		</center>
		<hr />
		
		<div class="row">
			<div class="span3">
			</div>
			<div class="span6">
				Un processus a été lancé.<br />
				Date de départ : ${when.start}<br />
				Date d'envoi du premier mail de rappel : ${when.firstEmail}<br />
				Date d'envoi du second mail de rappel : ${when.secondEmail}<br />
				Date de fin des soumissions : ${when.endSubmission}<br />
				Date de fin : ${when.end}
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="/js/jquery/jquery-latest.js"></script> 
	<script type="text/javascript" src="/js/jquery/jquery.tablesorter.min.js"></script> 
	<script src="/js/select2.min.js" type="text/javascript"></script>
</body>
</html>