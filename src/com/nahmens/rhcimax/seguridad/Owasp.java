package com.nahmens.rhcimax.seguridad;

/**
 * OWASP Hashing Java
 * https://www.owasp.org/index.php/Hashing_Java
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;

import android.util.Base64;

import com.nahmens.rhcimax.database.modelo.Usuario;



public class Owasp {

	private final static int ITERATION_NUMBER = 1000;

	public Owasp() {
	}

	/**
	 * Authenticates the user with a given login and password If password and/or
	 * login is null then always returns false. If the user does not exist in
	 * the database returns false.
	 * 
	 * @param login
	 *            String The login of the user
	 * @param password
	 *            String The password of the user
	 * @param mSalt
	 *            String Salt of the user
	 * @param encodedPass
	 *            String The encoded password of the user
	 * @return boolean Returns true if the user is authenticated, false
	 *         otherwise
	 * @throws SQLException
	 *             If the database is inconsistent or unavailable ( (Two users
	 *             with the same login, salt or digested password altered etc.)
	 * @throws NoSuchAlgorithmException
	 *             If the algorithm SHA-1 is not supported by the JVM
	 */
	public boolean authenticate(String login, String password, String mSalt,
			String encodedPass) throws SQLException, NoSuchAlgorithmException {

		try {
			boolean userExist = true;
			// INPUT VALIDATION
			if (login == null || password == null) {
				// TIME RESISTANT ATTACK
				// Computation time is equal to the time needed by a legitimate
				// user
				userExist = false;
				login = "";
				password = "";
			}

			String digest, salt;
			if (encodedPass != null & mSalt != null) {
				digest = encodedPass;
				salt = mSalt;

			} else { // TIME RESISTANT ATTACK (Even if the user does not exist
						// the
				// Computation time is equal to the time needed for a legitimate
				// user
				digest = "000000000000000000000000000=";
				salt = "00000000000=";
				userExist = false;
			}

			byte[] bDigest = base64ToByte(digest);
			byte[] bSalt = base64ToByte(salt);

			// Compute the new DIGEST
			byte[] proposedDigest = getHash(ITERATION_NUMBER, password, bSalt);

			return Arrays.equals(proposedDigest, bDigest) && userExist;
		} catch (IOException ex) {
			throw new SQLException(
					"Database inconsistant Salt or Digested Password altered");
		}
	}

	/**
	 * Encrypts user's password and generate a salt
	 * 
	 * @param user
	 *            User who is assigned salt and encrypted password
	 * @return boolean Returns true if the password and salt were generated.
	 *         False otherwise.
	 */
	public boolean encrypt(Usuario user) {

		String password = user.getPassword();
		String login = user.getLogin();

		try {

			if (login != null && password != null) {
				// Uses a secure Random not a simple Random
				SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
				
				// Salt generation 64 bits long
				byte[] bSalt = new byte[8];
				random.nextBytes(bSalt);
				
				// Digest computation
				byte[] bDigest = getHash(ITERATION_NUMBER, password, bSalt);
				String sDigest = byteToBase64(bDigest);
				String sSalt = byteToBase64(bSalt);

				user.setPassword(sDigest);
				user.setSalt(sSalt);

				return true;
				
			} else {
				return false;
			}
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * From a password, a number of iterations and a salt, returns the
	 * corresponding digest
	 * 
	 * @param iterationNb
	 *            int The number of iterations of the algorithm
	 * @param password
	 *            String The password to encrypt
	 * @param salt
	 *            byte[] The salt
	 * @return byte[] The digested password
	 * @throws NoSuchAlgorithmException
	 *             If the algorithm doesn't exist
	 * @throws UnsupportedEncodingException
	 */
	public byte[] getHash(int iterationNb, String password, byte[] salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.reset();
		digest.update(salt);
		byte[] input = digest.digest(password.getBytes("UTF-8"));
		for (int i = 0; i < iterationNb; i++) {
			digest.reset();
			input = digest.digest(input);
		}
		return input;
	}

	/**
	 * From a base 64 representation, returns the corresponding byte[]
	 * 
	 * @param data
	 *            String The base64 representation
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] base64ToByte(String data) throws IOException {

		return Base64.decode(data, Base64.DEFAULT);
	}

	/**
	 * From a byte[] returns a base 64 representation
	 * 
	 * @param data
	 *            byte[]
	 * @return String
	 * @throws IOException
	 */
	public static String byteToBase64(byte[] data) {
		
		return new String(Base64.encode(data, Base64.DEFAULT));

	}
}
