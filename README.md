Affectation parcours/filières 3A 
================================

localhost:3306/affectation correspond à AGAP<br />
5 fichiers à modifier :
* src\main\webapp\WEB-INF\agap.xml
* src\test\java\fr\affectation\service\agap\agap.xml
* src\test\java\fr\affectation\service\configuration\agap.xml
* src\test\java\fr\affectation\service\statistics\agap.xml
* src\test\java\fr\affectation\service\student\agap.xml
<br />
<br />
localhost:3306/affectation2 correspond à la base de données où sont stockés les choix des élèves, les parcours, les filières etc ...<br />
11 fichiers à modifier :
* src\main\webapp\WEB-INF\affectation-servlet.xml
* src\test\java\fr\affectation\service\admin\AdminServiceTest-context.xml
* src\test\java\fr\affectation\service\choice\ChoiceServiceTest-context.xml
* src\test\java\fr\affectation\service\configuration\ConfigurationServiceTest-context.xml
* src\test\java\fr\affectation\service\exclusion\ExclusionServiceTest-context.xml
* src\test\java\fr\affectation\service\mail\MailServiceTest-context.xml
* src\test\java\fr\affectation\service\responsible\ResponsibleServiceTest-context.xml
* src\test\java\fr\affectation\service\specialization\SpecializationServiceTest-context.xml
* src\test\java\fr\affectation\service\statistics\StatisticsServiceTest-context.xml
* src\test\java\fr\affectation\service\student\StudentServiceTest-context.xml
* src\test\java\fr\affectation\service\validation\ValidationServiceTest-context.xml
<br />
<br />
Dans le fichier src\main\webapp\WEB-INF\spring-security.xml, il faut remplacer http://localhost:8080/affectation-3A/j_spring_cas_security_check par l'adresse réelle suivie /j_spring_cas_security_check.
<br />
<br />
L'addresse d'envoi de mail est configurée dans le fichier src\main\webapp\WEB-INF\mail-config.xml.
<br />
<br />
Les fichiers des élèves sont stockés dans les répertoires :
* src\main\webapp\WEB-INF\resources\cv
* src\main\webapp\WEB-INF\resources\lettres\filieres
* src\main\webapp\WEB-INF\resources\lettres\parcours
<br />
<br />
Des fichiers d'export (pdf, xls, zip, img) sont stockés temporairement dans le répertoire :
* src\main\webapp\WEB-INF\resources\temp