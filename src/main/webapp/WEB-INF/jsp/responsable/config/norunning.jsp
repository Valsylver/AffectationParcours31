<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale
	Marseille</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<div class="row">
			<br />
			<br />
			<div class="alert alert-info">Vous êtes répertorié en tant que responsable 
				<c:choose>
					<c:when test="${specialization.type == specialization.JOB_SECTOR}">
						de la filière métier <b>${specialization.name} (${specialization.abbreviation})</b>.
					</c:when>
					<c:otherwise>
						du parcours d'approfondissement <b>${specialization.name} (${specialization.abbreviation})</b>.
					</c:otherwise>
				</c:choose>
				<br />
				<b>Le processus n'est actuellement pas en cours</b>.
			</div>
			<a href="${pageContext.request.contextPath}/logout" class="btn btn-danger"><i class="icon-white icon-off"></i> Deconnexion</a>
		</div>
	</div>
</body>
</html>
