<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale Marseille</title>
<!-- Bootstrap -->
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
<link href="/css/student-admin-page.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li class="nav-header">Statistiques</li>
					<li><a href="/admin/run/main/statistics/choice1">Choix</a></li>
					<li><a href="/admin/run/main/statistics/form/synthese">Dossiers</a></li>
					<li class="dropdown-submenu"><a href="#">Répartition parcours</a>
						<ul class="dropdown-menu">
							<c:forEach var="js" items="${allJs}">
								<li><a href="/admin/run/main/statistics/inverse-repartition/js/${js.abbreviation}">${js.abbreviation}</a></li>
							</c:forEach>
						</ul></li>
					<li class="dropdown-submenu"><a href="#">Répartition filières</a>
						<ul class="dropdown-menu">
							<c:forEach var="ic" items="${allIc}">
								<li><a href="/admin/run/main/statistics/inverse-repartition/ic/${ic.abbreviation}">${ic.abbreviation}</a></li>
							</c:forEach>
						</ul></li>

					<li class="nav-header">Parcours</li>
					<li><a href="/admin/run/main/choices/improvement-course/synthese/choice1">Synthèse</a></li>
					<c:forEach var="spec" items="${allIc}">
						<li><a href="/admin/run/main/choices/improvement-course/details/${spec.abbreviation}/choice1">${spec.abbreviation}</a></li>
					</c:forEach>

					<li class="nav-header">Filières</li>
					<li><a href="/admin/run/main/choices/job-sector/synthese/choice1">Synthèse</a></li>
					<c:forEach var="spec" items="${allJs}" varStatus="status">
						<li><a href="/admin/run/main/choices/job-sector/details/${spec.abbreviation}/choice1">${spec.abbreviation}</a></li>
					</c:forEach>
				</ul>
			</div>

			<div class="span7">
				<c:choose>
					<c:when test="${student != null}">
						<h3>
							<legend>${student.name}</legend>
						</h3>
						<h4>Voeux</h4>
						<table class="table table-bordered table-striped table-condensed">
							<thead>
								<tr>
									<th></th>
									<th>Parcours</th>
									<th>Filières</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td><b>Choix 1</b>
									<td>${student.icChoices.choice1}</td>
									<td>${student.jsChoices.choice1}</td>
								</tr>
								<tr>
									<td><b>Choix 2</b>
									<td>${student.icChoices.choice2}</td>
									<td>${student.jsChoices.choice2}</td>
								</tr>
								<tr>
									<td><b>Choix 3</b>
									<td>${student.icChoices.choice3}</td>
									<td>${student.jsChoices.choice3}</td>
								</tr>
								<tr>
									<td><b>Choix 4</b>
									<td>${student.icChoices.choice4}</td>
									<td>${student.jsChoices.choice4}</td>
								</tr>
								<tr>
									<td><b>Choix 5</b>
									<td>${student.icChoices.choice5}</td>
									<td>${student.jsChoices.choice5}</td>
								</tr>
							</tbody>
						</table>

						<h4>Documents</h4>
						<c:choose>
							<c:when test="${student.hasFilledResume}">
								<a href="/files/cv_${student.login}">CV</a>
							</c:when>
							<c:otherwise>
Cet élève n'a pas encore déposé son CV.
</c:otherwise>
						</c:choose>
						<br />

						<c:choose>
							<c:when test="${student.hasFilledLetterIc}">
								<a href="/files/lettre_parcours_${student.login}">Lettre parcours</a>
							</c:when>
							<c:otherwise>
Cet élève n'a pas encore déposé sa lettre de motivation pour son choix de parcours d'approfondissement.
</c:otherwise>
						</c:choose>
						<br />

						<c:choose>
							<c:when test="${student.hasFilledLetterJs}">
								<a href="/files/lettre_filiere_${student.login}">Lettre filière</a>
							</c:when>
							<c:otherwise>
Cet élève n'a pas encore déposé sa lettre de motivation pour son choix de filière métier.
</c:otherwise>
						</c:choose>
						<br />
						<br />

						<h4>Résultats</h4>
						<div class="marge">
							<h5>Contentieux</h5>
						</div>
						<c:choose>
							<c:when test="${fn:length(student.contentious) > 0 }">
								<table class="table table-bordered table-striped table-condensed">
									<thead>
										<tr>
											<th>Cycle</th>
											<th>Semestre</th>
											<th>UE</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="contentious" items="${student.contentious}">
											<tr>
												<td>${contentious.cycle}</td>
												<td>${contentious.semester}</td>
												<td>${contentious.ueCode}</td>
											</tr>
										</c:forEach>
								</table>
							</c:when>
							<c:otherwise>
Cet élève n'a aucun contentieux.
</c:otherwise>
						</c:choose>
						<div class="marge">
							<h5>Moyennes GPA</h5>
						</div>
						<table class="table-bordered table-striped table-condensed">
							<thead>
								<tr>
									<th>Semestre</th>
									<th>Moyenne Gpa</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>5</td>
									<td>${student.gpaMeans[0]}</td>
								</tr>
								<tr>
									<td>6</td>
									<td>${student.gpaMeans[1]}</td>
								</tr>
								<tr>
									<td>7</td>
									<td>${student.gpaMeans[2]}</td>
								</tr>
							</tbody>
						</table>
						<br />
						<div class="marge">
							<h5>Résultats par UE</h5>
						</div>
						<table class="table-bordered table-striped table-condensed">
							<thead>
								<tr>
									<th>Semestre</th>
									<th>Cycle</th>
									<th>Code UE</th>
									<th>Grade GPA</th>
									<th>Grade ECTS</th>
									<th>Session</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="result" items="${student.results}">
									<tr>
										<td>${result.semester}</td>
										<td>${result.cycle}</td>
										<td>${result.code}</td>
										<td>${result.gpa}</td>
										<td>${result.ects}</td>
										<td>${result.session}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

					</c:when>
					<c:otherwise>
						<div class="alert alert-error">L'étudiant demandé est inconnu.</div>
					</c:otherwise>
				</c:choose>

				<br /> <br />
			</div>

			<div class="span3">
				<tags:rightColumnAdmin />
			</div>
		</div>
	</div>

</body>
</html>