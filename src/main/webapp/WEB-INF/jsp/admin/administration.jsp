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
			<div class="span9">
				<h3>Modification des spécialisations</h3>
				<a href="/admin/config/ic" class="btn btn-primary"><i
					class="icon-white icon-edit"></i> Parcours d'approfondissement</a> <br />
				<br /> <a href="/admin/config/js" class="btn btn-primary"><i
					class="icon-white icon-edit"></i> Filières métier</a>

				<h3>Processus</h3>
				<a href="/admin/stop-process" class="btn btn-danger"><i
					class="icon-white icon-stop"></i> Arrêter le processus en cours</a> <br />
				<br />
				<h3>
					Gestion des élèves <a href="#" class="btn btn-primary"><i
						class="icon-white icon-ok"></i> Sauvegarder les modifications</a>
				</h3>
				<div class="row">
					<div class="span3">
						<h4>Promo</h4>
						<ul class="unstyled">
							<c:forEach var="name" items="${studentsConcerned}"
								varStatus="status">
								<li id="eleve${status.index}"><a href="#"><i class="icon-minus-sign"></i></a> ${name}</li>
							</c:forEach>
						</ul>
					</div>
					<div class="span3">
						<h4>Elèves à exclure</h4>
					</div>
					<div class="span3">
						<h4>Elèves en césure</h4>
					</div>
				</div>
			</div>
			<tags:rightColumnAdmin />
		</div>
	</div>

</body>
</html>
