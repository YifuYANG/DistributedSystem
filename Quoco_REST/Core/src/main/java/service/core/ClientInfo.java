package service.core;

/**
 * Interface to define the state to be stored in ClientInfo objects
 * 
 * @author Rem
 *
 */
public class ClientInfo {
	public static final char MALE				= 'M';
	public static final char FEMALE				= 'F';

	private String name;
	private char gender;
	private int age;
	private int points;
	private int noClaims;
	private String licenseNumber;

	public ClientInfo(String name, char sex, int age, int points, int noClaims, String licenseNumber) {
		this.name = name;
		this.gender = sex;
		this.age = age;
		this.points = points;
		this.noClaims = noClaims;
		this.licenseNumber = licenseNumber;
	}
	
	public ClientInfo() {}

	public String getName() {
		return name;
	}

	public char getGender() {
		return gender;
	}

	public int getAge() {
		return age;
	}

	public int getPoints() {
		return points;
	}

	public int getNoClaims() {
		return noClaims;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setNoClaims(int noClaims) {
		this.noClaims = noClaims;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
}
