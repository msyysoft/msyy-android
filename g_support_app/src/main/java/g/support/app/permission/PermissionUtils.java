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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yan Zhenjie on 2016/9/10.
 */
class PermissionUtils {

    @TargetApi(23)
    static String[] getShouldShowRationalePermissions(Object o, String... permissions) {
        List<String> strings = new ArrayList<>(1);
        if (o instanceof Activity) {
            for (String permission : permissions)
                if (((Activity) o).shouldShowRequestPermissionRationale(permission))
                    strings.add(permission);
        } else if (o instanceof Fragment) {
            for (String permission : permissions)
                if (((Fragment) o).shouldShowRequestPermissionRationale(permission))
                    strings.add(permission);
        } else
            throw new IllegalArgumentException("The " + o.getClass().getName() + " is not support.");
        return strings.toArray(new String[strings.size()]);
    }

    static int[] getPermissionsResults(Object o, String... permissions) {
        int[] results = new int[permissions.length];
        for (int i = 0; i < results.length; i++)
            results[i] = checkPermission(o, permissions[i]);
        return results;
    }

    static String[] getDeniedPermissions(Object o, String... permissions) {
        List<String> strings = new ArrayList<>(1);
        for (String permission : permissions)
            if (checkPermission(o, permission) != PackageManager.PERMISSION_GRANTED)
                strings.add(permission);
        return strings.toArray(new String[strings.size()]);
    }

    static int checkPermission(Object o, String permission) {
        return getContext(o).checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid());
    }

    static Context getContext(Object o) {
        if (o instanceof Context)
            return (Context) o;
        else if (o instanceof Fragment)
            return ((Fragment) o).getActivity();
        throw new IllegalArgumentException("The " + o.getClass().getName() + " is not support to get the context.");
    }

}
