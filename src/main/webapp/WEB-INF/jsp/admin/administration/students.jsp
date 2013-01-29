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
					<li class="active"><a href="/admin/administration/students">Elèves</a></li>
					<li><a href="/admin/administration/process">Processus</a></li>
				</ul>
			</div>
		
			<div class="span7">
				<a href="#" class="btn btn-primary"><i
					class="icon-white icon-ok"></i> Sauvegarder les modifications</a>
				<div class="row">
					<div class="span2">
						<h4>Promo ${promo}</h4>
						<ul id="promo" class="unstyled">
							<c:forEach var="student" items="${studentsConcerned}"
								varStatus="status">
								<li><a id="promo${status.index}" title="${student.login}" href="javascript:void(0);" onclick="removeStudent(this.id);"><i
										class="icon-minus-sign"></i></a> ${student.name}</li>
							</c:forEach>
						</ul>
					</div>
					<div class="span2">
						<h4>Elèves à exclure</h4>
						<ul id="exclusion" class="unstyled">
							<c:forEach var="student" items="${studentsToExclude}"
								varStatus="status">
								<li><a id="exclusion${status.index}" href="javascript:void(0);" title="${student.login}" onclick="addStudent(this.id);"><i
										class="icon-plus-sign"></i></a> ${student.name}</li>
							</c:forEach>
						</ul>
					</div>
					<div class="span2">
						<h4>Elèves en césure</h4>
						<div id="resume"></div>
					</div>
				</div>
			</div>
			<tags:rightColumnAdmin />
		</div>
	</div>

</body>
<script src="/js/students-exclusion.js" type="text/javascript"></script>
</html>
