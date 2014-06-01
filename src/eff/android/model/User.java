/**
 * 
 */
package eff.android.model;

import android.util.Base64;

/**
 * @author Manu
 * 
 */
public class User {

	// Data to retrieve a user
	private final String email;
	private final String password;
	public String token;

	// Data full filled with the retrieved user
	private String id;
	private String firstName;
	private String lastName;

	/**
	 * Sample Response JSON
	 */
	// {
	// "id": 314,
	// "firstName": "Oliver",
	// "lastName": "Kotte",
	// "email": "kotte@atb-bremen.de",
	// "registeredThroughEFF": true,
	// "ldapLogin": null,
	// "linkedInId": "5gUbaXUCog",
	// "registrationDate": 1399042586414,
	// "registrationStatus": "CONFIRMED",
	// "resetPWRequestDate": null,
	// "uploadCacheId": "ecf52a15-3d3d-4b97-bc97-8625c2deda4b",
	// "profilePictureFile": null,
	// "receivingNotifications": true
	// }

	/**
	 * 
	 */
	public User(String email, String password) {
		this.email = email;
		this.password = password;

		this.token = Base64.encodeToString((email + ":" + password).getBytes(),
				Base64.NO_WRAP);

		// This way does not work
		// try {
		// this.token = Base64.encodeToString(
		// (this.email + ":" + this.password).getBytes("UTF-8"),
		// Base64.DEFAULT);
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }

	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + ", token="
				+ token + ", id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + "]";
	}

}
