package SEProject;

public final class GlobalID {
    private static String userIdentifier;

    public static void setUserIdentifier(String username){
        userIdentifier = username;
    }

    public static String getUserIdentifier(){
        return userIdentifier;
    }
}
