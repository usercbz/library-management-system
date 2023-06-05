let token = sessionStorage.getItem('token')
axios.interceptors.request.use(
    config => {
        if (token)
            config.headers['authorization'] = token
        return config
    },
    error => {
        console.log(error)
        return Promise.reject(error)
    }
)

axios.interceptors.response.use(function (response) {
    // 判断执行结果
    if (response.data.code == 0) {
        return Promise.reject(response.data.msg)
    }
    return response.data;
}, function (error) {
    // 一般是服务端异常或者网络异常
    console.log(error)
    if(error.response.status == 401){
        // 未登录，跳转
        setTimeout(() => {
            location.href = "../login.html"
        }, 1000);
        return Promise.reject("请先登录");
    }
    return Promise.reject("服务器异常");
});