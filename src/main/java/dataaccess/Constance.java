package dataaccess;

public final class Constance {
    private Constance(){
        // prevent instantiation
    }

    public static final String baseUrl = "http://192.168.2.13:3000/";

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";

    public static final String Content_Type = "Content-Type";
    public static final String Content_Type_JSON = "application/json";

    public static final String login_success = "Login success";

    public static final String invalid_URI_syntax = "Invalid URI syntax";

}
