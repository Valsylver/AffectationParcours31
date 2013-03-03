<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale Marseille</title>
<link href="/css/bootstrap.min.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
<script src="/js/jquery/jquery-1.8.3.js"></script>
<script src="/js/jquery/jquery-ui-1.9.2.custom.min.js"></script>

</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li><a href="/admin/run/settings/admins">Administrateurs</a></li>
					<li><a href="/admin/run/settings/students">Elèves</a></li>
					<li class="active"><a href="/admin/run/settings/export">Export</a></li>
					<li><a href="/admin/run/settings/mail1">Mail 1 <c:choose><c:when test="${mail1Activated}">[actif]</c:when><c:otherwise>[non actif]</c:otherwise></c:choose></li></a>
					<li><a href="/admin/run/settings/mail2">Mail 2 <c:choose><c:when test="${mail2Activated}">[actif]</c:when><c:otherwise>[non actif]</c:otherwise></c:choose></li></a></li>
					<li><a href="/admin/run/settings/spontaneous-mail">Mail Spontané</a></li>
					<li><a href="/admin/run/settings/process">Processus</a></li>
					<li><a href="/admin/run/settings/specializations">Spécialisations</a></li>
				</ul>
			</div>

			<div class="span10">
				<a href="/files/results/resultats-pdf.pdf" class="btn btn-primary"><i class="icon-white icon-download-alt"></i> Télécharger les résultats en pdf</a> <br /><br />
				<a href="/files/results/resultats-xls-eleves.xls" class="btn btn-primary"><i class="icon-white icon-download-alt"></i> Télécharger les résultats en xls (élèves)</a> <br /> <br />
				<a href="/files/results/resultats-xls-spec.xls" class="btn btn-primary"><i class="icon-white icon-download-alt"></i> Télécharger les résultats en xls (spécialisations)</a> <br />
			</div>
		</div>
	</div>

</body>
<script src="/js/students-exclusion.js" type="text/javascript"></script>
</html>
