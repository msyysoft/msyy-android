package g.support.app.permission;

public interface PermissionListener {
    void onPermissonYES(int requestCode, String[] grantPermissions);
    void onPermissonNO(int requestCode, String[] noPermissions);
    void onPermissonDENY(int requestCode, String[] denyPermissions);
}
