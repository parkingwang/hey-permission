HeyPermission
===

[ ![Download](https://api.bintray.com/packages/parkingwang/maven/HeyPermission/images/download.svg) ](https://bintray.com/parkingwang/maven/HeyPermission/_latestVersion)

HeyPermission 是一个基于注解及部分回调的对 Android 动态权限调用进行封装的库。

# 特性

- 单个权限/权限组申请
- 注解回调结果
  * `@PermissionsGranted` 申请权限均被允许
  * `@PermissionsDenied` 申请权限被拒绝（下次还可询问用户）
  * `@PermissionsNeverAskAgain` 申请权限被永久拒绝
  * `@PermissionsResult` 申请权限结果（允许或拒绝都会回调）
- 注解回调方法可以是任意参数
  * 如果参数中有 int，则第一个 int 参数将接收此次的 requestCode
  * 如果参数中有 List<String>，则第一个 List 参数将接收此次申请通过（`@PermissionsGranted`）或被拒绝（其他注解）的 permissions
- 回调注解支持处理多个 requestCode
  * 仅申请允许的注解只能处理单个 requestCode
  * 其他权限结果注解支持多个 requestCode，如果为空则表示处理所有 requestCode
- 提示用户权限的重要性（回调方法）
  * 默认的对话框供调用
- 被永久拒绝后提示用户去系统设置添加权限的对话框
- 支持以下组件
  * Activity
  * Fragment
  * SupportFragment

# 使用方法

## 依赖

```groovy
    compile 'com.parkingwang:hey-permission:0.1'
    // OR
    implementation 'com.parkingwang:hey-permission:0.1'
```

## Java 代码

请求权限：

```java
    HeyPermission.requestPermissions(this, requestCode, permission);
```

处理权限回调

```java

    // 权限被永久拒绝后的回调。建议在 BaseActivity 或 BaseFragment 中统一处理。
    @PermissionsNeverAskAgain
    public void onPermissionNeverAskAgain(int requestCode) {
        PermissionDialogs.showDefaultAppSettingsDialog(this, requestCode);
    }

    @PermissionsDenied
    public void onPermissionDenied() {
        Toast.makeText(this, R.string.msg_you_denied_the_permission_request, Toast.LENGTH_SHORT).show();
    }

    // 实现了 RationaleCallback 接口后会回调此方法，返回 true 表示已弹出提示权限重要性的对话框。
    @Override
    public boolean onShowRationale(PermissionRequestExecutor executor, int requestCode,
                                   String[] permissions, boolean callOnResult) {
        if (!callOnResult) {
            PermissionDialogs.showDefaultRationaleDialog(this, executor, requestCode, permissions);
            return true;
        }
        return false;
    }

    // 必须在此回调中调用 HeyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    // 建立在 BaseActivity 或 BaseFragment 中重写此方法。
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HeyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
```

## Javadoc

https://parkingwang.github.io/hey-permission/

# 参考项目

- yanzhenjie/[AndPermission](https://github.com/yanzhenjie/AndPermission)
- permissions-dispatcher/[PermissionsDispatcher](https://github.com/permissions-dispatcher/PermissionsDispatcher)
- googlesamples/[easypermissions](https://github.com/googlesamples/easypermissions)
- yoojia/[NextEvents](https://github.com/yoojia/NextEvents)

