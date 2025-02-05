/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package g.support.app.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yan Zhenjie on 2016/9/9.
 */
public class AndPermission {

    /**
     * In the Activity.
     *
     * @param activity {@link Activity}.
     * @return {@link Permission}.
     */
    public static Permission with(Activity activity) {
        return new ImplPermission(activity);
    }

    /**
     * In the Activity.
     *
     * @param fragment {@link Fragment}.
     * @return {@link Permission}.
     */
    public static Permission with(Fragment fragment) {
        return new ImplPermission(fragment);
    }

    /**
     * Request permissions in the activity.
     *
     * @param activity    {@link Activity}.
     * @param requestCode request code.
     * @param permissions all permissions.
     */
    public static void send(Activity activity, int requestCode, String... permissions) {
        with(activity).requestCode(requestCode).permission(permissions).send();
    }

    /**
     * Request permissions in the activity.
     *
     * @param fragment    {@link Fragment}.
     * @param requestCode request code.
     * @param permissions all permissions.
     */
    public static void send(Fragment fragment, int requestCode, String... permissions) {
        with(fragment).requestCode(requestCode).permission(permissions).send();
    }

    /**
     * Should show rationale permissions;
     *
     * @param activity    {@link Activity}.
     * @param permissions permissions.
     * @return true, other wise false.
     */
    public static boolean getShouldShowRationalePermissions(Activity activity, String... permissions) {
        return PermissionUtils.getShouldShowRationalePermissions(activity, permissions).length > 0;
    }

    /**
     * Should show rationale permissions;
     *
     * @param fragment    {@link Fragment}.
     * @param permissions permissions.
     * @return true, other wise false.
     */
    public static boolean getShouldShowRationalePermissions(Fragment fragment, String... permissions) {
        return PermissionUtils.getShouldShowRationalePermissions(fragment, permissions).length > 0;
    }

    /**
     * Check permissions;
     *
     * @param context     {@link Context}.
     * @param permissions permissions.
     * @return true, other wise false.
     */
    public static boolean checkPermission(Context context, String... permissions) {
        return PermissionUtils.getDeniedPermissions(context, permissions).length <= 0;
    }

    /**
     * Check permissions;
     *
     * @param fragment    {@link Fragment}.
     * @param permissions permissions.
     * @return true, other wise false.
     */
    public static boolean checkPermission(Fragment fragment, String... permissions) {
        return PermissionUtils.getDeniedPermissions(fragment, permissions).length <= 0;
    }


    /**
     * Parse the request results.
     *
     * @param o            {@link Activity} or {@link Fragment}.
     * @param requestCode  request code.
     * @param permissions  all permissions.
     * @param grantResults results.
     */
    public static void onRequestPermissionsResult(Object o, int requestCode, String[] permissions, int[] grantResults, PermissionListener listener) {
        if (listener == null)
            return;
        List<String> deniedPermissions = new ArrayList<>(1);
        for (int i = 0; i < grantResults.length; i++)
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                deniedPermissions.add(permissions[i]);

        if (deniedPermissions.size() == 0)
            listener.onPermissonYES(requestCode, permissions);
        else {
            String[] rationalePermissions = PermissionUtils.getShouldShowRationalePermissions(o, deniedPermissions.toArray(new String[deniedPermissions.size()]));
            if (rationalePermissions.length == 0) // Remind users of the purpose of permissions.
                listener.onPermissonDENY(requestCode, permissions);
            else
                listener.onPermissonNO(requestCode, deniedPermissions.toArray(new String[deniedPermissions.size()]));
        }

    }

    private AndPermission() {
    }

}
