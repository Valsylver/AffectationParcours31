<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale
	Marseille</title>
<!-- Bootstrap -->
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
<link href="/css/student-admin-page.css" rel="stylesheet">

<script src="/js/jquery/jquery-1.8.3.js"></script>
<script src="/js/jquery/jquery-ui-1.9.2.custom.min.js"></script>

</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li><a href="/admin/administration/specialization">Spécialisations</a></li>
					<li><a href="/admin/administration/students">Elèves</a></li>
					<li class="active"><a href="/admin/administration/process">Processus</a></li>
				</ul>
			</div>
		
			<div class="span7">
				<a href="/admin/stop-process" class="btn btn-danger"><i
					class="icon-white icon-stop"></i> Arrêter le processus en cours</a> <br />
			</div>
			<tags:rightColumnAdmin />
		</div>
	</div>

</body>
</html>