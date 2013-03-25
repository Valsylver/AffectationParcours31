package fr.affectation.domain.student;

public class UeResult implements Comparable<UeResult>{
	
	private String cycle;

	private String code;

	private String semester;

	private int session;

	private String ects;

	private float gpa;

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}

	public String getEcts() {
		return ects;
	}

	public void setEcts(String ects) {
		this.ects = ects;
	}

	public float getGpa() {
		return gpa;
	}

	public void setGpa(float gpa) {
		this.gpa = gpa;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	@Override
	public int compareTo(UeResult other) {
		String semester = getSemester();
		String otherSemester = other.getSemester();
		if ((semester != null) && (otherSemester != null)) {
			int semesterEquals = otherSemester.compareTo(semester);
			if (semesterEquals != 0) {
				return semesterEquals;
			} 
			else {
				String code = getCode();
				String otherCode = other.getCode();
				if ((code != null) && (otherCode != null)){
					int codeEquals = code.compareTo(otherCode);
					if (codeEquals != 0) {
						return codeEquals;
					}
				}
			}
		}
		return 0;
	}

}
