<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale Marseille</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/student-admin-page.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-final/jquery-1.8.3.min.js" type="text/javascript"></script>
<script>
function inverseValidationIc(){
	query("ic");
	}
function inverseValidationJs(){
	query("js");
	}
function query(type){
	var login = document.getElementById("login").innerHTML;
	var path = document.getElementById("path").innerHTML;
	if (type == "ic"){
		var isValidated = document.getElementById("isValidatedIc").innerHTML;		
	}
	else{
		var isValidated = document.getElementById("isValidatedJs").innerHTML;
	}
	$.ajax({
		type: "POST",
		url: path + "/admin/run/main/student/inverse-validation",
		data: "type="+type+"&login="+login+"&currentValidation="+isValidated, 
		success: function(){
			var name = document.getElementById("name").innerHTML;
			var message;
			var button;
			var textNew;
			var typeReal;
			if (type == "ic"){
				button = document.getElementById("buttonIc");
				var text = "textIc";
				typeReal = "parcours d'approfondissement";
				if (isValidated == "true"){
					message = " est désormais <b>refusé(e)</b> pour son premier choix de parcours d'approfondissement.";
					button.className = "btn btn-success";	
					button.innerHTML = "<i class='icon-white icon-ok'></i> Accepter";
					document.getElementById("isValidatedIc").innerHTML = "false";
					textNew = "refusé";
				}
				else{
					message = " est désormais <b>accepté(e)</b> pour son premier choix de parcours d'approfondissement.";
					button.className = "btn btn-danger";	
					button.innerHTML = "<i class='icon-white icon-remove'></i> Refuser";
					document.getElementById("isValidatedIc").innerHTML = "true";
					textNew = "accepté";
				}
			}
			if (type == "js"){
				button = document.getElementById("buttonJs");
				var text = "textJs";
				typeReal = "filière métier";
				if (isValidated == "true"){
					message = " est désormais <b>refusé(e)</b> pour son premier choix de filière métier.";
					button.className = "btn btn-success";	
					button.innerHTML = "<i class='icon-white icon-ok'></i> Accepter";
					document.getElementById("isValidatedJs").innerHTML = "false";
					textNew = "refusé";
				}
				else{
					message = " est désormais <b>accepté(e)</b> pour son premier choix de filière métier.";
					button.className = "btn btn-danger";	
					button.innerHTML = "<i class='icon-white icon-remove'></i> Refuser";
					document.getElementById("isValidatedJs").innerHTML = "true";
					textNew = "accepté";
				}
			}
			document.getElementById(text).innerHTML = "Cet(te) élève est actuellement " + textNew + " pour son premier choix de " + typeReal + ".";
			$('#infoValidation').html("<div class='alert alert-info'><b>" + name + "</b>" + message + "</div>");
		}
	});
}
</script>
</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />
		<div id="path" style="display:none">${pageContext.request.contextPath}</div>

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li class="nav-header">Statistiques</li>
					<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/choice1">Choix</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/form/synthese">Dossiers</a></li>
					<li class="dropdown-submenu"><a href="#">Répartition parcours</a>
						<ul class="dropdown-menu">
							<c:forEach var="js" items="${allJs}">
								<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/inverse-repartition/js/${js.abbreviation}">${js.abbreviation}</a></li>
							</c:forEach>
						</ul></li>
					<li class="dropdown-submenu"><a href="#">Répartition filières</a>
						<ul class="dropdown-menu">
							<c:forEach var="ic" items="${allIc}">
								<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/inverse-repartition/ic/${ic.abbreviation}">${ic.abbreviation}</a></li>
							</c:forEach>
						</ul></li>
					
					<li class="dropdown-submenu"><a href="#">Autres choix</a>
						<ul class="dropdown-menu">
							<li class="dropdown-submenu"><a href="#">Parcours</a> 
								<ul class="dropdown-menu">
								<c:forEach var="ic" items="${allIc}">
									<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/repartition-other-choice2/1/${ic.abbreviation}">${ic.abbreviation}</a>
									</li>
								</c:forEach>
								</ul>
							</li>
							<li class="dropdown-submenu"><a href="#">Filières</a>
								<ul class="dropdown-menu">
								<c:forEach var="js" items="${allJs}">
									<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/repartition-other-choice2/2/${js.abbreviation}">${js.abbreviation}</a>
									</li>
								</c:forEach>
								</ul>
							</li>
						</ul></li>

					<li class="nav-header">Parcours</li>
					<li><a href="${pageContext.request.contextPath}/admin/run/main/choices/improvement-course/synthese/choice1">Synthèse</a></li>
					<c:forEach var="spec" items="${allIc}">
						<li><a href="${pageContext.request.contextPath}/admin/run/main/choices/improvement-course/details/${spec.abbreviation}/choice1">${spec.abbreviation}</a></li>
					</c:forEach>

					<li class="nav-header">Filières</li>
					<li><a href="${pageContext.request.contextPath}/admin/run/main/choices/job-sector/synthese/choice1">Synthèse</a></li>
					<c:forEach var="spec" items="${allJs}" varStatus="status">
						<li><a href="${pageContext.request.contextPath}/admin/run/main/choices/job-sector/details/${spec.abbreviation}/choice1">${spec.abbreviation}</a></li>
					</c:forEach>
				</ul>
			</div>

			<div class="span8">
				<c:choose>
					<c:when test="${student != null}">
						<div id="login" style="display:none">${student.login}</div>
						<div id="name" style="display:none">${student.name}</div>
						<h3>
							${student.name} <a href="${pageContext.request.contextPath}/admin/run/main/student/edit-student-form/${login}" class="btn btn-primary btn-small pull-right"><i class="icon-white icon-pencil"></i></a>
						</h3>
						<c:if test="${not empty change }">
							<div class="alert alert-info">${change}</div>
						</c:if>
						<h4>Filière d'origine : ${student.origin}</h4>
						<h4>Voeux</h4>
						<table class="table table-bordered table-striped table-condensed">
							<thead>
								<tr>
									<th></th>
									<th><center>Parcours</center></th>
									<th><center>Filières</center></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td><b>Choix 1</b>
									<td><center>${student.icChoices.choice1}</center></td>
									<td><center>${student.jsChoices.choice1}</center></td>
								</tr>
								<tr>
									<td><b>Choix 2</b>
									<td><center>${student.icChoices.choice2}</center></td>
									<td><center>${student.jsChoices.choice2}</center></td>
								</tr>
								<tr>
									<td><b>Choix 3</b>
									<td><center>${student.icChoices.choice3}</center></td>
									<td><center>${student.jsChoices.choice3}</center></td>
								</tr>
								<tr>
									<td><b>Choix 4</b>
									<td><center>${student.icChoices.choice4}</center></td>
									<td><center>${student.jsChoices.choice4}</center></td>
								</tr>
								<tr>
									<td><b>Choix 5</b>
									<td><center>${student.icChoices.choice5}</center></td>
									<td><center>${student.jsChoices.choice5}</center></td>
								</tr>
							</tbody>
						</table>
						
						<c:if test="${validationAvailable}">
							<h4>Validation</h4>
							<div style="display:none">
								<div id="isValidatedIc">${isValidatedIc}</div>
								<div id="isValidatedJs">${isValidatedJs}</div>
							</div>
							<div id="infoValidation"></div>
							<c:choose>
								<c:when test="${isValidatedIc}">
									<div id="textIc" style="display:inline;">Cet(te) élève est actullement accepté dans son premier choix de parcours d'approfondissement.</div>
									<a id="buttonIc" onclick="inverseValidationIc()" class="btn btn-danger"><i class="icon-white icon-remove"></i> Refuser</a>
								</c:when>
								<c:otherwise>
									<div id="textIc" style="display:inline;">Cet(te) élève est actullement refusé dans son premier choix de parcours d'approfondissement.</div> 
									<a id="buttonIc" onclick="inverseValidationIc()" class="btn btn-success"><i class="icon-white icon-ok"></i> Accepter</a>
								</c:otherwise>
							</c:choose>
							<br />
							<c:choose>
								<c:when test="${isValidatedJs}">
									<div id="textJs" style="display:inline;">Cet(te) élève est actullement accepté dans son premier choix de filière métier.</div> 
									<a id="buttonJs" onclick="inverseValidationJs()" class="btn btn-danger"><i class="icon-white icon-remove"></i> Refuser</a>
								</c:when>
								<c:otherwise>
									<div id="textJs" style="display:inline;">Cet(te) élève est actullement refusé dans son premier choix de filière métier.</div> 
									<a id="buttonJs" onclick="inverseValidationJs()" class="btn btn-success"><i class="icon-white icon-ok"></i> Accepter</a>
								</c:otherwise>
							</c:choose>
						</c:if>

						<h4>Documents</h4>
						<c:choose>
							<c:when test="${student.hasFilledResume}">
								<a href="${pageContext.request.contextPath}/files/cv_${student.login}.pdf">CV</a>
							</c:when>
							<c:otherwise>
