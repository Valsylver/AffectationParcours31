package fr.affectation.domain.student;

public class Contentious implements Comparable<Contentious>{

	private String cycle;
	private String semester;
	private String ueCode;

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public String getName() {
		return semester;
	}

	public void setSemester(String name) {
		this.semester = name;
	}

	public String getUeCode() {
		return ueCode;
	}

	public void setUeCode(String ueCode) {
		this.ueCode = ueCode;
	}

	public String getSemester() {
		return semester;
	}
	
	@Override
	public int compareTo(Contentious other) {
		String otherSemester = other.getSemester();
		if ((semester != null) && (otherSemester != null)){
			int compareSemester = semester.compareTo(otherSemester);
			if (compareSemester != 0){
				return compareSemester;
			}
			else{
				String otherUeCode = other.getUeCode();
				if ((ueCode != null) && (otherUeCode != null)){
					int compareUeCode = ueCode.compareTo(otherUeCode);
					return compareUeCode;
				}
			}
		}
		return 0;
	}
}
