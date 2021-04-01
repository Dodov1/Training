package com.web.training.config.appConstants;

public class AppConstants {


    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 1800000; // 30 min
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/login";
    public static final String REGISTER_UP_URL = "/register";
    public static final String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";


    public static final Integer MAX_SEARCH_SUGGESTIONS = 7;


    public static final String EMAIL_VERIFICATION_MESSAGE = "Hey %s,\n" +
            "\n" +
            "You’re almost ready to start enjoying our site. http://localhost:4200/\n" +
            "\n" +
            "Simply click the big red button below to verify your email address.\n" +
            "\n" +
            "<a href=http://localhost:8080/users/%d/enable >"  +
            "\n" +
            "<button style=color:red >Activate your Account</button>\n" +
            "\n" +
            "</a>";

    public static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User Not Found!";

    public static final String USER_IS_ALREADY_TRAINER_EXCEPTION_MESSAGE = "User Is Already Trainer!";

    public static final boolean USER_ENABLED = true;

    public static final String NO_TRAINING_FOUND_WITH_ID = "No Training with id: %d found!";

    public static final String NO_DAY_FOUND_WITH_ID = "No Day with id: %d found!";

    public static final String NO_WEIGHT_DATA = "No Weight Data Added";

    public static final String NO_EXERCISE_WITH_ID = "No Exercise found with id: %d!";

    public static final String NO_TRAINER_WITH_ID = "No Trainer found with id: %d!";

    public static final String NO_TRAINER_WITH_USERNAME = "No Trainer found with username: %s!";

    public static final String USER_HAS_ALREADY_TRAINING_WITH_NAME = "User has already training with name: %s!";

    public static final String NO_USER_WITH_ID = "No User found with id: %d!";

    public static final String INVALID_SORTING_TYPE = "Invalid sorting type!";

    public static final String INVALID_SORTING_ARGUMENTS = "Invalid sorting arguments!";

    public static final String ASCENDING_ORDER_NAME = "asc";

    public static final String DESCENDING_ORDER_NAME = "desc";

    public static final String DEFAULT_PICTURE_NAME = "default-user-male.png";

    public static final String DEFAULT_USER_AUTHORITY = "ROLE_USER";

    public static final String DEFAULT_ADMIN_AUTHORITY = "ROLE_ADMIN";

    public static final String USER_ALREADY_EXISTS = "User already exists";

    public static final String DEFAULT_TRAINER_AUTHORITY = "ROLE_TRAINER";

    public static final String PICTURE_NOT_FOUND = "PICTURE_NOT_FOUND";

    public static final String PICTURE_NAMING = "user%dPic.%s";

    public static final String YOUTUBE_PREFIX = "https://www.youtube.com/embed/";

    public static final String YOUTUBE_PREFIX_START = "https://www.youtube.com";

    public static final Integer WEIGHT_STATISTIC_VIEW_NUMBER = 10;

    public static final String NO_USER_FOUND_FOR_TRAINER = "NO USER FOUND FOR TRAINER";

    public static final String USER_NOT_VERIFIED = "USER NOT VERIFIED";
}