Cet(te) élève n'a pas déposé son CV.
</c:otherwise>
						</c:choose>
						<br />

						<c:choose>
							<c:when test="${student.hasFilledLetterIc}">
								<a href="${pageContext.request.contextPath}/files/lettre_parcours_${student.login}.pdf">Lettre parcours</a>
							</c:when>
							<c:otherwise>
Cet(te) élève n'a pas déposé sa lettre de motivation pour son choix de parcours d'approfondissement.
</c:otherwise>
						</c:choose>
						<br />

						<c:choose>
							<c:when test="${student.hasFilledLetterJs}">
								<a href="${pageContext.request.contextPath}/files/lettre_filiere_${student.login}.pdf">Lettre filière</a>
							</c:when>
							<c:otherwise>
Cet(te) élève n'a pas déposé sa lettre de motivation pour son choix de filière métier.
</c:otherwise>
						</c:choose>
						<br />
						<br />

						<h4>Résultats</h4>
						<div class="marge">
							<h5 class="muted">Contentieux</h5>
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
Cet(te) élève n'a aucun contentieux.
</c:otherwise>
						</c:choose>
						<div class="marge">
							<h5 class="muted">Moyennes GPA</h5>
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
							<h5 class="muted">Résultats par UE</h5>
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

			<div class="span2">
				<tags:rightColumnAdmin />
			</div>
		</div>
	</div>

</body>
</html>