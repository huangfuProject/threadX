import LocalStorageUtil from '@/common/LocalStorageUtil';
import router from '@/router';
class LoginCheck {
    static readonly LOGIN_PATH = "/user/login";

    static checkLoginToken():void {
        const token = LocalStorageUtil.getLoginToken();
        if(token === '') {
            router.push('/login');
        }
    }
}

export default LoginCheck