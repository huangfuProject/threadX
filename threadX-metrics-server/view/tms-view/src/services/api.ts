import axios from 'axios';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { ElMessage } from 'element-plus'
import {ErrorStatusConstants} from '../constants/ErrorStatusConstants'
import router from '@/router';
import LocalStorageUtil from '@/common/LocalStorageUtil';
import LoginCheck from '@/common/LoginCheck';


class ApiUtils {
  private static axiosInstance = axios.create({
    baseURL: 'http://127.0.0.1:8081',
    // 其他Axios配置，如超时时间等
    timeout: 30000,
    headers: {
      'Source-Name': 'ThreadX-tms'
    }
  });

  /**
   * 添加默认的拦截器
   */
  public static setupInterceptors(): void {
    //请求拦截器
    this.axiosInstance.interceptors.request.use(function (config) {
      config.headers['threadx-token'] = LocalStorageUtil.getLoginToken()
      // 显示进度条
      NProgress.start();
      return config;
    }, function (error) {
      // 对请求错误做些什么
      NProgress.done();
      return Promise.reject(error);
    });

    /**
     * 响应拦截器
     */
    this.axiosInstance.interceptors.response.use(function (response) {
      // 2xx 范围内的状态码都会触发该函数。
      //解构数据
      const { code, message, result } = response.data;
      if (ErrorStatusConstants.SUCCESS === code) {
        // 隐藏进度条
        NProgress.done();
        // 返回成功的data
        return result;
      } else {
        // 隐藏进度条
        NProgress.done();

        ElMessage.error(message)
        if(ErrorStatusConstants.USER_NOT_LOGIN_ERROR === code) {
          LocalStorageUtil.logoutDataRemove()
          router.push('/login');
        }
        // 抛出错误提示
        return Promise.reject(new Error(message));
      }
    }, function (error) {
      // 超出 2xx 范围的状态码都会触发该函数。
      // 对响应错误做点什么
      // 隐藏进度条
      NProgress.done();
      // console.log(error.response.status)
      ElMessage.error(error.message)
      return Promise.reject(error);
    });
  }

  /**
   * post请求方法
   * @param url 请求的地址
   * @param data 发送的数据
   * @param config 可选的Axios请求配置
   * @returns 最终的返回结果
   */
  public static async post<T>(url: string, data?: any, config?: any): Promise<T> {
    //当不是登录的时候，检查是否存在token，不存在token就跳转到登录页面，不在进行请求
    if(url !== LoginCheck.LOGIN_PATH) {
      LoginCheck.checkLoginToken()
    }
    return await this.axiosInstance.post(url, data, config);
  }

  /**
   * get请求方法
   * @param url 请求的地址 
   * @param params 参数  json类型的就行
   * @param config 可选的Axios请求配置
   * @returns 最终的返回结果
   */
  public static async get<T>(url: string, params?: any, config?: any): Promise<T> {
    LoginCheck.checkLoginToken()
    return await this.axiosInstance.get(url, { params, ...config });
  }
}

// 设置默认拦截器
ApiUtils.setupInterceptors();


export default ApiUtils;
