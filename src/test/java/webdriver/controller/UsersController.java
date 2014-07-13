/**
 * 
 */
package webdriver.controller;

import java.util.Vector;

import webdriver.PropertiesResourceManager;

/**
 * Singleton store information about users. Contains available's users pool, current logged user and user's type.
 * credentials stored in file 'credentials.properties'
 */
public class UsersController {
	private static final String SEPARATOR = ",";
	private static UsersController instance;
	private static Vector<User> simpleUsers;
	private static Vector<User> adminUsers;
	private static User currentLoggedUser;
	private static UserType currentLoggedType;
	private static PropertiesResourceManager credentials = new PropertiesResourceManager("credentials.properties");

	/**
	 * getCurrentLoggedUser
	 * @return currentLoggedUser
	 */
	public static User getCurrentLoggedUser() {
		return currentLoggedUser;
	}

	/**
	 * getCurrentLoggedType
	 * @return currentLoggedType
	 */
	public static UserType getCurrentLoggedType() {
		return currentLoggedType;
	}

	/**
	 * read property and fill collections
	 */
	private UsersController() {
		simpleUsers = getFilledVector(credentials.getProperty("simpleUserName"), credentials.getProperty("simpleUserPassword"));
		adminUsers = getFilledVector(credentials.getProperty("credentials_logins_admin"), credentials.getProperty("credentials_passwords_admin"));
	}

	/**
	 * Implementation of the Singleton
	 * @return UsersController() instance
	 */
	public static synchronized UsersController getInstance() {
		if (instance == null) {
			instance = new UsersController();
		}
		return instance;
	}

	/**
	 * filling User's collection
	 * @param pLogins Logins
	 * @param pPasswords Passwords
	 * @return Vector<User>
	 */
	private Vector<User> getFilledVector(final String pLogins, final String pPasswords) {
		String[] logins = pLogins.split(SEPARATOR);
		String[] passwords = pPasswords.split(SEPARATOR);
		Vector<User> vector = new Vector<UsersController.User>();
		for (int i = 0; i < logins.length; i++) {
			User temp = new User(logins[i], passwords[i]);
			vector.add(temp);
		}
		return vector;
	}

	/**
	 * return available User, see {@link User}
	 * @param type - user's type, see {@link UserType}
	 * @param differentFrom - if not null, will return other User with the same type
	 * @return User
	 */
	public User getUser(final UserType type, final User differentFrom) {
		User toReturn = null;
		Vector<User> users = null;
		switch (type) {
		case USER:
			users = simpleUsers;
			break;
		case ADMIN:
			users = adminUsers;
			break;
		default:
			break;
		}
		// looking for user
		for (User user : users) {
			if (differentFrom != null) {
				if (!user.equals(differentFrom)) {
					toReturn = user;
					break;
				}
			} else {
				toReturn = user;
				break;
			}
		}
		currentLoggedUser = toReturn;
		currentLoggedType = type;
		return toReturn;
	}

	/**
	 * return available User by Index, see {@link User}
	 * @param type User type
	 * @param userIndex User index
	 * @return User
	 */
	public User getUserByIndex(final UserType type, final int userIndex) {
		int index = 1;
		User toReturn = null;
		Vector<User> users = null;
		switch (type) {
		case USER:
			users = simpleUsers;
			break;
		case ADMIN:
			users = adminUsers;
			break;
		default:
			break;
		}

		// looking for user
		for (User user : users) {
			if (index++ == userIndex) {
				toReturn = user;
				break;
			}
		}
		currentLoggedUser = toReturn;
		currentLoggedType = type;
		return toReturn;
	}

	/**
	 * @param type UserType
	 * @return first available user of current type
	 */
	public User getUserByType(final UserType type) {
		return getUser(type, null);
	}

	/**
	 * @param type UserType
	 * @return User, which differs from {@link #currentLoggedUser}
	 */
	public User getAnotherUser(final UserType type) {
		return getUser(type, currentLoggedUser);
	}

	/**
	 * Describes User. Contains Login and password.<br>
	 * Can be compared {@link #equals(Object)} with other User
	 */
	public class User {
		/**
		 * @uml.property name="login"
		 */
		private String login;
		/**
		 * @uml.property name="password"
		 */
		private String password;

		/**
		 * User
		 * @param uLogin login
		 * @param uPassword password
		 */
		public User(final String uLogin, final String uPassword) {
			login = uLogin;
			password = uPassword;
		}

		@Override
		public boolean equals(final Object obj) {
			return login.equals(((User) obj).getLogin());
		}

		/**
		 * @return login
		 * @uml.property name="login"
		 */
		public String getLogin() {
			return login;
		}

		/**
		 * @return password
		 * @uml.property name="password"
		 */
		public String getPassword() {
			return password;
		}
	}

}
