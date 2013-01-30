<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

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

		<form:form action="/admin/run/edit-exclusion" method="POST"
			commandName="studentExclusion" enctype="multipart/form-data">

			<div id="exclusionInputs">
				<c:forEach begin="0"
					end="${fn:length(studentExclusion.currentPromotion)-1}" var="i">
					<c:choose>
						<c:when test="${i < fn:length(studentsToExclude)}">
							<form:input id="excludedInput${i}" path="excluded[${i}]"
								style="display:none" value="${studentsToExclude[i].login}"></form:input>
						</c:when>
						<c:otherwise>
							<form:input id="excludedInput${i}" path="excluded[${i}]"
								style="display:none"></form:input>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</div>

			<div class="row">
				<div class="span2">
					<ul class="nav nav-list">
						<li><a href="/admin/administration/specialization">Spécialisations</a></li>
						<li class="active"><a href="/admin/administration/students">Elèves</a></li>
						<li><a href="/admin/administration/process">Processus</a></li>
					</ul>
				</div>

				<div class="span7">
					<button class="btn btn-primary" name="commit" type="submit">
						<i class="icon-white icon-ok"></i> Sauvegarder les modifications
					</button>
					<div class="row">
						<div class="span2">
							<h4>Promo ${promo}</h4>
							<ul id="promo" class="unstyled">
								<c:forEach var="student" items="${studentsConcerned}"
									varStatus="status">
									<li><a id="promo${status.index}" title="${student.login}"
										href="javascript:void(0);" onclick="removeStudent(this.id);"><img src="/img/minus-new.png"></img></a> ${student.name}</li>
								</c:forEach>
							</ul>
						</div>
						<div class="span2">
							<h4>Elèves à exclure</h4>
							<ul id="exclusion" class="unstyled">
								<c:forEach var="student" items="${studentsToExclude}"
									varStatus="status">
									<li><a id="exclusion${status.index}"
										title="${student.login}" href="javascript:void(0);"
										onclick="addStudent(this.id);"><img src="/img/plus-new.png"></img></a> ${student.name}</li>
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
		</form:form>
	</div>

</body>
<script src="/js/students-exclusion.js" type="text/javascript"></script>
</html>
